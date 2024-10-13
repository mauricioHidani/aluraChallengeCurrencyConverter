package br.com.alura.challenges.currency.converter.services.impls;

import br.com.alura.challenges.currency.converter.controllers.CurrencyController;
import br.com.alura.challenges.currency.converter.exceptions.NotFoundException;
import br.com.alura.challenges.currency.converter.models.CurrencyHistory;
import br.com.alura.challenges.currency.converter.models.CurrencyTarget;
import br.com.alura.challenges.currency.converter.models.app.enums.MenuState;
import br.com.alura.challenges.currency.converter.services.IActionConvertService;
import br.com.alura.challenges.currency.converter.services.ICurrencyService;
import br.com.alura.challenges.currency.converter.utils.CurrencyFormatUtil;
import br.com.alura.challenges.currency.converter.utils.InteractionUtil;
import br.com.alura.challenges.currency.converter.utils.LocalDateTimeParseUtil;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ActionConvertService implements IActionConvertService {
	
	private final ICurrencyService service;
	private final Scanner scanner;
	private final int breakLineLenght;

	private final InteractionUtil itr = new InteractionUtil();

	public ActionConvertService(final ICurrencyService service,
								final Scanner scanner,
								final int breakLineLenght) {
		this.service = service;
		this.scanner = scanner;
		this.breakLineLenght = breakLineLenght;
	}

	@Override
	public void init(final MenuState menuState) {
		if (menuState == MenuState.CONVERT_CURRENCY) {
			itr.breakSection(':', breakLineLenght);
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

				var animationThread = onAnimationThread();
				var taskThread = onTaskThread(currency, currencyValue, currencyTarget);

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

	private Thread onAnimationThread() {
		return new Thread(() -> {
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
	}

	private Thread onTaskThread(final String currency, final BigDecimal currencyValue, final String currencyTarget) {
		return new Thread(() -> {
			try {
				final var currencyFmt = new CurrencyFormatUtil();
				final var founded = service.getCurrency(currency);
				final var resultConverted = service.convertValueBy(currencyValue, currencyTarget, founded);

				var doneStr = "Pronto";
				System.out.printf("\r\r%s ", doneStr);
				itr.breakSection('.', breakLineLenght - doneStr.length() - 1);

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

				CurrencyController.addCurrencyOfHistory(
					new CurrencyHistory(
						currency, currencyValue,
						new CurrencyTarget(currencyTarget, resultConverted),
						founded.timeLastUpdate()
					)
				);

			} catch (IllegalArgumentException | NotFoundException e) {
				System.out.println("\r" + e.getMessage());
			}
		});
	}

}
