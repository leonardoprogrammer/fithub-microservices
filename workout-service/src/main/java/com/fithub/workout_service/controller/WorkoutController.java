package com.fithub.workout_service.controller;

import com.fithub.workout_service.model.entity.Workout;
import com.fithub.workout_service.model.entity.WorkoutExercise;
import com.fithub.workout_service.model.entity.WorkoutExerciseSeries;
import com.fithub.workout_service.model.record.*;
import com.fithub.workout_service.service.UserService;
import com.fithub.workout_service.service.WorkoutExerciseSeriesService;
import com.fithub.workout_service.service.WorkoutExerciseService;
import com.fithub.workout_service.service.WorkoutService;
import com.fithub.workout_service.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutExerciseSeriesService workoutExerciseSeriesService;
    private final UserService userService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, WorkoutExerciseService workoutExerciseService, WorkoutExerciseSeriesService workoutExerciseSeriesService, UserService userService) {
        this.workoutService = workoutService;
        this.workoutExerciseService = workoutExerciseService;
        this.workoutExerciseSeriesService = workoutExerciseSeriesService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Object> registerWorkout(@RequestBody RegisterWorkoutRequest registerWorkoutRequest) {
        // VALIDATIONS
        if (registerWorkoutRequest.userId() == null) {
            return ResponseEntity.badRequest().body("Informe o ID do usuário.");
        }

        if (Utils.isNullOrEmpty(registerWorkoutRequest.title())) {
            return ResponseEntity.badRequest().body("Informe um título para o treino.");
        }

        if (registerWorkoutRequest.workoutDate() == null && registerWorkoutRequest.startDateTime() == null) {
            return ResponseEntity.badRequest().body("Informe a data do treino ou a data de início.");
        }

        if (registerWorkoutRequest.workoutDate() != null && registerWorkoutRequest.workoutDate().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida para o treino.");
        }

        if (registerWorkoutRequest.startDateTime() != null) {
            if (registerWorkoutRequest.startDateTime().after(new Date())) {
                return ResponseEntity.badRequest().body("Informe uma data válida para o início do treino.");
            }

            if (registerWorkoutRequest.endDateTime() != null) {
                if (registerWorkoutRequest.endDateTime().after(new Date())) {
                    return ResponseEntity.badRequest().body("Informe uma data válida para o término de treino.");
                }

                if (registerWorkoutRequest.endDateTime().before(registerWorkoutRequest.startDateTime())) {
                    return ResponseEntity.badRequest().body("A data de início do treino deve ser antes da data de término.");
                }
            }
        }

        // CHECKS
        if (!userService.existsById(registerWorkoutRequest.userId())) {
            return new ResponseEntity<>("Usuário não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        Workout workout = new Workout();
        BeanUtils.copyProperties(registerWorkoutRequest, workout);

        workoutService.save(workout);

        return new ResponseEntity<>("Treino registrado com sucesso.", HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateWorkout(@PathVariable UUID id, @RequestBody UpdateWorkoutRequest updateWorkoutRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID do treino.");
        }

        if (Utils.isNullOrEmpty(updateWorkoutRequest.title())) {
            return ResponseEntity.badRequest().body("O título do treino não pode ficar em branco.");
        }

        if (updateWorkoutRequest.workoutDate() == null && updateWorkoutRequest.startDateTime() == null) {
            return ResponseEntity.badRequest().body("Informe a data do treino ou a data de início.");
        }

        if (updateWorkoutRequest.workoutDate() != null && updateWorkoutRequest.workoutDate().after(new Date())) {
            return ResponseEntity.badRequest().body("Informe uma data válida para o treino.");
        }

        if (updateWorkoutRequest.startDateTime() != null) {
            if (updateWorkoutRequest.startDateTime().after(new Date())) {
                return ResponseEntity.badRequest().body("Informe uma data válida para o início do treino.");
            }

            if (updateWorkoutRequest.endDateTime() != null) {
                if (updateWorkoutRequest.endDateTime().after(new Date())) {
                    return ResponseEntity.badRequest().body("Informe uma data válida para o término do treino.");
                }

                if (updateWorkoutRequest.endDateTime().before(updateWorkoutRequest.startDateTime())) {
                    return ResponseEntity.badRequest().body("A data de início do treino deve ser antes da data de término.");
                }
            }
        }

        Workout workout = workoutService.findById(id);

        // CHECKS
        if (workout == null) {
            return new ResponseEntity<>("Treino não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        BeanUtils.copyProperties(updateWorkoutRequest, workout);
        workout.setDateAltDefault();

        workoutService.save(workout);

        return ResponseEntity.ok("Treino atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getWorkoutById(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        Workout workout = workoutService.findById(id);

        // CHECKS
        if (workout == null) {
            return new ResponseEntity<>("Treino não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(workout);
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<Object> getFullWorkoutById(@PathVariable UUID id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        Workout workout = workoutService.findById(id);

        if (workout == null) {
            return new ResponseEntity<>("Treino não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        List<WorkoutExercise> workoutExercises = workoutExerciseService.findExercisesByWorkoutId(workout.getId());

        if (workoutExercises != null && !workoutExercises.isEmpty()) {
            for (WorkoutExercise workoutExercise : workoutExercises) {
                workoutExercise.setSeries(workoutExerciseSeriesService.findSeriesByWorkoutExerciseId(workoutExercise.getId()));
            }
        }

        workout.setExercises(workoutExercises);

        return ResponseEntity.ok(workout);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteWorkoutById(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        // CHECKS
        if (!workoutService.existsById(id)) {
            return new ResponseEntity<>("Treino não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        List<WorkoutExercise> workoutExerciseList = workoutExerciseService.findExercisesByWorkoutId(id);

        if (workoutExerciseList != null && !workoutExerciseList.isEmpty()) {
            for (WorkoutExercise workoutExercise : workoutExerciseList) {
                List<WorkoutExerciseSeries> workoutExerciseSeries = workoutExerciseSeriesService.findSeriesByWorkoutExerciseId(workoutExercise.getId());
                workoutExerciseSeriesService.deleteAllList(workoutExerciseSeries);
            }
        }
        workoutExerciseService.deleteAllList(workoutExerciseList);
        workoutService.deleteById(id);

        return ResponseEntity.ok("Treino deletado com sucesso.");
    }
}
