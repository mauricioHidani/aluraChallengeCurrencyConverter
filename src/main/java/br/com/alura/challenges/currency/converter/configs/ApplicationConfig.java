package br.com.alura.challenges.currency.converter.configs;

import br.com.alura.challenges.currency.converter.utils.BannerUtil;
import br.com.alura.challenges.currency.converter.utils.PropertiesUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Scanner;

public class ApplicationConfig {

	public PropertiesUtil propertiesUtil() {
		return new PropertiesUtil();
	}

	public String propertiesName() {
		return "application";
	}

	public Scanner scanner() {
		return new Scanner(System.in);
	}

	public Gson gson() {
		return new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
	}

	public BannerUtil bannerUtil() {
		return new BannerUtil();
	}

}
