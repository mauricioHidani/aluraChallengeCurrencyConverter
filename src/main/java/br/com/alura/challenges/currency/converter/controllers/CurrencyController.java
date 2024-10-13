package br.com.alura.challenges.currency.converter.controllers;

import br.com.alura.challenges.currency.converter.exceptions.NotFoundException;
import br.com.alura.challenges.currency.converter.models.app.BannerProps;
import br.com.alura.challenges.currency.converter.models.app.enums.MenuState;
import br.com.alura.challenges.currency.converter.services.ICurrencyService;
import br.com.alura.challenges.currency.converter.utils.BannerUtil;
import br.com.alura.challenges.currency.converter.utils.CurrencyFormatUtil;
import br.com.alura.challenges.currency.converter.utils.InteractionUtil;
import br.com.alura.challenges.currency.converter.utils.LocalDateTimeParseUtil;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CurrencyController {

	private final int BREAK_LINE_LENGHT = 64;

	private final Scanner scanner;
	private final BannerUtil bannerUtil;
	private final BannerProps bannerProps;
	private final ICurrencyService service;

	public CurrencyController(final Scanner scanner,
							  final BannerUtil bannerUtil,
							  final BannerProps bannerProps,
							  final ICurrencyService service) {
		this.scanner = scanner;
		this.bannerUtil = bannerUtil;
		this.bannerProps = bannerProps;
		this.service = service;
	}

	public void start() {
		final var itr = new InteractionUtil();
		var state = MenuState.RUNNING;
		System.out.println(bannerUtil.load(bannerProps));

		do {
			state = choose();
			onConvertChecked(state);

			itr.breakSection(':', BREAK_LINE_LENGHT);
			itr.breakSection('*', BREAK_LINE_LENGHT);
		} while (state != MenuState.LEAVING);

		System.out.println("Obrigado por utilizar o conversor de moedas.\n");
	}

	private String mainMenu() {
		return """
    			
				Menu Principal
				  1- Converter moeda
				  2- Sair""";
	}

	private MenuState choose() {
		final var itr = new InteractionUtil();

		System.out.println(mainMenu());
		System.out.print("Opção: ");
		try {
			var option = scanner.nextInt();
			switch (option) {
				case 1 -> { return MenuState.CONVERT_CURRENCY; }
				case 2 -> { return MenuState.LEAVING; }
				default -> throw new IllegalArgumentException("Essa opção não existente.");
			}

		} catch (InputMismatchException e) {
			System.out.println("Não é possivel prosseguir, a opção selecionada não é válida.");
			itr.clearScanner(scanner);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		return MenuState.RUNNING;
	}

	private void onConvertChecked(final MenuState menuState) {
		final var itr = new InteractionUtil();

		if (menuState == MenuState.CONVERT_CURRENCY) {
			itr.breakSection(':', BREAK_LINE_LENGHT);
			System.out.println("Opção selecionada: Converter moeda");
			System.out.println("\t- A identificação das moedas segue a ISO 4217.");
			System.out.println("\t- (exp: USD = United State Dolar).");
			System.out.println();

			try {
				System.out.println("> Informe a moeda que será convertida, e em seguida seu valor.");
				var currency = inCurrency(itr);
				var currencyValue = inCurrencyValue(itr);

				System.out.println("> Agora informe para qual moeda deseja convertê-la.");
				var currencyTarget = inCurrency(itr);

				var animationThread = new Thread(() -> {
					try {
						while (!Thread.currentThread().isInterrupted()) {
							System.out.print("\rConvertendo |");
							Thread.sleep(125);
							System.out.print("\rConvertendo /");
							Thread.sleep(125);
							System.out.print("\rConvertendo _");
							Thread.sleep(125);
							System.out.print("\rConvertendo \\");
							Thread.sleep(125);
						}
					} catch (InterruptedException e) {
						System.out.print("\r");
					}
				});

				var taskThread = new Thread(() -> {
					try {

						final var currencyFmt = new CurrencyFormatUtil();
						final var founded = service.getCurrency(currency);
						final var resultConverted = service.convertValueBy(currencyValue, currencyTarget, founded);

						var doneStr = "Pronto";
						System.out.printf("\r\r%s ", doneStr);
						itr.breakSection('.', BREAK_LINE_LENGHT - doneStr.length() - 1);

						System.out.printf(
							"Moeda utilizada na conversão %s vale [%s]\n",
							currencyTarget,
							currencyFmt.toFormat(
								founded.conversionRates().get(currencyTarget),
								currency
							)
						);
						System.out.printf(
							"Ultima atualização da moeda em %s\n",
							LocalDateTimeParseUtil.toComplete(founded.timeLastUpdate())
						);

						System.out.printf(
							"\nResultado da conversão de [%s] para [%s]\n",
							currencyFmt.toFormat(currencyValue, currency),
							currencyFmt.toFormat(resultConverted, currencyTarget)
						);
					} catch (IllegalArgumentException | NotFoundException e) {
						System.out.println("\r" + e.getMessage());
					}
				});

				animationThread.start();
				taskThread.start();

				taskThread.join();
				animationThread.interrupt();
				animationThread.join();
			} catch (InputMismatchException e) {
				System.out.println("O valor informado não é valido.");
			} catch ( IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (InterruptedException e) {
				e.getStackTrace();
			}
		}
	}

	private String inCurrency(final InteractionUtil itr) {
		System.out.print("Moeda: ");
		itr.clearScanner(scanner);
		var currency = scanner.nextLine();
		if (currency.length() != 3) {
			throw new IllegalArgumentException("A moeda informada não é valida, deve possuir 3 caracteres.");
		}
		if (!currency.matches("^[a-zA-Z]+$")) {
			throw new IllegalArgumentException("A moeda não é reconhecida.");
		}
		return currency;
	}

	private BigDecimal inCurrencyValue(final InteractionUtil itr) {
		System.out.print("Valor: ");
		var currencyValue = BigDecimal.valueOf(scanner.nextDouble());
		if (currencyValue.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException(
				"Não é possivel converter moedas com valor Zero."
			);
		}
		if (currencyValue.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(
				"Não é possivel converter moedas negativas."
			);
		}
		return currencyValue;
	}

}
