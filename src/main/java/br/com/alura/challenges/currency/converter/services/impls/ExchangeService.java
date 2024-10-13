package br.com.alura.challenges.currency.converter.services.impls;

import br.com.alura.challenges.currency.converter.services.IExchangeService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeService implements IExchangeService {

	@Override
	public HttpResponse<String> request(String uri) {
		try (var client = HttpClient.newHttpClient()) {
			var request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (UncheckedIOException | IOException e) {
			System.out.println("Não é possivel estabelecer a requisição.");
		} catch (InterruptedException e) {
			System.out.println("Não é foi possivel realizar a operação, foi enterrompida antes.");
		}
		return null;
	}

}
