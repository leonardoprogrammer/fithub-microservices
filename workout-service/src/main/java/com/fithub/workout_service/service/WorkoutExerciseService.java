package com.fithub.workout_service.service;

import com.fithub.workout_service.model.entity.WorkoutExercise;
import com.fithub.workout_service.repository.WorkoutExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkoutExerciseService {

    final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    @Transactional
    public void save(WorkoutExercise workoutExercise) {
        workoutExerciseRepository.save(workoutExercise);
    }

    public WorkoutExercise findById(UUID id) {
        return workoutExerciseRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return workoutExerciseRepository.existsById(id);
    }

    public List<WorkoutExercise> findExercisesByWorkoutId(UUID workoutId) {
        return workoutExerciseRepository.findByWorkoutId(workoutId);
    }

    public void deleteAllList(List<WorkoutExercise> workoutExerciseList) {
        workoutExerciseRepository.deleteAll(workoutExerciseList);
    }

    public void deleteById(UUID id) {
        workoutExerciseRepository.deleteById(id);
    }
}
