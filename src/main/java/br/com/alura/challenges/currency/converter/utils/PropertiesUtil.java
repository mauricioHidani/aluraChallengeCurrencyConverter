package br.com.alura.challenges.currency.converter.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PropertiesUtil {

	public <T> T load(final String fileName, final Class<T> clazz) {
		try {
			var gson = new Gson();
			var path = PropertiesUtil.class.getClassLoader().getResource(fileName + ".json").getPath();
			try (var reader = new JsonReader(new FileReader(path))) {
				return gson.fromJson(reader, clazz);
			} catch (FileNotFoundException e) {
				System.out.println("ERROR: Não foi possivel encontrar o arquivo de configuração indicado.");
			} catch (IOException e) {
				System.out.println("ERROR: Não foi possivel ler o arquivo de configuração indicado.");
			}
		} catch (NullPointerException e) {
			System.out.println("ERROR: Não foi possivel realizar a operação de configuração indicada.");
		}

		return null;
	}

}
