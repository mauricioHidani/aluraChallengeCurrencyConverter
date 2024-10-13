package br.com.alura.challenges.currency.converter.utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeParseUtil {

	public static LocalDateTime convertRFC1123(final String target) {
		if (target.isBlank()) {
			throw new IllegalArgumentException("Não é possivel realizar a operação com uma data inválida.");
		}

		try {
			var formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
			var zonedDateTime = ZonedDateTime.parse(target, formatter);
			return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
		} catch (DateTimeParseException e) {
			System.out.println("ERROR: Não é possivel converter a data no formato especificado.");
		} catch (DateTimeException e) {
			System.out.println("ERROR: Não é realizável a conversão da data não identificado seu local.");
		}

		return null;
	}

	public static String toComplete(final LocalDateTime dateTime) {
		var fmt = DateTimeFormatter.ofPattern("EE d/MM/yyyy hh:mm:ss");
		return fmt.format(dateTime);
	}

}
