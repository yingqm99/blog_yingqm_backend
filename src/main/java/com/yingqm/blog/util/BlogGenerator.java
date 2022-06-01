package com.yingqm.blog.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BlogGenerator {

    public void generateFile(String title, String userName, String text) {
        try {
            File file = new File(title + "." + userName + ".md");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
