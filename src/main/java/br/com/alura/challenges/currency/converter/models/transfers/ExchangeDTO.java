package br.com.alura.challenges.currency.converter.models.transfers;

import java.util.Map;

public record ExchangeDTO(String baseCode, Map<String, Double> conversionRates, String timeLastUpdateUtc) {

	public boolean isNonNull() {
		return baseCode != null ||
				conversionRates != null ||
				timeLastUpdateUtc != null;
	}

}
