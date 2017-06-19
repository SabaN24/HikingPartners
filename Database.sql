Create Database IF NOT EXISTS Hiking_Partners
Default Character set UTF8;

USE Hiking_Partners;

CREATE TABLE IF NOT EXISTS hikes (
    ID INT NOT NULL,
    hike_name NVARCHAR(50),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    description TEXT,
    max_people INT,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS users (
	ID INT AUTO_INCREMENT NOT NULL,
    first_name NVARCHAR(10) NOT NULL,
    last_name NVARCHAR(20) NOT NULL,
    img_url nvarchar(50),
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS roles (
    ID INT AUTO_INCREMENT NOT NULL,
    role_name NVARCHAR(10) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS location_types (
    ID INT AUTO_INCREMENT NOT NULL,
	type_name NVARCHAR(15) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS hike_to_user (
    ID INT AUTO_INCREMENT NOT NULL,
    hike_ID INT NOT NULL,
    user_ID INT NOT NULL,
    role_ID INT NOT NULL,
    PRIMARY KEY (hike_ID),
	FOREIGN KEY (ID) REFERENCES hikes(ID),
    FOREIGN KEY (role_ID) REFERENCES roles(ID)
);

CREATE TABLE IF NOT EXISTS locations (
    ID INT AUTO_INCREMENT NOT NULL,
    loc_name NVARCHAR(15) NOT NULL,
	longitude VARCHAR(15) NOT NULL,
    latitude VARCHAR(15) NOT NULL,
    location_type_ID INT NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (location_type_ID) REFERENCES location_types(ID)
);

CREATE TABLE IF NOT EXISTS hike_to_location (
	ID INT AUTO_INCREMENT NOT NULL,
    hike_ID INT NOT NULL,
    location_ID INT NOT NULL,
    priority INT NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (hike_ID) REFERENCES hikes(ID),
    FOREIGN KEY (location_ID) REFERENCES locations(ID)
);

CREATE TABLE IF NOT EXISTS posts (
    ID INT AUTO_INCREMENT NOT NULL,
	post_text TEXT NOT NULL,
    hike_ID INT NOT NULL,
	user_ID INT NOT NULL,
    post_time DATETIME NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (hike_ID) REFERENCES hikes(ID)
);

CREATE TABLE IF NOT EXISTS privacy_types (
	ID INT AUTO_INCREMENT NOT NULL,
    type_name NVARCHAR(20) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS comments (
    ID INT AUTO_INCREMENT NOT NULL,
	comment_text TEXT NOT NULL,
    post_ID INT,
    hike_ID INT NOT NULL,
	user_ID INT NOT NULL,
    comment_time DATETIME NOT NULL,
    privacy_type INT NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (post_ID) REFERENCES posts(ID),
	FOREIGN KEY (privacy_type) REFERENCES privacy_types(ID),
    FOREIGN KEY (hike_id) REFERENCES hikes(ID)
);

CREATE TABLE IF NOT EXISTS cover_photos (
    ID INT AUTO_INCREMENT NOT NULL,
	location_ID INT NOT NULL,
    description TEXT,
    hike_ID INT NOT NULL,
    img_url VARCHAR(40) NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (hike_ID) REFERENCES hikes(ID),
    FOREIGN KEY (location_ID) REFERENCES locations(ID)
);


CREATE TABLE IF NOT EXISTS gallery_photos (
    ID INT AUTO_INCREMENT NOT NULL,
    hike_ID INT NOT NULL,
    img_url VARCHAR(40) NOT NULL,
    user_ID INT NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (hike_ID) REFERENCES hikes(ID),
	FOREIGN KEY (user_ID) REFERENCES users(ID)
);


CREATE TABLE IF NOT EXISTS post_likes (
    ID INT AUTO_INCREMENT NOT NULL,
    post_ID INT NOT NULL,
    user_ID INT NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (user_ID) REFERENCES users(ID),
	FOREIGN KEY (post_ID) REFERENCES posts(ID)
);


CREATE TABLE IF NOT EXISTS comment_likes (
    ID INT AUTO_INCREMENT NOT NULL,
    comment_ID INT NOT NULL,
    user_ID INT NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (user_ID) REFERENCES users(ID),
	FOREIGN KEY (comment_ID) REFERENCES comments(ID)
);

-- --------------------------------------------------------------------- --
DELIMITER $$

CREATE PROCEDURE get_creator_info(user_id INT)
BEGIN
SELECT users.id, users.first_name, users.last_name, users.img_url
FROM hike_to_user INNER JOIN users
ON users.id = user_id
WHERE hike_to_user.user_ID = user_id AND role_id = 1;

END$$


DELIMITER $$

CREATE PROCEDURE get_cover_photos(hike_id INT)
BEGIN
SELECT cover_photos.id, cover_photos.img_url, locations.loc_name, cover_photos.description
FROM cover_photos INNER JOIN locations
ON cover_photos.location_ID = locations.id
WHERE cover_photos.hike_ID = hike_id;

END$$

DELIMITER $$

CREATE PROCEDURE get_public_comments(hike_id INT)
BEGIN
SELECT comments.ID, comments.comment_text, comments.user_ID, comments.comment_time
FROM comments
WHERE comments.hike_id = hike_id AND comments.privacy_type = 1;

END$$

DELIMITER $$

CREATE PROCEDURE get_private_comments(post_id INT)
BEGIN
SELECT comments.ID, comments.comment_text, comments.user_ID, comments.comment_time
FROM comments
WHERE comments.hike_id = hike_id AND comments.privacy_type = 2;

END$$

DELIMITER $$

CREATE PROCEDURE get_comment_likes(comment_id INT)
BEGIN
SELECT COUNT(comment_ID)
FROM comment_likes
WHERE comment_likes.comment_id = comment_id;

END$$

DELIMITER $$

CREATE PROCEDURE get_post_likes(post_id INT)
BEGIN
SELECT COUNT(post_ID)
FROM post_likes
WHERE post_likes.post_id = post_id;

END$$