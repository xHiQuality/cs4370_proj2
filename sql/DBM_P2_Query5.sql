SELECT 
    E1.emp_no AS emp_no_1, 
    D1.dept_no AS department, 
    E2.emp_no AS emp_no_2
FROM 
    dept_emp D1
JOIN 
    dept_emp D2 
    ON D1.dept_no = D2.dept_no
    AND D1.emp_no <> D2.emp_no 
    AND D1.from_date <= D2.to_date
    AND D1.to_date >= D2.from_date
JOIN 
    employees E1 ON D1.emp_no = E1.emp_no
JOIN 
    employees E2 ON D2.emp_no = E2.emp_no
WHERE 
    E1.emp_no = 10009
    AND E2.emp_no = 11545;
