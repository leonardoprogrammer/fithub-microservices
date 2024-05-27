CREATE TABLE workout_exercise_series (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workout_exercise_id UUID NOT NULL,
    quantity INT NOT NULL,
    repetitions INT NOT NULL,
    weight_kg DECIMAL(10, 2) NOT NULL,
    date_inc TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_alt TIMESTAMP,
    FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercise(id)
);
