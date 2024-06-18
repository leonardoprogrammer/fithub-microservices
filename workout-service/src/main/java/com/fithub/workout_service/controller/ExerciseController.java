package com.fithub.workout_service.controller;

import com.fithub.workout_service.model.entity.WorkoutExercise;
import com.fithub.workout_service.model.record.RegisterExerciseRequest;
import com.fithub.workout_service.model.record.UpdateExerciseRequest;
import com.fithub.workout_service.service.WorkoutExerciseService;
import com.fithub.workout_service.service.WorkoutService;
import com.fithub.workout_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercise")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExerciseController {

    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutService workoutService;

    @Autowired
    public ExerciseController(WorkoutExerciseService workoutExerciseService, WorkoutService workoutService) {
        this.workoutExerciseService = workoutExerciseService;
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<Object> registerExercise(@RequestBody RegisterExerciseRequest registerExerciseRequest) {
        // VALIDATIONS
        if (registerExerciseRequest.workoutId() == null) {
            return ResponseEntity.badRequest().body("Informe o ID do treino.");
        }

        if (Utils.isNullOrEmpty(registerExerciseRequest.name())) {
            return ResponseEntity.badRequest().body("Informe o nome do exercício.");
        }

        // CHECKS
        if (!workoutService.existsById(registerExerciseRequest.workoutId())) {
            return new ResponseEntity<>("Treino não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        WorkoutExercise workoutExercise = new WorkoutExercise(
                registerExerciseRequest.workoutId(),
                registerExerciseRequest.name()
        );

        workoutExerciseService.save(workoutExercise);

        return ResponseEntity.ok("Exercício registrado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateExercise(@PathVariable UUID id, UpdateExerciseRequest updateExerciseRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        if (Utils.isNullOrEmpty(updateExerciseRequest.name())) {
            return ResponseEntity.badRequest().body("O nome do exercício não pode ficar em branco.");
        }

        WorkoutExercise workoutExercise = workoutExerciseService.findById(id);

        // CHECKS
        if (workoutExercise == null) {
            return new ResponseEntity<>("Exercício não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        workoutExercise.setName(updateExerciseRequest.name());
        workoutExercise.setDateAltDefault();
        workoutExerciseService.save(workoutExercise);

        return ResponseEntity.ok("Exercício atualizado com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExerciseById(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        WorkoutExercise workoutExercise = workoutExerciseService.findById(id);

        // CHECKS
        if (workoutExercise == null) {
            return new ResponseEntity<>("Exercício não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(workoutExercise);
    }

    @GetMapping
    public ResponseEntity<Object> getExercisesByWorkoutId(@RequestParam(name = "workoutId") UUID workoutId) {
        // VALIDATIONS
        if (workoutId == null) {
            return ResponseEntity.badRequest().body("Informe o ID do treino.");
        }

        List<WorkoutExercise> exercises = workoutExerciseService.findExercisesByWorkoutId(workoutId);

        if (exercises == null || exercises.isEmpty()) {
            return new ResponseEntity<>("Nenhum exercício encontrado com este ID de treino.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(exercises);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExercise(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        // CHECKS
        if (!workoutExerciseService.existsById(id)) {
            return new ResponseEntity<>("Exercício não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        workoutExerciseService.deleteById(id);

        return ResponseEntity.ok("Exercício deletado com sucesso.");
    }
}
