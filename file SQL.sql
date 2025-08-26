create database ASM2;
Use ASM2;
CREATE USER user1@localhost IDENTIFIED BY '1';
GRANT ALL PRIVILEGES ON *.* TO user1@localhost WITH GRANT OPTION;

create table user (
id int auto_increment not null primary KEY,
name varchar(255) not null,
address varchar(255) not null,
email varchar(255) not null unique,
phonenumber varchar(255) not null,
password varchar(255) not null,
enable tinyint(1) not null,
image varchar(255) ,
description TEXT ,
company_id int ,
role_id int not null,
cv_id int,
foreign key (company_id) references company (id),
foreign key (role_id) references role(id),
foreign key (cv_id) references cv(id)
);


create table company (
id int not null auto_increment primary KEY,
name varchar (255) not null,
address varchar(255) not null,
email varchar(255) not null,
phonenumber varchar(255) not null,
logo varchar(255),
description text 
);
ALTER TABLE applications add column is_approved tinyint not null;



create table role (
id int not null auto_increment primary key,
name varchar(255) not null
);

create table cv (
id int not null auto_increment primary key,
file_name varchar(255) not null
);

CREATE TABLE posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,        -- Tiêu đề bài đăng
    description TEXT NOT NULL,          -- Mô tả công việc
    experience VARCHAR(100),            -- Kinh nghiệm yêu cầu
    vacancies INT NOT NULL,             -- Số người cần tuyển
    address VARCHAR(255),               -- Địa chỉ làm việc
    deadline DATE NOT NULL,             -- Hạn ứng tuyển
    salary VARCHAR(100),                -- Lương (có thể để dạng text như "15M-20M")
    job_type varchar(255), -- Loại công việc
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    company_id int not null
);


CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL  -- VD: "NodeJS", "Java", "PHP"
);


CREATE TABLE post_categories (
    post_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (post_id, category_id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);


create table applications (
post_id int not null,
user_id int Not null,
is_approved tinyint(1) not null,
primary key (post_id,user_id),
foreign key (post_id) references posts(id),
foreign key (user_id) references user(id)
);

CREATE TABLE followed_post (
  user_id INT NOT NULL,
  post_id INT NOT NULL,
  PRIMARY KEY (user_id, post_id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);
CREATE TABLE followed_company (
  user_id INT NOT NULL,
  company_id INT NOT NULL,
  PRIMARY KEY (user_id, company_id),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (company_id) REFERENCES company(id)
);

-- -------------------------
