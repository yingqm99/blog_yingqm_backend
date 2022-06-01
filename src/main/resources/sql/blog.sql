SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
    `user_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'user_id',
    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL UNIQUE COMMENT 'username',
    `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci UNIQUE COMMENT 'email',
    `description` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'description',
    `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'phone_number',
    `profile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'profile_pic',
    `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'password',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `blogs`;

CREATE TABLE `blogs` (
    `blog_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'blog_id',
    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'blog_name',
    `user_id` bigint UNSIGNED NOT NULL COMMENT 'user_id',
    `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'blog_category',
    `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'user_name',
    PRIMARY KEY (`blog_id`),
    FOREIGN KEY (`user_id`) REFERENCES users(`user_id`),
    FOREIGN KEY (`user_name`) REFERENCES users(`name`)
) ENGINE = InnoDB;