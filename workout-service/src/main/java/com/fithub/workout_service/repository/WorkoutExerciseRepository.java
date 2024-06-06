package com.fithub.workout_service.repository;

import com.fithub.workout_service.model.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {
}
