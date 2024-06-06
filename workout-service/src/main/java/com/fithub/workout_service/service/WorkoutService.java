package com.fithub.workout_service.service;

import com.fithub.workout_service.model.entity.Workout;
import com.fithub.workout_service.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class WorkoutService {

    final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    public Workout save(Workout workout) {
        return workoutRepository.save(workout);
    }

    @Transactional
    public void update(Workout workout) {
        workoutRepository.save(workout);
    }

    public Optional<Workout> findById(UUID id) {
        return workoutRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return workoutRepository.existsById(id);
    }
}
