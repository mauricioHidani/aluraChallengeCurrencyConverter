package br.com.alura.challenges.currency.converter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BannerUtil {

	private final String BANNER_ARCHIVE = "banner.txt";
	private final String BANNER_STD = "Alura One Challenge - Currency Converter";

	public String load() {
		try {
			var inputStream = BannerUtil.class.getClassLoader().getResourceAsStream(BANNER_ARCHIVE);
			var builder = new StringBuilder();

			if (inputStream != null) {
				try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
					var line = "";
					while ((line = reader.readLine()) != null) {
						builder.append(line + '\n');
					}
					return builder.toString();

				} catch (IOException e) {
					return BANNER_STD;
				}
			} else {
				return BANNER_STD;
			}
		} catch (NullPointerException e) {
			return BANNER_STD;
		}
	}

}
