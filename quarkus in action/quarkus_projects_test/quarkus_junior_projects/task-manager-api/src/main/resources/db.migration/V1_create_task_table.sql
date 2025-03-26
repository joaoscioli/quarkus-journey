CREATE TABLE task (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      completed BOOLEAN DEFAULT FALSE
)