package com.fithub.workout_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workout_exercise_series")
public class WorkoutExerciseSeries implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "workout_exercise_id", nullable = false)
    private UUID workoutExerciseId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer repetitions;

    @Column(name = "weight_kg", precision = 10, scale = 2, nullable = false)
    private BigDecimal weightKg;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    @Column(name = "date_alt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateAlt;

    public WorkoutExerciseSeries() {
        this.dateInc = Timestamp.valueOf(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(UUID workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public Timestamp getDateInc() {
        return dateInc;
    }

    public void setDateInc(Timestamp dateInc) {
        this.dateInc = dateInc;
    }

    public Timestamp getDateAlt() {
        return dateAlt;
    }

    public void setDateAlt(Timestamp dateAlt) {
        this.dateAlt = dateAlt;
    }

    public void setDateAltDefault() {
        this.dateAlt = Timestamp.valueOf(LocalDateTime.now());
    }
}
