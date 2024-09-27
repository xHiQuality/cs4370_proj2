SELECT DISTINCT DE.dept_no, dept_name, AVG(salary),
COUNT(CASE WHEN birth_date < "1950-01-01" and birth_date > "1939-12-31" then 1 END) as "Born in 40s", 
COUNT(CASE WHEN birth_date < "1960-01-01" and birth_date > "1949-12-31" then 1 END) as "Born in 50s",
COUNT(CASE WHEN birth_date < "1970-01-01" and birth_date > "1959-12-31" then 1 END) as "Born in 60s",
COUNT(CASE WHEN birth_date < "1980-01-01" and birth_date > "1969-12-31" then 1 END) as "Born in 70s"       
FROM  DEPT_EMP DE JOIN SALARIES S
ON DE.emp_no = S.emp_no
JOIN EMPLOYEES E
ON DE.emp_no = E.emp_no
JOIN DEPARTMENTS D
ON DE.dept_no = D.dept_no
GROUP BY DE.dept_no
ORDER BY AVG(salary) DESC
