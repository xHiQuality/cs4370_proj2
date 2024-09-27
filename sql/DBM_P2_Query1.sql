select
	dept_no,
    dept_name,
    (avg_female_salary / avg_male_salary) as "salary_ratio_female_to_male"
from
	(
		select
        d.dept_no,
        d.dept_name,
        (
			select avg(salary)
			from employees.salaries s 
				inner join employees.employees e on e.emp_no = s.emp_no
				inner join employees.dept_emp de on de.emp_no = e.emp_no and de.dept_no = d.dept_no
			where e.gender = 'F'
			group by de.dept_no
		) AS "avg_female_salary",
		(
			select avg(salary)
			from employees.salaries s
				inner join employees.employees e on e.emp_no = s.emp_no
				inner join employees.dept_emp de on de.emp_no = e.emp_no and de.dept_no = d.dept_no
			where e.gender = 'M'
			group by de.dept_no
		) AS "avg_male_salary"
	from
		employees.departments d
)t1
	order by
		salary_ratio_female_to_male desc
	limit 1