-- R2DBC 这套自然是不会像 JPA 一样帮着建表的，所以还得自己来

CREATE TABLE `user`
(
    id           SERIAL PRIMARY KEY,
    user_name    VARCHAR(255),
    display_name VARCHAR(255),
    description  VARCHAR(255)
);
CREATE TABLE `article`
(
    id        SERIAL PRIMARY KEY,
    title     VARCHAR(255),
    digest    VARCHAR(255),
    content   VARCHAR(255),
    author    INT,
    post_time DATETIME,
    slug      VARCHAR(255),
    FOREIGN KEY (author) REFERENCES `user` (id)
);
