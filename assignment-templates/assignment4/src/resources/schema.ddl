create table courses(name varchar(255) NOT NULL, course_num varchar(20) NOT NULL, course_id int NOT NULL AUTO_INCREMENT, PRIMARY KEY(course_id));

create table students(name varchar(255) NOT NULL, student_id int AUTO_INCREMENT, course_id int, PRIMARY KEY(student_id), FOREIGN KEY(course_id) REFERENCES courses(course_id));

insert into courses(name, course_num) values("Data Management", "CS347");

insert into students(name) values("Student 2");

insert into students(name, course_id) values("Student 4", (select course_id from courses where course_num="CS378"));

select * from (students join courses on students.course_id = courses.course_id) where courses.course_num="CS378";

select * from (students join courses on students.course_id = courses.course_id);

select * from (students left outer join courses on students.course_id = courses.course_id);

select * from (students right outer join courses on students.course_id = courses.course_id);