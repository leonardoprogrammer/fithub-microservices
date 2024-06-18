package com.fithub.workout_service.service;

import com.fithub.workout_service.model.entity.WorkoutExerciseSeries;
import com.fithub.workout_service.repository.WorkoutExerciseSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkoutExerciseSeriesService {

    final WorkoutExerciseSeriesRepository workoutExerciseSeriesRepository;

    @Autowired
    public WorkoutExerciseSeriesService(WorkoutExerciseSeriesRepository workoutExerciseSeriesRepository) {
        this.workoutExerciseSeriesRepository = workoutExerciseSeriesRepository;
    }

    @Transactional
    public void save(WorkoutExerciseSeries workoutExerciseSeries) {
        workoutExerciseSeriesRepository.save(workoutExerciseSeries);
    }

    public WorkoutExerciseSeries findById(UUID id) {
        return workoutExerciseSeriesRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return workoutExerciseSeriesRepository.existsById(id);
    }

    public List<WorkoutExerciseSeries> findSeriesByWorkoutExerciseId(UUID workoutExerciseId) {
        return workoutExerciseSeriesRepository.findByWorkoutExerciseId(workoutExerciseId);
    }

    public void deleteAllList(List<WorkoutExerciseSeries> exerciseSeriesList) {
        workoutExerciseSeriesRepository.deleteAll(exerciseSeriesList);
    }

    public void deleteById(UUID id) {
        workoutExerciseSeriesRepository.deleteById(id);
    }
}
