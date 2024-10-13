package br.com.alura.challenges.currency.converter.services;

import br.com.alura.challenges.currency.converter.models.Currency;

import java.math.BigDecimal;

public interface ICurrencyService {
	Currency getCurrency(final String currency);
	BigDecimal convertValueBy(final BigDecimal value, final String targetExchange, final Currency currency);
}
