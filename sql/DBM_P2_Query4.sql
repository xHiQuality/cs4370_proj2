SELECT DISTINCT first_name, last_name, gender, birth_date, salary, title, T.from_date, T.to_date  
FROM DEPT_MANAGER DM JOIN TITLES T
ON DM.emp_no = T.emp_no and T.title LIKE "%Manager%" and T.to_date LIKE "9999%"
JOIN EMPLOYEES E
ON DM.emp_no = E.emp_no and E.gender = "F" and E.birth_date < "1990-01-01"
JOIN SALARIES S
ON DM.emp_no = S.emp_no and S.to_date = T.to_date and S.salary < 80000;
