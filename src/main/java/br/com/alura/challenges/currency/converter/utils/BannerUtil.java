package br.com.alura.challenges.currency.converter.utils;

import br.com.alura.challenges.currency.converter.models.app.BannerProps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BannerUtil {

	private final String BANNER_ARCHIVE = "banner.txt";
	private final String BANNER_STD = "Alura One Challenge - Currency Converter";

	public String load(BannerProps bannerProps) {
		var builder = new StringBuilder();

		try {
			var inputStream = BannerUtil.class.getClassLoader().getResourceAsStream(BANNER_ARCHIVE);
			if (inputStream != null) {
				try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
					var line = "";
					while ((line = reader.readLine()) != null) {
						builder.append(line);
						builder.append('\n');
					}
				} catch (IOException e) {
					builder.append(BANNER_STD);
				}
			} else {
				builder.append(BANNER_STD);
			}
		} catch (NullPointerException e) {
			builder.append(BANNER_STD);
		}

		if (bannerProps != null) {
			builder.append(bannerProps.title());
			builder.append('\s');
			builder.append(bannerProps.version());
			builder.append('\n');
			builder.append(bannerProps.since());
			builder.append('\n');
		}
		return builder.toString();
	}

}
