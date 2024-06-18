package com.fithub.workout_service.model.record;

import java.util.UUID;

public record RegisterExerciseRequest(UUID workoutId, String name) {
}
