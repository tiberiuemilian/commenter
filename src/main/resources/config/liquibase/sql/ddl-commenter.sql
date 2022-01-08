CREATE
DATABASE IF NOT EXISTS `commenter` DEFAULT CHARACTER SET utf8;

USE
`commenter`;

DROP TABLE IF EXISTS `author`;
CREATE TABLE `author`
(
    `id`   int         NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `comment`
(
    `id`        int           NOT NULL AUTO_INCREMENT,
    `parent_id` int DEFAULT NULL,
    `url`       varchar(2047) NOT NULL,
    `author_id` int DEFAULT NULL,
    `content`   text          NOT NULL,
    PRIMARY KEY (`id`),
    KEY         `parent_fk_idx` (`parent_id`),
    KEY         `author_idx` (`author_id`),
    FULLTEXT KEY `content_idx` (`content`),
    FULLTEXT KEY `url_idx` (`url`),
    CONSTRAINT `author_fk` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
    CONSTRAINT `parent_fk` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`)
);

CREATE TABLE `tag`
(
    `id`         int          NOT NULL AUTO_INCREMENT,
    `comment_id` int          NOT NULL,
    `name`       varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `comment_fk_idx` (`comment_id`),
    KEY          `author_idx` (`name`),
    CONSTRAINT `comment_fk` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);