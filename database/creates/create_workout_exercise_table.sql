CREATE TABLE workout_exercise (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_alt TIMESTAMP,
    FOREIGN KEY (workout_id) REFERENCES workout(id)
);
