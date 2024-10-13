package br.com.alura.challenges.currency.converter.utils;

import java.util.Arrays;
import java.util.Scanner;

public class InteractionUtil {

	public void breakSection(final Character target, final Integer count) {
		var newCharArr = new char[count];
		Arrays.fill(newCharArr, target);
		System.out.println(new String(newCharArr));
	}

	public void clearScanner(final Scanner scanner) {
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}
	}

}
