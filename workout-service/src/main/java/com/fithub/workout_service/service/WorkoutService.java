package com.fithub.workout_service.service;

import com.fithub.workout_service.model.entity.Workout;
import com.fithub.workout_service.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class WorkoutService {

    final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    public void save(Workout workout) {
        workoutRepository.save(workout);
    }

    public Workout findById(UUID id) {
        return workoutRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return workoutRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        workoutRepository.deleteById(id);
    }
}
