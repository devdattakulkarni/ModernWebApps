Installation of MySQL Server:

MySQL Community Server
http://dev.mysql.com/downloads/

On Mac, download the .dmg file

Starting MySQL Server (on Mac)
- In Spotlight search for MySQL
- MySQL window should open up with “Start MySQL Server” button on it
- MySQL Server Instance should be in “stopped” status
- Hit the “Start MySQL Server” button; you may be prompted for a password
- Enter the password; MySQL server should be up

MySQL Clients
- Command line
  - /usr/local/mysql/bin/mysql – if you install the .dmg from above link
- MySQL Workbench
  - SQuirrel SQL
  - http://squirrel-sql.sourceforge.net/

Local MySQL Server
127.0.0.1
Port number 3306
Usename: root

Setting up database:

1) Run mysql client
   - /usr/local/mysql/bin/mysql -u root

2) Create Database
   - create database if not exists student_courses;

3) Create User
   - Example: create user 'devdatta3'@'localhost’ identified by 'devdatta3';

4) Grant Privileges
   GRANT ALL ON student_courses.* TO ‘devdatta3’@’localhost’;

5) Logout 
   - On mysql prompt: “quit”

6) Login as user;
   - /usr/local/mysql/bin/mysql –h localhost --user=devdatta3 --password=devdatta3

7) Create Table
   - show databases;
   - use student_courses;
   - show tables;
   - create table courses(name varchar(255) NOT NULL, course_num varchar(20) NOT NULL, course_id int NOT NULL AUTO_INCREMENT, PRIMARY KEY(course_id));
