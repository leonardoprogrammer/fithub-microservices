package com.fithub.workout_service.repository;

import com.fithub.workout_service.model.entity.WorkoutExerciseSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseSeriesRepository extends JpaRepository<WorkoutExerciseSeries, UUID> {

    List<WorkoutExerciseSeries> findByWorkoutExerciseId(UUID workoutExerciseId);
}
