package br.com.alura.challenges.currency.converter.services.impls;

import br.com.alura.challenges.currency.converter.controllers.CurrencyController;
import br.com.alura.challenges.currency.converter.models.app.enums.MenuState;
import br.com.alura.challenges.currency.converter.services.IActionHistory;
import br.com.alura.challenges.currency.converter.utils.InteractionUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class ActionHistory implements IActionHistory {

	private final int breakLineLenght;

	private final InteractionUtil itr = new InteractionUtil();

	public ActionHistory(final int breakLineLenght) {
		this.breakLineLenght = breakLineLenght;
	}

	@Override
	public void init(MenuState state) {
		if (state == MenuState.CONVERSIONS_HISTORY) {
			itr.breakSection(':', breakLineLenght);
			System.out.println("Opção selecionada: Histórico de converção de moedas\n");

			final var lengthHistory = CurrencyController.getCurrencyHistory().size();

			var index = new AtomicInteger(0);
			CurrencyController.getCurrencyHistory().forEach(registry -> {
				final var i = index.getAndIncrement();
				System.out.println(registry);
				if (i < (lengthHistory - 1)) {
					itr.breakSection('.', breakLineLenght);
				}
			});
		}
	}

}
