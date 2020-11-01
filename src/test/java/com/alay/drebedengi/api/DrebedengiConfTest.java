package com.alay.drebedengi.api;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class DrebedengiConfTest {

    @Test
    public void testCustomUrl() throws URISyntaxException {
        String customDdServer = "https://www.customddserver.ru/soap/";
        DrebedengiConf conf = DrebedengiConf.builder()
                .uri(new URI(customDdServer))
                .apiKey("demo_api")
                .login("demo@example.com")
                .pass("demo")
                .build();
        assertThat(conf.getUri().toString()).isEqualTo(customDdServer);
    }

    @Test
    public void testDefaultServer() {
        assertThat(DrebedengiConf.DEFAULT_SERVER).isEqualTo("https://www.drebedengi.ru/soap/");

        DrebedengiConf conf = DrebedengiConf.builder()
                .apiKey("demo_api")
                .login("demo@example.com")
                .pass("demo")
                .build();
        assertThat(conf.getUri().toString()).isEqualTo(DrebedengiConf.DEFAULT_SERVER);
    }

}
