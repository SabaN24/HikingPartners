drop schema if exists Hiking_Partners;
Create Database IF NOT EXISTS Hiking_Partners
  Default Character set UTF8;

USE Hiking_Partners;

CREATE TABLE IF NOT EXISTS hikes (
  ID INT NOT NULL AUTO_INCREMENT,
  hike_name NVARCHAR(50),
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  description TEXT,
  max_people INT,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS users (
  ID INT AUTO_INCREMENT NOT NULL,
  facebook_ID BIGINT NOT NULL,
  first_name NVARCHAR(10) NOT NULL,
  last_name NVARCHAR(20) NOT NULL,
  profile_picture_url NVARCHAR(500),
  birth_date DATE,
  gender ENUM('male', 'female'),
  email VARCHAR(50) NOT NULL,
  about_me_text NVARCHAR(1000),
  cover_picture_url NVARCHAR(500),
  facebook_link NVARCHAR(500),
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS roles (
  ID INT AUTO_INCREMENT NOT NULL,
  role_name NVARCHAR(10) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS location_types (
  ID INT AUTO_INCREMENT NOT NULL,
  type_name NVARCHAR(15) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS hike_to_user (
  ID INT AUTO_INCREMENT NOT NULL,
  hike_ID INT NOT NULL,
  user_ID INT NOT NULL,
  role_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (hike_ID)
  REFERENCES hikes (ID),
  FOREIGN KEY (role_ID)
  REFERENCES roles (ID),
  foreign key (user_ID)
  references users(ID)
);


CREATE TABLE IF NOT EXISTS hike_to_location (
  ID INT AUTO_INCREMENT NOT NULL,
  hike_ID INT NOT NULL,
  location_lat VARCHAR(50),
  location_lng VARCHAR(50),
  location_type_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (hike_ID)
  REFERENCES hikes (ID)
);


CREATE TABLE IF NOT EXISTS gallery_photos (
  ID INT AUTO_INCREMENT NOT NULL,
  hike_ID INT NOT NULL,
  img_url VARCHAR(256) NOT NULL,
  user_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (hike_ID)
  REFERENCES hikes (ID),
  FOREIGN KEY (user_ID)
  REFERENCES users (ID)
);

CREATE TABLE IF NOT EXISTS posts (
  ID INT AUTO_INCREMENT NOT NULL,
  post_text TEXT,
  link TEXT,
  hike_ID INT NOT NULL,
  user_ID INT NOT NULL,
  post_time DATETIME NOT NULL,
  photo_ID int null,
  PRIMARY KEY (ID),
  FOREIGN KEY (hike_ID)
  REFERENCES hikes (ID),
  FOREIGN KEY (photo_ID)
  REFERENCES gallery_photos(ID),
  foreign key (user_ID)
  references users(ID)
);

CREATE TABLE IF NOT EXISTS privacy_types (
  ID INT AUTO_INCREMENT NOT NULL,
  type_name NVARCHAR(20) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS comments (
  ID INT AUTO_INCREMENT NOT NULL,
  comment_text TEXT NOT NULL,
  post_ID INT,
  hike_ID INT NOT NULL,
  user_ID INT NOT NULL,
  comment_time DATETIME NOT NULL,
  privacy_type INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (post_ID)
  REFERENCES posts (ID),
  FOREIGN KEY (privacy_type)
  REFERENCES privacy_types (ID),
  FOREIGN KEY (hike_id)
  REFERENCES hikes (ID),
  foreign key (user_ID)
  references users(ID)
);

CREATE TABLE IF NOT EXISTS cover_photos (
  ID INT AUTO_INCREMENT NOT NULL,
  description TEXT,
  hike_ID INT NOT NULL,
  img_url VARCHAR(256) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (hike_ID)
  REFERENCES hikes (ID)
);


CREATE TABLE IF NOT EXISTS post_likes (
  ID INT AUTO_INCREMENT NOT NULL,
  post_ID INT NOT NULL,
  user_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (user_ID)
  REFERENCES users (ID),
  FOREIGN KEY (post_ID)
  REFERENCES posts (ID)
);


CREATE TABLE IF NOT EXISTS comment_likes (
  ID INT AUTO_INCREMENT NOT NULL,
  comment_ID INT NOT NULL,
  user_ID INT NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (user_ID)
  REFERENCES users (ID),
  FOREIGN KEY (comment_ID)
  REFERENCES comments (ID)
);

CREATE TABLE IF NOT EXISTS requests (
  ID INT AUTO_INCREMENT NOT NULL,
  sender_ID INT NOT NULL,
  receiver_ID INT NOT NULL,
  hike_ID INT NOT NULL,
  PRIMARY KEY(ID),
  FOREIGN KEY (sender_ID) REFERENCES users(ID),
  FOREIGN KEY (receiver_ID) REFERENCES users(ID),
  FOREIGN KEY (hike_ID) REFERENCES hikes(ID)
);

create table if not exists messages (
  ID int auto_increment not null,
  from_user_id int not null,
  to_user_id int not null,
  message TEXT NOT NULL,
  message_date DATETIME NOT NULL,
  PRIMARY KEY(ID),
  FOREIGN KEY (from_user_id) REFERENCES users(ID),
  foreign key (to_user_id) references users(ID)

);

create table if not exists open_chats(
  ID int auto_increment not null,
  from_user_id int not null,
  to_user_id int not null,
  PRIMARY KEY(ID),
  FOREIGN KEY (from_user_id) REFERENCES users(ID),
  foreign key (to_user_id) references users(ID)


);



-- ----------------------------------------------------------------------------- --

insert into users (facebook_ID, first_name, last_name, profile_picture_url, birth_date, gender, email, about_me_text, cover_picture_url, facebook_link)
values
  ('1399903786758940', 'Sandro', 'Jikia', 'https://fb-s-c-a.akamaihd.net/h-ak-fbx/v/t1.0-1/12687968_963509663731690_303512796493540735_n.jpg?oh=ed9cd4b5eedc5f69c8eee5dc0d4ea08f&oe=59CAC61E&__gda__=1506912407_633aa37b5a527f13626b1276f44f5f00', NULL, 'male', 'sandro@post.com', 'Hello! I am new Hiker!', NULL, 'https://www.facebook.com/app_scoped_user_id/1399903786758940/'
  ),
  ('1297368127029077',
   'Vache',
   'Katsadze',
   'https://fb-s-a-a.akamaihd.net/h-ak-fbx/v/t31.0-1/17635142_1218461468253077_6870905881472586478_o.jpg?oh=39ab588fd93f8d3c8b5c1dc7146d4e93&oe=59D74422&__gda__=1510811644_df8d1b98f172aaca7c40e91057722a18',
   '1997-11-11',
   'male',
   'vache.vk@gmail.com',
   'Hello! I am new Hiker!',
   'https://scontent.xx.fbcdn.net/v/t1.0-9/q84/s720x720/13613_625017010930862_8045229606211891650_n.jpg?oh=a98d0adb6944671152045ebf5d951492&oe=59CFFA21',
   'https://www.facebook.com/app_scoped_user_id/1297368127029077/'
  ),
  ('10209073007213123',
   'Saba',
   'Natroshvili',
   'https://fb-s-c-a.akamaihd.net/h-ak-fbx/v/t1.0-1/12308736_10205094390590194_7418540499813895437_n.jpg?oh=e204555956efa5b5444301dd56f9f94d&oe=59D91AAD&__gda__=1506812275_76fb45e06235001aa6401e372276f0d2',
   NULL,
   'male',
   'sabanatroshvili@yahoo.com',
   'Hello! I am new Hiker!',
   'https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/13417545_10206253488526918_2372119789953736284_n.jpg?oh=24c863192b37a0973f1e3759a2b8f46f&oe=5A054DD9',
   'https://www.facebook.com/app_scoped_user_id/10209073007213123/'
  ),
  ('1530047160352334',
   'Levan',
   'Beroshvili',
   'https://fb-s-b-a.akamaihd.net/h-ak-fbx/v/t31.0-1/c379.0.1290.1290/10506738_10150004552801856_220367501106153455_o.jpg?oh=6c57c27cdd7087bbb3fe212a121df59e&oe=5A08697C&__gda__=1506545431_ea3ffe79d57caf91de854335c028bd24',
   NULL,
   'male',
   'lberoshvili9@gmail.com',
   'Hello! I am new Hiker!',
   NULL,
   'https://www.facebook.com/app_scoped_user_id/1530047160352334/');

SELECT
  *
FROM
  hikes;

INSERT INTO location_types VALUES
  (1, 'ZGVAAAA');



INSERT INTO privacy_types VALUES
  (1, 'public'),
  (2, 'private');

INSERT INTO roles VALUES
  (1, 'Creator'),
  (2, 'Member');


INSERT INTO hikes VALUES
  (1, 'KVELAZE MAGARI PAXODE', str_to_date('1989.12.01', '%Y.%m.%d'), str_to_date('1989.12.01', '%Y.%m.%d'), 'MAGARI PAXODI!!! SHEMODIT ALL :***', 5);



INSERT INTO comments VALUES
  (1, 'რა დაგვჭირდება?', NULL, 1, 1, str_to_date('1999.12.01', '%Y.%m.%d'), 1),
  (2, 'ადგილები არის?', NULL, 1, 2, str_to_date('1999.08.01', '%Y.%m.%d'), 1),
  (3, 'წასვლის დროს ვერ გადმოვწევთ?', NULL, 1, 3, str_to_date('1999.09.01', '%Y.%m.%d'), 1),
  (4, 'ანი როგორ ხარ მთავარანგელოზობა ვერ მოგილოცე', NULL, 1, 1, str_to_date('1999.10.01', '%Y.%m.%d'), 1),
  (5, 'უკაცრავად მაინტერესებს პატარა ბავშვის პამპერსები თუ გაქვთ, 3 ზომა შავ პარკში რო ჩამიდოთ, მადლობა', NULL, 1, 4, str_to_date('1999.11.01', '%Y.%m.%d'), 1);


INSERT INTO hike_to_user VALUES
  (1, 1, 1, 1),
  (2, 1, 2, 2),
  (3, 1, 3, 2),
  (4, 1, 4, 2);

INSERT INTO cover_photos VALUES
  (1, 'მაგარი ლოკაცია დზნ', 1, '');



insert into posts values
  (1,"9ზე ოკრიბაშ იყავით ბერლინში მივდივართ", '',  1, 1, now(), null),
  (2,"რუსი ნაშები ჩითავენ? XD XD XD", '', 1, 3, now(), null),
  (3,"ხალვა მომაქ მე", '', 1, 2, now(), NULL );
  
insert into comments
(comment_text, post_ID, hike_ID, user_ID, comment_time, privacy_type)
values
  ( "9ზე ვერ ვასწრებ 10იის ნახზე მანდ ვარ", 1, 1, 2, now(), 2),
  ( "ე ვერ მოვასწრებთ !!! ", 1, 1, 3, now(), 2),
  ( "კაია", 2, 1, 2, now(), 2);




UPDATE hikes
SET
  hike_name = 'მოლაშქრეთა კლუბი აიეტი',
  description = 'ტურის ორგანიზატორია მოლაშქრეთა კლუბი აიეტი, გასვლის და დაბრუნების თარიღი : 15 ივლისი - 16 ივლისი, მთავარი ლოკაციები : თბილისი, ბათმი, თურქეთი, ყაზბეგი'
WHERE
  id = 1;


INSERT INTO hike_to_location (hike_id, location_lat, location_lng, location_type_id) values
  (1, "41.64228","41.63392", 1);

SELECT
  *
FROM
  hike_to_location
WHERE
  hike_id = 1;
-- select ID from posts order by ID desc limit 1;

-- insert into posts (hike_id, post_text, user_id, post_time) values(1,"vache", 1, str_to_date('1989.12.01', '%Y.%m.%d'));

-- select  * from users;
-- select  * from comments;
-- select  * from comment_likes;
-- insert into comments(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)values ( 'TestTestTest', 1, 1, '15/06/17 21:05:37', 2, 14);

-- insert into comments(comment_text, hike_ID, user_ID, comment_time, privacy_type, post_ID)values ( 'TestTestTest', 1, 1, '15/06/17 21:08:23',1, null);

-- insert into posts (post_text, hike_id, user_id, post_time) values("TestTestTest",1, 1, '15/06/17 19:00:04');
-- delete from posts where ID = 36;
-- delete from comments where ID = 100;
-- delete from post_likes where ID = 5;
-- delete from comment_likes where ID = 12;
INSERT INTO REQUESTS (sender_id, receiver_id, hike_id) values
(1, 3, 1),
(2, 3, 1),
(4, 3, 1);
-- --------------------------------------------------------------------- --
DELIMITER $$

CREATE PROCEDURE get_creator_info(hike_ID INT)
  BEGIN
    SELECT users.id as ID, users.first_name as first_name, users.last_name as last_name, profile_picture_url as profile_picture_url,
      facebook_ID, birth_date, gender, email, "" as about_me_text, cover_picture_url, facebook_link
    FROM hike_to_user INNER JOIN users
        ON users.id = user_id
    WHERE hike_to_user.hike_ID = hike_ID AND role_id = 1;

  END$$


DELIMITER $$

CREATE PROCEDURE get_cover_photos(hike_id INT)
  BEGIN
    SELECT cover_photos.id as ID, cover_photos.img_url as img_url, cover_photos.description as description
    FROM cover_photos
    WHERE cover_photos.hike_ID = hike_id;

  END$$

DELIMITER $$

CREATE PROCEDURE get_public_comments(hike_id INT)
  BEGIN
    SELECT comments.ID as ID, comments.comment_text as comment_text, comments.user_ID user_ID, comments.comment_time comment_time
    FROM comments
    WHERE comments.hike_id = hike_id AND comments.privacy_type = 1;

  END$$

DELIMITER $$

create PROCEDURE get_private_comments(post_id INT)
  BEGIN
    SELECT comments.ID ID, comments.comment_text comment_text, comments.user_ID user_ID, comments.comment_time comment_time
    FROM comments
    WHERE comments.post_ID = post_id AND comments.privacy_type = 2;

  END$$

DELIMITER $$

CREATE PROCEDURE get_comment_likes(comment_id INT)
  BEGIN
    SELECT COUNT(comment_ID) as count
    FROM comment_likes
    WHERE comment_likes.comment_id = comment_id;

  END$$

DELIMITER $$

CREATE PROCEDURE get_post_likes(post_id INT)
  BEGIN
    SELECT COUNT(post_ID) as count
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

DELIMITER $$

CREATE PROCEDURE get_joined_people(hike_id INT)
  BEGIN
    SELECT COUNT(user_id) as count
    FROM hike_to_user
    WHERE hike_to_user.hike_id = hike_id;

  END$$

DELIMITER $$

CREATE PROCEDURE get_hikes_by_user(user_id INT)
  BEGIN
    SELECT hikes.id, hikes.hike_name, hikes.start_date,
      hikes.end_date, hikes.description, hikes.max_people FROM hikes
      INNER JOIN hike_to_user ON hike_to_user.user_ID = user_id and hike_to_user.hike_ID = hikes.ID where hike_to_user.role_ID = 1;
  END$$

DELIMITER $$



CREATE PROCEDURE get_hike_members(hike_id INT)
  BEGIN
    SELECT *, (select role_ID roleID from hike_to_user h where h.user_ID = u.ID and h.hike_ID = hike_id) role_ID
    FROM users u where u.ID in (select user_ID from hike_to_user htu where htu.user_ID = u.ID and htu.hike_ID = hike_id);

  END$$

DELIMITER $$
CREATE PROCEDURE request_response(request_id INT, response VARCHAR(20))
  BEGIN
    DECLARE desired_hike INT;
    DECLARE user_ID INT;
    SELECT
      hike_ID
    INTO desired_hike FROM
      requests
    WHERE
      ID = request_id;
    SELECT
      sender_ID
    INTO user_ID FROM
      requests
    WHERE
      ID = request_id;
    DELETE FROM requests
    WHERE
      id = request_id;
    IF(response = 'accept') THEN
      INSERT INTO hike_to_user (hike_ID, user_ID, role_ID) VALUES (desired_hike, user_ID, 2);
    END IF;

  END $$