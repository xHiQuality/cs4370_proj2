SELECT 
    E1.emp_no AS E1,
    E1.department AS D1,
    E1.emp_no_colleague AS E3,
    E2.department AS D2,
    E2.emp_no AS E2
FROM (
    SELECT
        D1.emp_no AS emp_no,
        D3.emp_no AS emp_no_colleague,
        D1.dept_no AS department
    FROM 
        dept_emp D1
    JOIN 
        dept_emp D3 
        ON D1.dept_no = D3.dept_no 
        AND D1.emp_no <> D3.emp_no 
        AND D1.from_date <= D3.to_date 
        AND D1.to_date >= D3.from_date
    WHERE 
        D1.emp_no = 13141
) AS E1
JOIN (
    SELECT
        D2.emp_no AS emp_no,
        D3.emp_no AS emp_no_colleague,
        D2.dept_no AS department
    FROM 
        dept_emp D2
    JOIN 
        dept_emp D3 
        ON D2.dept_no = D3.dept_no 
        AND D2.emp_no <> D3.emp_no 
        AND D2.from_date <= D3.to_date 
        AND D2.to_date >= D3.from_date
    WHERE 
        D2.emp_no = 13111
) AS E2
ON 
    E1.emp_no_colleague = E2.emp_no_colleague
WHERE 
    E1.emp_no_colleague <> 13141 
    AND E2.emp_no_colleague <> 13111
    AND E1.department <> E2.department
LIMIT 100;
