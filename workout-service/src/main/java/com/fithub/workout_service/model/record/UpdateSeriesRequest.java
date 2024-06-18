package com.fithub.workout_service.model.record;

import java.math.BigDecimal;

public record UpdateSeriesRequest(Integer quantity, Integer repetitions, BigDecimal weightKg) {
}
