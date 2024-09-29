select
	e.emp_no,
    e.birth_date,
    e.first_name,
    e.last_name,
    e.gender,
    e.hire_date,
    max(((case when m.to_date= '9999-01-01' THEN current_date() ELSE m.to_date END) - m.from_date)) as "max_manager_tenure"
from
	employees.employees e
		inner join employees.dept_manager m on e.emp_no = m.emp_no
group by
	e.emp_no
order by
	max_manager_tenure desc
