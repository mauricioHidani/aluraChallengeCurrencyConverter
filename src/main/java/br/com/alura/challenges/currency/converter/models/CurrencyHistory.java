package br.com.alura.challenges.currency.converter.models;

import br.com.alura.challenges.currency.converter.utils.CurrencyFormatUtil;
import br.com.alura.challenges.currency.converter.utils.LocalDateTimeParseUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrencyHistory {

	private final String currency;
	private final BigDecimal value;
	private final CurrencyTarget target;
	private final LocalDateTime timeLastUpdate;
	private LocalDateTime registeredIn;

	public CurrencyHistory(final String currency,
						   final BigDecimal value,
						   final CurrencyTarget target,
						   final LocalDateTime timeLastUpdate) {
		this.currency = currency;
		this.value = value;
		this.target = target;
		this.timeLastUpdate = timeLastUpdate;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public CurrencyTarget getTarget() {
		return target;
	}

	public LocalDateTime getTimeLastUpdate() {
		return timeLastUpdate;
	}

	public LocalDateTime getRegisteredIn() {
		return registeredIn;
	}

	public void setRegisteredIn(LocalDateTime registeredIn) {
		this.registeredIn = registeredIn;
	}

	@Override
	public String toString() {
		var currencyFmt = new CurrencyFormatUtil();
		return """
				Registrado em %s
				[Moeda %s | Conversão %s]
				Ultima atualização da cotação [%s]"""
				.formatted(
					LocalDateTimeParseUtil.toComplete(registeredIn),
					currencyFmt.toFormat(value, currency),
					currencyFmt.toFormat(target.value(), target.currency()),
					LocalDateTimeParseUtil.toComplete(timeLastUpdate)
				);
	}

}
