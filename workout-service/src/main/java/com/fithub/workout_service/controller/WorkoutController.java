package com.fithub.workout_service.controller;

import com.fithub.workout_service.model.dto.RequestRegisterWorkoutDTO;
import com.fithub.workout_service.model.entity.Workout;
import com.fithub.workout_service.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerWorkout(@RequestBody RequestRegisterWorkoutDTO requestRegisterWorkoutDTO) {
        // VALIDATIONS

        // CHECKS

        // PROCESS

        Workout workout = new Workout();
        Workout newWorkout = workoutService.save(workout);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newWorkout.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
