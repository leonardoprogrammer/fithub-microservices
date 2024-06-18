package com.fithub.workout_service.model.record;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public record UpdateWorkoutRequest(String title, Date workoutDate, Timestamp startDateTime,
                                   Timestamp endDateTime, Time timedTime, String observation) {
}
