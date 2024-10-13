package br.com.alura.challenges.currency.converter.controllers;

import br.com.alura.challenges.currency.converter.models.CurrencyHistory;
import br.com.alura.challenges.currency.converter.models.app.BannerProps;
import br.com.alura.challenges.currency.converter.models.app.enums.MenuState;
import br.com.alura.challenges.currency.converter.services.IActionConvertService;
import br.com.alura.challenges.currency.converter.services.IActionHistory;
import br.com.alura.challenges.currency.converter.services.ICurrencyService;
import br.com.alura.challenges.currency.converter.services.impls.ActionConvertService;
import br.com.alura.challenges.currency.converter.services.impls.ActionHistory;
import br.com.alura.challenges.currency.converter.utils.BannerUtil;
import br.com.alura.challenges.currency.converter.utils.InteractionUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class CurrencyController {

	private final int BREAK_LINE_LENGHT = 64;

	private final Scanner scanner;
	private final BannerUtil bannerUtil;
	private final BannerProps bannerProps;

	private final IActionConvertService convertService;
	private final IActionHistory historyService;

	private static Set<CurrencyHistory> history = new HashSet<>();

	public CurrencyController(final Scanner scanner,
							  final BannerUtil bannerUtil,
							  final BannerProps bannerProps,
							  final ICurrencyService service) {
		this.scanner = scanner;
		this.bannerUtil = bannerUtil;
		this.bannerProps = bannerProps;

		this.convertService = new ActionConvertService(service, scanner, BREAK_LINE_LENGHT);
		this.historyService = new ActionHistory(BREAK_LINE_LENGHT);
	}

	public static synchronized void addCurrencyOfHistory(CurrencyHistory currency) {
		var now = LocalDateTime.now(Clock.systemDefaultZone());
		currency.setRegisteredIn(now);
		history.add(currency);
	}

	public static synchronized Set<CurrencyHistory> getCurrencyHistory() {
		return history;
	}

	public void start() {
		final var itr = new InteractionUtil();
		var state = MenuState.RUNNING;
		System.out.println(bannerUtil.load(bannerProps));

		do {
			state = choose();
			convertService.init(state);
			historyService.init(state);

			itr.breakSection(':', BREAK_LINE_LENGHT);
			itr.breakSection('*', BREAK_LINE_LENGHT);
		} while (state != MenuState.LEAVING);

		System.out.println("Obrigado por utilizar o conversor de moedas.\n");
	}

	private String mainMenu() {
		return """
				Menu Principal
				  1- Converter moeda
				  2- Historico de conversões
				  3- Sair""";
	}

	private MenuState choose() {
		final var itr = new InteractionUtil();

		System.out.println(mainMenu());
		System.out.print("Opção: ");
		try {
			var option = scanner.nextInt();
			switch (option) {
				case 1 -> { return MenuState.CONVERT_CURRENCY; }
				case 2 -> { return MenuState.CONVERSIONS_HISTORY; }
				case 3 -> { return MenuState.LEAVING; }
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

}
