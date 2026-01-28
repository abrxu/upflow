CREATE TABLE tbl_feedback (
    id UUID PRIMARY KEY,
    message TEXT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    department_id UUID NOT NULL REFERENCES tbl_department(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);