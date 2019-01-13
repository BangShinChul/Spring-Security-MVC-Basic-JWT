## DB 구조도


```
# table 생성 쿼리

create table role (
  r_id int(11) not null auto_increment primary key,
  r_name varchar(10) not null unique
);

insert into role (r_name) values (
    'ADMIN'
);

insert into role (r_name) values (
    'USER'
);

create table user_info (
  u_id int(11) not null auto_increment primary key,
  user_id varchar(20) not null unique,
  user_password varchar(300) not null, 
  user_email varchar(40) not null,
  user_name varchar(30) not null,
  user_create_date datetime not null default current_timestamp,
  user_active int(10) not null default 1 comment '0:비활성화, 1:활성화',
  user_role varchar(10) not null default 'USER',
  CONSTRAINT `fk_user_role`
  		FOREIGN KEY (user_role) REFERENCES role (r_name)
  		ON DELETE RESTRICT
  		ON UPDATE CASCADE
);

create table board (
  b_id int(11) not null auto_increment primary key,
  board_cat varchar(25) not null,
  board_sub_cat varchar(25) default null,
  board_name varchar(30) not null
);

create table board_item (
  bi_id int(11) not null auto_increment primary key,
  b_id int(11) not null comment 'board b_id',
  title varchar(45) not null,
  content text not null,
  writter varchar(20) not null,
  like_cnt int (10) not null default 0,
  create_date datetime not null default current_timestamp,
  edit_date datetime default null,
  CONSTRAINT `fk_board_and_board_item` 
        FOREIGN KEY (b_id) REFERENCES board (b_id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
  CONSTRAINT `fk_user_and_board_item` 
        FOREIGN KEY (writter) REFERENCES user_info(user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE  		
);

```


<br> 

```
> use bangshinchul # database 선택

> desc userinfo; # userinfo 테이블 구조 확인

+---------------+-------------+------+-----+---------+-------+
| Field         | Type        | Null | Key | Default | Extra |
+---------------+-------------+------+-----+---------+-------+
| user_id       | varchar(16) | NO   | PRI | NULL    |       |
| user_password | varchar(32) | NO   |     | NULL    |       |
| user_email    | varchar(32) | NO   |     | NULL    |       |
| user_name     | varchar(15) | NO   |     | NULL    |       |
+---------------+-------------+------+-----+---------+-------+



```