CREATE TABLE task (
    id uuid NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    priority INT,
    completed BOOLEAN,
    created_at DATE,
    updated_at DATE,
    CONSTRAINT task_primary_key PRIMARY KEY (id)
);






