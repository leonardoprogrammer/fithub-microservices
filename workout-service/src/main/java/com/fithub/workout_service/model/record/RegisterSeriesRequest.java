package com.fithub.workout_service.model.record;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterSeriesRequest(UUID exerciseId, Integer quantity, Integer repetitions, BigDecimal weightKg) {
}
