package com.alay.drebedengi.api;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URL;

@Builder
@Getter
public class DrebedengiConf {
	public static final String DEFAULT_SERVER = "https://www.drebedengi.ru/soap/";

	@Builder.Default
	private final URI uri = getDefaultUri();
	private final String apiKey;
	private final String login;
	private final String pass;

	@SneakyThrows
	private static URI getDefaultUri() {
		return new URI(DEFAULT_SERVER);
	}
}
