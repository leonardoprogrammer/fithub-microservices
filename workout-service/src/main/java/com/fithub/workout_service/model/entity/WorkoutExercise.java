package com.fithub.workout_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workout_exercise")
public class WorkoutExercise implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "workout_id", nullable = false)
    private UUID workoutId;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    @Column(name = "date_alt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateAlt;

    public WorkoutExercise() {
        this.dateInc = Timestamp.valueOf(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(UUID workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
