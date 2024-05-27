CREATE TABLE workout (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    title VARCHAR(255),
    workout_date DATE,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    timed_time TIME,
    observation TEXT,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_alt TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
