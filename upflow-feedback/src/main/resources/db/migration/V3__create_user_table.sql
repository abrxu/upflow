CREATE TABLE tbl_user (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role_id UUID NOT NULL REFERENCES tbl_role(id),
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE tbl_user_department (
    user_id UUID NOT NULL REFERENCES tbl_user(id),
    department_id UUID NOT NULL REFERENCES tbl_department(id),
    PRIMARY KEY (user_id, department_id)
);