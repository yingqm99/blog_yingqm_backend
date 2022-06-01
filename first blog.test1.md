# 学习笔记：IO多路复用之epoll初探
epoll是linux提供的IO多路复用三种模式select, poll, epoll中的一种，也是效率最高的一种模式。这篇文章主要介绍epoll的原理与使用。
## epoll的使用
首先，创造一个epoll实例：
```cpp
int epoll_fd = epoll_create(1);
```
这一行代码创建了一个epoll实例。epoll_create中的参数在linux2.6.8之后就并没有意义，只要大于0就可以了。  
创建了epoll实例后，我们的目标是添加epoll监听的文件以及对应的事件类型:
```cpp
epoll_event event;
event.data.fd = fd;
event.events = EPOLLIN;
int flag = epoll_ctl(epoll_fd, EPOLL_CTL_ADD, fd, &evs);
```
以上代码的意思是添加了一个对于文件fd的输入事件。其中epoll_event是一个头文件<sys/epoll.h>中定义的数据结构，到sys/epoll.h中发现这个结构长这样。
```cpp
typedef union epoll_data
{
  void *ptr;
  int fd;
  uint32_t u32;
  uint64_t u64;
} epoll_data_t;

struct epoll_event
{
  uint32_t events;
  epoll_data_t data;
} __EPOLL_PACKED;
```
下面解释一下这个数据结构。events就是事件的名称，其中常用events的种类有EPOLLIN, EPOLLOUT, EPOLLET, EPOLLERR，分别对应的是读，写，边缘触发和错误信息。这里需要介绍一下水平触发(level trigger)和边缘触发(edge trigger)。以读为例，水平触发的含义就是只要当有数据可读，那么就持续地触发事件。边缘触发仅当数据从没有可读到有可读、或者事件触发类型变为读事件并且缓冲区有数据可读等边缘条件时才会触发一次事件，之后便不再触发。比如有一个读操作事件监听，当缓冲区第一次有数据可读时，会触发一次事件。但是假如一次没有读完，那么水平触发会继续触发事件，而边缘触发则不会再次触发事件了并且剩下的数据就丢失了。在ET模式下，缓冲区的数据将会被一直读或者写知道结束(errno=EAGAIN)，这个过程是阻塞的。  
声明边缘触发地方式为：  
```cpp
epoll_event ev_et;
ev_et.data.fd = fd;
ev_et.events = EPOLLET | EPOLLIN
```  
data的类型是epoll_data。epoll_data是一个联合体数据结构，允许装入数据结构中的任一类型数据。注意epoll_data并没有规定其中应该装什么数据，它允许程序员自己实现。比如说我希望在data中装入监听事件对应的文件描述符，那么我就把fd装到data中。  
将监听的事件以及对应的文件描述符装入epoll实例中后，下面就是以轮询的方式监听这些时间有没有发生了：
```cpp
epoll_event events[10];
while(true) {
  int num = epoll_wait(epoll_fd, events, 10, 30000);
  for (int i = 0; i < num, i++) {
    if (events[i].events | EPOLLIN) {
      ssize_t read_bytes = read(events[i].data.fd, buffer, 10);
    }
    if (events[i].events | EPOLLOUT) {
      //写的逻辑
    }
    if (events[i].events | EPOLLERR) {
      //错误信息的逻辑
    }
  }
}
if (close(epoll_fd)) {
  fprintf("error!");
  return 1;
}
```
epoll_wait第一个参数是epoll实例的fd。第二个参数是一个数组的指针，所有监听到的事件全部被放在这个数组中。第三个参数是最大监听事件的个数。第四个参数是超时的时间。返回值是监听到事件的个数。当有监听到事件时，遍历所有监听到的事件并对其进行事件相对应的操作。  
以上就是epoll的基本使用。  
## epoll的原理
epoll的实现中有两个重要的数据结构：红黑树和双向链表。其中红黑树中每个节点都装着所有监听fd对应的epitem。链表中也装着触发事件fd对应的epitem。  
当epoll_create被调用时，linux kernel会创建一个名为eventpoll的数据结构，长这样：
```cpp
struct eventpoll {
  struct list_head rdllist;
  struct rb_root_cached rbr;
  struct epitem *ovflist;
  // 其余省略
}
```
其中rbr就是那个装着所有被监听fd的红黑树。rdllist时一个链表，装着已经触发的事件对应的epitem。
红黑树中每个节点有一个key和value，key的值是一个名为epoll_filefd的数据结构，value的值是一个名为epitem的数据结构。
epoll_filefd和epitem的结构如下：
```cpp
struct epoll_filefd {
  struct file *file;
  int fd;
}
struct epitem {
  struct rb_nore rbn;
  struct list_head rdllink;
  struct epitem *next;
  struct epoll_event event;
  struct eventpoll *ep;
}
```
当有一个新的事件要加入监听时，首先在红黑树中寻找是不是已经加入监听了。假如没有加入就添加到红黑树中，并且将该进程注册到设备的等待队列中，并且指定了一个回调函数。当事件就绪时，调用该回调函数将其加入到rdllist中。所以epoll实例想要看有没有事件触发，并不需要轮询查看所有被监听的文件了，只需要看rdllist中有没有东西就可以了。  
以上就是epoll的基本原理，有机会会介绍一下epoll linux上的源码。  
每个进程都有内核态用户态两种。当程序运行用户代码时就是用户态，运行内核代码时就是内核态。