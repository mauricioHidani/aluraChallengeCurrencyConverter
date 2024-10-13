package br.com.alura.challenges.currency.converter.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record Currency(String baseCode, Map<String, BigDecimal> conversionRates, LocalDateTime timeLastUpdate) {
}
