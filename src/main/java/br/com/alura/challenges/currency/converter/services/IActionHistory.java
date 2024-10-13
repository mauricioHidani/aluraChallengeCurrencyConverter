package br.com.alura.challenges.currency.converter.services;

import br.com.alura.challenges.currency.converter.models.app.enums.MenuState;

public interface IActionHistory {
	void init(MenuState state);
}
