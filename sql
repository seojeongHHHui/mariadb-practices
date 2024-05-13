select version(), current_date, now() from dual;

-- 수학함수, 사칙연산
select sin(pi()/4), 1+2*3-4/5 from dual;

-- 대소문자 구분 x
sELecT VERSION(), current_DATE, NOW() From DUAL;

-- table 생성: DDL
create table pet(
	name varchar(100),
    owner varchar(50),
    species varchar(20),
    gender char(1),
    birth date,
    death date
);

-- schema 확인
describe pet;
desc pet;

-- tabel 삭제
drop table pet;
show tables;

-- insert: DML(C:create)
insert into pet values('fluffy', '서정희', 'dog', 'm', '2020-02-04', null);

-- select: DML(R:review)
select * from pet;

-- update: DML(U)
update pet set name='fluffyyy' where name='fluffy';

-- delete: DML(D)
delete from pet where name='fluffyyy';

-- load data: mysql(CLI) 전용
load data local infile '/root/pet.txt' into table pet;

-- select 연습
select name, species
  from pet
where name='bowser';

select name, species, gender
  from pet
where species = 'dog'
  and gender = 'f';
  
  select name, species
  from pet
where species = 'bird'
  or species = 'snake';

select name, birth
  from pet
order by birth asc;

select name, birth
  from pet
order by birth desc;

select name, birth, death
  from pet
where death is not null;

select name
  from pet
where name like 'b%';

select name
  from pet
where name like '%fy';

select name
  from pet
where name like '%w%';

select name
  from pet
where name like '_____';

select name
  from pet
where name like 'b____';

select count(*)
  from pet;
  
  