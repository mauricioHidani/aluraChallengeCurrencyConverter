package br.com.alura.challenges.currency.converter.configs;

import br.com.alura.challenges.currency.converter.utils.BannerUtil;
import br.com.alura.challenges.currency.converter.utils.PropertiesUtil;

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

	public BannerUtil bannerUtil() {
		return new BannerUtil();
	}

}
