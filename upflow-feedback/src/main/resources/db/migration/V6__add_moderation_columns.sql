ALTER TABLE tbl_feedback
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    ADD COLUMN moderated_content TEXT;

UPDATE tbl_feedback SET status = 'PENDING' WHERE status IS NULL;
