package com.fithub.workout_service.controller;

import com.fithub.workout_service.model.entity.WorkoutExerciseSeries;
import com.fithub.workout_service.model.record.RegisterSeriesRequest;
import com.fithub.workout_service.model.record.UpdateSeriesRequest;
import com.fithub.workout_service.service.WorkoutExerciseSeriesService;
import com.fithub.workout_service.service.WorkoutExerciseService;
import com.fithub.workout_service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SeriesController {

    private final WorkoutExerciseSeriesService workoutExerciseSeriesService;
    private final WorkoutExerciseService workoutExerciseService;

    @Autowired
    public SeriesController(WorkoutExerciseSeriesService workoutExerciseSeriesService, WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseSeriesService = workoutExerciseSeriesService;
        this.workoutExerciseService = workoutExerciseService;
    }

    @PostMapping
    public ResponseEntity<Object> registerSeries(@PathVariable UUID exerciseId, @RequestBody RegisterSeriesRequest registerSeriesRequest) {
        // VALIDATIONS
        if (registerSeriesRequest.exerciseId() == null) {
            return ResponseEntity.badRequest().body("Informe o ID do exercício.");
        }

        if (Utils.isNullOrZero(registerSeriesRequest.quantity())) {
            return ResponseEntity.badRequest().body("Informe a quantidade.");
        }

        if (Utils.isNullOrZero(registerSeriesRequest.repetitions())) {
            return ResponseEntity.badRequest().body("Informe as repetições.");
        }

        if (Utils.isNullOrZero(registerSeriesRequest.weightKg())) {
            return ResponseEntity.badRequest().body("Informe a carga.");
        }

        // CHECKS
        if (!workoutExerciseService.existsById(registerSeriesRequest.exerciseId())) {
            return new ResponseEntity<>("Exercício não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        WorkoutExerciseSeries workoutExerciseSeries = new WorkoutExerciseSeries(
                registerSeriesRequest.exerciseId(),
                registerSeriesRequest.quantity(),
                registerSeriesRequest.repetitions(),
                registerSeriesRequest.weightKg()
        );

        workoutExerciseSeriesService.save(workoutExerciseSeries);

        return ResponseEntity.ok("Série registrada com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSeries(@PathVariable UUID id, UpdateSeriesRequest updateSeriesRequest) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        if (Utils.isNullOrZero(updateSeriesRequest.quantity())) {
            return ResponseEntity.badRequest().body("A quantidade não pode ficar em branco.");
        }

        if (Utils.isNullOrZero(updateSeriesRequest.repetitions())) {
            return ResponseEntity.badRequest().body("As repetições não podem ficar em branco.");
        }

        if (Utils.isNullOrZero(updateSeriesRequest.weightKg())) {
            return ResponseEntity.badRequest().body("A carga não pode ficar em branco.");
        }

        WorkoutExerciseSeries workoutExerciseSeries = workoutExerciseSeriesService.findById(id);

        // CHECKS
        if (workoutExerciseSeries == null) {
            return new ResponseEntity<>("Série não encontrada com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        workoutExerciseSeries.setQuantity(updateSeriesRequest.quantity());
        workoutExerciseSeries.setRepetitions(updateSeriesRequest.repetitions());
        workoutExerciseSeries.setWeightKg(updateSeriesRequest.weightKg());
        workoutExerciseSeries.setDateAltDefault();
        workoutExerciseSeriesService.save(workoutExerciseSeries);

        return ResponseEntity.ok("Série atualizada com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSeriesById(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        WorkoutExerciseSeries workoutExerciseSeries = workoutExerciseSeriesService.findById(id);

        // CHECKS
        if (workoutExerciseSeries == null) {
            return new ResponseEntity<>("Série não encontrado com este ID.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(workoutExerciseSeries);
    }

    @GetMapping
    public ResponseEntity<Object> getSeriesByExerciseId(@RequestParam(name = "exerciseId") UUID exerciseId) {
        // VALIDATIONS
        if (exerciseId == null) {
            return ResponseEntity.badRequest().body("Informe o ID do exercício.");
        }

        List<WorkoutExerciseSeries> series = workoutExerciseSeriesService.findSeriesByWorkoutExerciseId(exerciseId);

        if (series == null || series.isEmpty()) {
            return new ResponseEntity<>("Nenhuma série encontrada com este ID de exercício.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(series);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> deleteSeries(@PathVariable UUID id) {
        // VALIDATIONS
        if (id == null) {
            return ResponseEntity.badRequest().body("Informe o ID.");
        }

        // CHEKS
        if (!workoutExerciseSeriesService.existsById(id)) {
            return new ResponseEntity<>("Série não encontrada com este ID.", HttpStatus.NOT_FOUND);
        }

        // PROCESS
        workoutExerciseSeriesService.deleteById(id);

        return ResponseEntity.ok("Série deletada com sucesso.");
    }
}
