package br.com.alura.challenges.currency.converter.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CurrencyFormatUtil {

	public String toFormat(final BigDecimal value, final String prefix) {
		var currencyFmt = new DecimalFormat("#,##0.00");
		currencyFmt.setPositivePrefix(prefix + " ");

		return currencyFmt.format(value);
	}

	public String toFormat(final BigDecimal value) {
		var currencyFmt = new DecimalFormat("#,##0.00");

		return currencyFmt.format(value);
	}

}
