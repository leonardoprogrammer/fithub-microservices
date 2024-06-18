package com.fithub.workout_service.model.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Workout implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(length = 255)
    private String title;

    @Column(name = "workout_date")
    @Temporal(TemporalType.DATE)
    private Date workoutDate;

    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp startDateTime;

    @Column(name = "end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endDateTime;

    @Column(name = "timed_time")
    @Temporal(TemporalType.TIME)
    private Time timedTime;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(name = "date_inc", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateInc;

    @Column(name = "date_alt")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateAlt;

    private List<WorkoutExercise> exercises;

    public Workout() {
        this.dateInc = Timestamp.valueOf(LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(Date workoutDate) {
        this.workoutDate = workoutDate;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Time getTimedTime() {
        return timedTime;
    }

    public void setTimedTime(Time timedTime) {
        this.timedTime = timedTime;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }
}
