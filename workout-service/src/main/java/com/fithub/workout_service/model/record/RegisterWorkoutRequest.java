package com.fithub.workout_service.model.record;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public record RegisterWorkoutRequest(UUID userId, String title, Date workoutDate, Timestamp startDateTime,
                                     Timestamp endDateTime, Time timedTime, String observation) {
}
