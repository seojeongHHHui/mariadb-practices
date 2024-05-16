-- 서브쿼리(SUBQUERY) SQL 문제입니다.

-- 문제1.
-- 현재 평균 연봉보다 많은 월급을 받는 직원은 몇 명이나 있습니까?
select count(*)
from salaries
where to_date='9999-01-01' and salary>(select avg(salary) from salaries where to_date='9999-01-01');

-- 문제2.
-- 현재, 각 부서별로 최고의 급여를 받는 사원의 사번, 이름, 부서, 연봉을 조회하세요.
-- 단 조회결과는 연봉의 내림차순으로 정렬되어 나타나야 합니다.
select a.emp_no, a.last_name, d.dept_name, b.salary
from employees a, salaries b, dept_emp c, departments d
where a.emp_no=b.emp_no and b.to_date='9999-01-01'
  and a.emp_no=c.emp_no and c.to_date='9999-01-01'
  and b.emp_no=c.emp_no and c.dept_no=d.dept_no
  and (c.dept_no, b.salary) in ( select a.dept_no, max(b.salary)
								 from dept_emp a, salaries b
								 where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
								 group by a.dept_no )
order by b.salary desc;

-- 문제3.
-- 현재, 자신의 부서 평균 급여보다 연봉(salary)이 많은 사원의 사번, 이름과 연봉을 조회하세요
select a.emp_no, a.last_name, b.salary
from employees a, salaries b, dept_emp c,
	 (select a.dept_no as result1, avg(b.salary) as result2
	  from dept_emp a, salaries b
	  where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
	  group by a.dept_no) d
where a.emp_no=b.emp_no and b.to_date='9999-01-01'
  and a.emp_no=c.emp_no and c.to_date='9999-01-01'
  and b.emp_no=c.emp_no and c.dept_no=d.result1
  and b.salary>d.result2;

-- 문제4.
-- 현재, 사원들의 사번, 이름, 매니저 이름, 부서 이름으로 출력해 보세요.
select a.emp_no, a.last_name, d.result1 as 'manager', c.dept_name
from employees a, dept_emp b, departments c,
     (select a.last_name as result1, b.dept_no as result2
      from employees a, dept_manager b
      where a.emp_no=b.emp_no and b.to_date='9999-01-01') d
where a.emp_no=b.emp_no and b.dept_no=c.dept_no and b.to_date='9999-01-01'
  and b.dept_no=d.result2 and c.dept_no=d.result2;

-- 문제5.
-- 현재, 평균연봉이 가장 높은 부서의 사원들의 사번, 이름, 직책, 연봉을 조회하고 연봉 순으로 출력하세요.
select a.emp_no, a.last_name, b.title, c.salary
from employees a, titles b, salaries c, dept_emp d
where a.emp_no=b.emp_no and a.emp_no=c.emp_no and b.emp_no=c.emp_no
  and b.to_date='9999-01-01' and c.to_date='9999-01-01'
  and a.emp_no=d.emp_no and b.emp_no=d.emp_no and c.emp_no=d.emp_no and d.to_date='9999-01-01'
  and d.dept_no=(
	select a.dept_no
	from dept_emp a, salaries b
	where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
	group by a.dept_no
	having avg(b.salary)=(select max(a.result)
						  from (select a.dept_no, avg(b.salary) as 'result'
								from dept_emp a, salaries b
								where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
								group by a.dept_no) a)
)
order by c.salary;

-- 문제6.
-- 평균 연봉이 가장 높은 부서와 평균연봉은? (dept_no:d007, avg(salary):88852.9695, Sales)
select a.dept_name, avg(b.salary)
from departments a, salaries b, dept_emp c
where a.dept_no=(
	select a.dept_no
	from dept_emp a, salaries b
	where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
	group by a.dept_no
	having avg(b.salary)=(select max(a.result)
						  from (select a.dept_no, avg(b.salary) as 'result'
								from dept_emp a, salaries b
								where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
								group by a.dept_no) a)
) and b.to_date='9999-01-01' and a.dept_no=c.dept_no and b.emp_no=c.emp_no and c.to_date='9999-01-01'
group by a.dept_no;

-- 문제7.
-- 평균 연봉이 가장 높은 직책과 연봉?
-- 개발자 20000
select a.title, avg(b.salary)
from titles a, salaries b
where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
group by a.title
having avg(b.salary)=( select max(a.result)
					   from ( select a.title, avg(b.salary) as 'result'
							  from titles a, salaries b
							  where a.emp_no=b.emp_no and a.to_date='9999-01-01' and b.to_date='9999-01-01'
							  group by a.title) a);

-- 문제8.
-- 현재 자신의 매니저보다 높은 연봉을 받고 있는 직원은?
-- 부서이름, 사원이름, 연봉, 매니저 이름, 메니저 연봉 순으로 출력합니다.
select c.dept_name, a.last_name, s.salary, m.manager_name, m.manager_salary
from employees a, dept_emp b, departments c, salaries s,
	 (select a.dept_no as 'manager_dept', c.last_name as 'manager_name', b.salary as 'manager_salary'
	  from dept_manager a, salaries b, employees c
	  where a.emp_no=b.emp_no and a.emp_no=c.emp_no and b.emp_no=c.emp_no
		and a.to_date='9999-01-01' and b.to_date='9999-01-01') m
where a.emp_no=b.emp_no and a.emp_no=s.emp_no and b.dept_no=c.dept_no
  and b.to_date='9999-01-01' and s.to_date='9999-01-01'
  and b.dept_no=m.manager_dept and c.dept_no=m.manager_dept
  and s.salary>m.manager_salary;
