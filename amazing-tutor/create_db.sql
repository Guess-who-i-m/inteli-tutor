CREATE DATABASE tutor;
USE tutor;
# DROP DATABASE tutor;

CREATE TABLE student(
                        stu_id      INT AUTO_INCREMENT PRIMARY KEY,
                        username    VARCHAR(50) UNIQUE ,
                        password    VARCHAR(50)
) ENGINE=InnoDB;

CREATE TABLE stu_info(
                         info_id     INT AUTO_INCREMENT PRIMARY KEY,
                         gender      ENUM('M','F'),
                         grade       INT,
                         addr        VARCHAR(100),
                         phone_num   VARCHAR(20),
                         avatar      VARCHAR(100),
                         stu_id      INT,
                         FOREIGN KEY (stu_id) REFERENCES student(stu_id)
) ENGINE=InnoDB;

CREATE TABLE recruit(
                        recruit_id  INT AUTO_INCREMENT PRIMARY KEY ,
                        price       INT,
                        sch_level   ENUM('jbw','eyy','syl','yb','eb'),
                        stu_level   ENUM('A','B','C','D','E'),
                        subject     VARCHAR(20),
                        online      BOOL,
                        detail      VARCHAR(200),
                        stu_id      INT,
                        FOREIGN KEY (stu_id) REFERENCES student(stu_id)
) ENGINE=InnoDB;

CREATE TABLE recruit_date(
                             date_id     INT AUTO_INCREMENT PRIMARY KEY ,
                             day         ENUM('mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun'),
                             start_time  TIME,
                             end_time    TIME,
                             recruit_id  INT,
                             FOREIGN KEY (recruit_id) REFERENCES recruit(recruit_id)
) ENGINE=InnoDB;

CREATE TABLE teacher(
                        tch_id      INT AUTO_INCREMENT  PRIMARY KEY ,
                        username    VARCHAR(50) UNIQUE ,
                        password    VARCHAR(50)
) ENGINE=InnoDB;

CREATE TABLE tch_info(
                         info_id     INT AUTO_INCREMENT  PRIMARY KEY ,
                         gender      ENUM('M','F'),
                         school      VARCHAR(20),
                         sch_level   ENUM('jbw','eyy','syl','yb','eb'),
                         edu_bg      ENUM('bachelor', 'master', 'doctor'),
                         phone_num   VARCHAR(20),
                         avatar      VARCHAR(100),
                         tch_id      INT,
                         FOREIGN KEY (tch_id) REFERENCES teacher(tch_id)
) ENGINE=InnoDB;

CREATE TABLE recommend(
                          recommend_id    INT AUTO_INCREMENT PRIMARY KEY ,
                          price           INT,
                          subject         VARCHAR(20),
                          online          BOOL,
                          detail          VARCHAR(300),
                          tch_id          INT,
                          FOREIGN KEY (tch_id) REFERENCES teacher(tch_id)
) ENGINE=InnoDB;

CREATE TABLE recommend_date(
                               date_id         INT AUTO_INCREMENT PRIMARY KEY ,
                               day             ENUM('mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun'),
                               start_time      TIME,
                               end_time        TIME,
                               recommend_id    INT,
                               FOREIGN KEY (recommend_id) REFERENCES recommend(recommend_id)
) ENGINE=InnoDB;

CREATE TABLE comment(
                        comment_id      INT AUTO_INCREMENT PRIMARY KEY ,
                        stu_id          INT,
                        tch_id          INT,
                        score           INT,
                        content         VARCHAR(300),
                        FOREIGN KEY (stu_id) REFERENCES student(stu_id),
                        FOREIGN KEY (tch_id) REFERENCES teacher(tch_id)
) ENGINE=InnoDB;