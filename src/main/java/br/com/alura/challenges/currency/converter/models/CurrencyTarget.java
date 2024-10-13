package br.com.alura.challenges.currency.converter.models;

import java.math.BigDecimal;

public record CurrencyTarget(String currency, BigDecimal value) {
}
