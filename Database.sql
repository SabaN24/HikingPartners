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

-- ----------------------------------------------------------------------------- -- 

INSERT INTO users VALUES
(1, 'Nodo', 'Sairmeli', ''),
(2, 'Vache', 'Katsadze', ''),
(3, 'Levaniko', 'Beroshvili', ''),
(4, 'Sandro', 'Jiqia', ''),
(5, 'Saba', 'Natroshvili', '');

INSERT INTO location_types VALUES
(1, 'ZGVAAAA');

INSERT INTO privacy_types VALUES
(1, 'public'),
(2, 'private');

INSERT INTO roles VALUES 
(1, 'Creator');

INSERT INTO locations VALUES
(1, 'QVABISTAVI', '45', '45', 1);

INSERT INTO hikes VALUES 
(1, 'KVELAZE MAGARI PAXODE', str_to_date('1989.12.01', '%Y.%m.%d'), str_to_date('1989.12.01', '%Y.%m.%d'), 'MAGARI PAXODI!!! SHEMODIT ALL :***', 5);



INSERT INTO comments VALUES
(1, 'რა დაგვჭირდება?', NULL, 1, 1, str_to_date('1999.12.01', '%Y.%m.%d'), 1),
(2, 'ადგილები არის?', NULL, 1, 2, str_to_date('1999.08.01', '%Y.%m.%d'), 1),
(3, 'წასვლის დროს ვერ გადმოვწევთ?', NULL, 1, 3, str_to_date('1999.09.01', '%Y.%m.%d'), 1),
(4, 'ანი როგორ ხარ მთავარანგელოზობა ვერ მოგილოცე', NULL, 1, 4, str_to_date('1999.10.01', '%Y.%m.%d'), 1),
(5, 'უკაცრავად მაინტერესებს პატარა ბავშვის პამპერსები თუ გაქვთ, 3 ზომა შავ პარკში რო ჩამიდოთ, მადლობა', NULL, 1, 5, str_to_date('1999.11.01', '%Y.%m.%d'), 1);


INSERT INTO hike_to_user VALUES
(1, 1, 1, 1);

INSERT INTO cover_photos VALUES
(1, 1, 'მაგარი ლოკაცია დზნ', 1, '');

INSERT INTO comment_likes VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 3, 4),
(5, 3, 5),
(6, 3, 1),
(7, 4, 2),
(8, 4, 3),
(9, 5, 4),
(10, 5, 5),
(11, 5, 1);

insert into posts values
(1,"9ზე ოკრიბაშ იყავით ბერლინში მივდივართ", 1, 4, now()),
(2,"რუსი ნაშები ჩითავენ? XD XD XD", 1, 3, now()),
(3,"ხალვა მომაქ მე", 1, 2, now()),
(4, "შავი ქამრები არ დამანახოთ!!! მწვანე კაია", 1, 1, now());

insert into comments 
(comment_text, post_ID, hike_ID, user_ID, comment_time, privacy_type)
		values 
			( "9ზე ვერ ვასწრებ 10იის ნახზე მანდ ვარ", 1, 1, 2, now(), 2),
			( "ე ვერ მოვასწრებთ !!! ", 1, 1, 4, now(), 2),
			( "კაია", 2, 1, 2, now(), 2);




update hikes set hike_name = "მოლაშქრეთა კლუბი აიეტი", description = "ტურის ორგანიზატორია მოლაშქრეთა კლუბი აიეტი, გასვლის და დაბრუნების თარიღი : 15 ივლისი - 16 ივლისი, მთავარი ლოკაციები : თბილისი, ბათმი, თურქეთი, ყაზბეგი" where id = 1;

-- select ID from posts order by ID desc limit 1;

-- insert into posts (hike_id, post_text, user_id, post_time) values(1,"vache", 1, str_to_date('1989.12.01', '%Y.%m.%d'));

-- select  * from posts;
-- select  * from comments;
-- select  * from comment_likes;
-- insert into comments(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)values ( 'TestTestTest', 1, 1, '15/06/17 21:05:37', 2, 14);

-- insert into comments(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)values ( 'TestTestTest', 1, 1, '15/06/17 21:08:23',1, null);

-- insert into posts (post_text, hike_id, user_id, post_time) values("TestTestTest",1, 1, '15/06/17 19:00:04');
-- delete from posts where ID = 36;
-- delete from comments where ID = 100;
-- delete from post_likes where ID = 5;
-- delete from comment_likes where ID = 12;

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

create PROCEDURE get_private_comments(post_id INT)
BEGIN
SELECT comments.ID, comments.comment_text, comments.user_ID, comments.comment_time
FROM comments
WHERE comments.post_ID = post_id AND comments.privacy_type = 2;

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

DELIMITER $$

create PROCEDURE get_post_comments_likes(post_id INT, hike_id int, privacy int)
BEGIN
if(privacy = 1) then
	SELECT cl.comment_ID comment_id, cl.user_ID user_id
	FROM comment_likes cl 
	inner join (select ID from comments c where c.hike_ID = hike_id) c on c.ID = cl.comment_id;
else
	SELECT cl.comment_ID comment_id, cl.user_ID user_id
	FROM comment_likes cl 
	inner join (select ID from comments c where c.post_ID = post_id) c on c.ID = cl.comment_id;
end if;

END$$

call get_post_comments_likes(