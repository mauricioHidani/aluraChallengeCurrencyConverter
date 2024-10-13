package br.com.alura.challenges.currency.converter.services.impls;

import br.com.alura.challenges.currency.converter.exceptions.NotFoundException;
import br.com.alura.challenges.currency.converter.models.Currency;
import br.com.alura.challenges.currency.converter.models.app.ExchangeProps;
import br.com.alura.challenges.currency.converter.models.transfers.ExchangeDTO;
import br.com.alura.challenges.currency.converter.services.ICurrencyService;
import br.com.alura.challenges.currency.converter.services.IExchangeService;
import br.com.alura.challenges.currency.converter.utils.LocalDateTimeParseUtil;
import br.com.alura.challenges.currency.converter.utils.PropertiesUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurrencyService implements ICurrencyService {

	private final IExchangeService exchangeService;
	private final String exchangeURI;

	private final Gson gson;

	public CurrencyService(final PropertiesUtil props,
						   final String propsPath,
						   final IExchangeService exchangeService,
						   final Gson gson) {
		var exchangeProps = props.load(propsPath, ExchangeProps.class);
		this.exchangeService = exchangeService;
		this.exchangeURI = String.format(
			"%s/%s/%s/",
			exchangeProps.exchangeUri(),
			exchangeProps.exchangeKey(),
			exchangeProps.exchangeVersion()
		);

		this.gson = gson;
	}

	@Override
	public Currency getCurrency(final String currency) {
		var json = exchangeService.request(exchangeURI + currency.toUpperCase()).body();
		var exchangeDTO = gson.fromJson(json, ExchangeDTO.class);
		if (!exchangeDTO.isNonNull()) {
			throw new NotFoundException("Não foi possivel encontrar a moeda indicada.");
		}

		var baseCode = exchangeDTO.baseCode();
		var timeLastUpdate = LocalDateTimeParseUtil.convertRFC1123(exchangeDTO.timeLastUpdateUtc());
		Map<String, BigDecimal> conversionRates = new HashMap<>();
		for (var entry : exchangeDTO.conversionRates().entrySet()) {
			if (entry.getValue() != null) {
				conversionRates.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
			}
		}

		return new Currency(baseCode, conversionRates, timeLastUpdate);
	}

	@Override
	public BigDecimal convertValueBy(final BigDecimal value, final String targetExchange, final Currency currency) {
		if (targetExchange.isBlank()) {
			throw new IllegalArgumentException(
				"Operação inválida, não é possivel prosseguir sem saber qual a moeda a ser convertida."
			);
		}

		if (!currency.conversionRates().containsKey(targetExchange)) {
			throw new NotFoundException(
				"Não foi possivel encontrar a moeda de troca informada."
			);
		}

		var targetCurrency = currency.conversionRates().get(targetExchange);
		return value.multiply(targetCurrency);
	}
}
