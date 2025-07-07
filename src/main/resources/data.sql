-- Insert Users
INSERT INTO "user" (email, name, created_at, updated_at)
VALUES 
    ('john.doe@example.com', 'John Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('jane.smith@example.com', 'Jane Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('bob.wilson@example.com', 'Bob Wilson', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Projects
INSERT INTO project (name, description, created_at, updated_at)
VALUES 
    ('Website Redesign', 'Modernize the company website', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Mobile App', 'Develop new mobile application', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Database Migration', 'Migrate legacy database to new system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Tasks
INSERT INTO task (title, description, status, project_id, assignee_id, created_at, updated_at)
VALUES 
    ('Design Homepage', 'Create new homepage design', 'IN_PROGRESS', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Implement User Authentication', 'Add login and registration', 'TODO', 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Create Mobile Wireframes', 'Design initial app wireframes', 'DONE', 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Develop API Endpoints', 'Create REST API for mobile app', 'IN_PROGRESS', 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Schema Planning', 'Plan new database schema', 'TODO', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Data Migration Script', 'Write script for data transfer', 'TODO', 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 