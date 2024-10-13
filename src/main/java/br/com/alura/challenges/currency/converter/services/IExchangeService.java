package br.com.alura.challenges.currency.converter.services;

import java.net.http.HttpResponse;

public interface IExchangeService {
	HttpResponse<String> request(String uri);
}
