package com.alay.drebedengi.api;

import com.alay.drebedengi.api.data.Account;
import com.alay.drebedengi.api.data.Balance;
import com.alay.drebedengi.api.data.Category;
import com.alay.drebedengi.api.data.Currency;
import com.alay.drebedengi.api.data.Tag;
import com.alay.drebedengi.api.operations.Credit;
import com.alay.drebedengi.api.operations.Debit;
import com.alay.drebedengi.api.operations.Exchange;
import com.alay.drebedengi.api.operations.GenericRecord;
import com.alay.drebedengi.api.operations.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DrebedengiApiTest {

    private DrebedengiApi ddApi;

    @BeforeEach
    void init() {
        ddApi = new DrebedengiApi(DrebedengiConf.builder()
                .apiKey("demo_api")
                .login("demo@example.com")
                .pass("demo")
                .build()
        );
    }

    @Test
    public void testRevision() throws IOException {
        assertThat(ddApi.getCurrentRevision()).isPositive();
    }

    @Test
    public void testCategories_ok() throws IOException {
        List<Category> categories = ddApi.getDebitCategories();
        categories.forEach(System.out::println);

        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();
    }

    @Test
    public void testIncomes_ok() throws IOException {
        List<Category> categories = ddApi.getIncomeSources();
        categories.forEach(System.out::println);

        assertThat(categories).isNotNull();
        assertThat(categories).isNotEmpty();
    }

    @Test
    public void testTags_ok() throws IOException {
        List<Tag> tags = ddApi.getTags();
        tags.forEach(System.out::println);

        assertThat(tags).isNotNull();
        assertThat(tags).isNotEmpty();
    }

    @Test
    public void testBalances_ok() throws IOException {
        List<Balance> balances = ddApi.getBalances();
        balances.forEach(System.out::println);

        assertThat(balances).isNotNull();
        assertThat(balances).isNotEmpty();
    }

    @Test
    public void testAccounts_ok() throws IOException {
        List<Account> accounts = ddApi.getAccounts();
        accounts.forEach(System.out::println);

        assertThat(accounts).isNotNull();
        assertThat(accounts).isNotEmpty();
    }

    @Test
    public void testCurrencies_ok() throws IOException {
        List<Currency> currencies = ddApi.getCurrencies();
        currencies.forEach(System.out::println);

        assertThat(currencies).isNotNull();
        assertThat(currencies).isNotEmpty();
    }

    @Test
    public void testExchange_ok() throws IOException {
        List<Map<String, String>> result = ddApi.createExchange(Exchange.builder()
                .accountId(40034)
                .soldSum(BigDecimal.valueOf(245.15))
                .soldCurrencyId(19)
                .boughtSum(BigDecimal.valueOf(136.11))
                .boughtCurrencyId(17)
                .date(LocalDateTime.now())
                .comment("auto exchange")
                .build()
        );
        result.forEach(System.out::println);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsValue("inserted");
        assertThat(result.get(1)).containsValue("inserted");
    }

    @Test
    public void testTransfer_ok() throws IOException {
        List<Map<String, String>> result = ddApi.createTransfer(Transfer.builder()
                .sum(BigDecimal.valueOf(245.15))
                .fromAccountId(40034)
                .currencyId(19)
                .toAccountId(40040)
                .date(LocalDateTime.now())
                .comment("auto exchange")
                .build()
        );
        result.forEach(System.out::println);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).containsValue("inserted");
        assertThat(result.get(1)).containsValue("inserted");
    }

    @Test
    public void testDebit_ok() throws IOException {
        List<Map<String, String>> result = ddApi.createDebit(Debit.builder()
                .sum(BigDecimal.valueOf(245.15))
                .accountId(40034)
                .categoryId(40001)
                .currencyId(19)
                .date(LocalDateTime.now())
                .build()
        );
        result.forEach(System.out::println);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsValue("inserted");
    }

    @Test
    public void testCredit_ok() throws IOException {
        List<Map<String, String>> result = ddApi.createCredit(Credit.builder()
                .sum(BigDecimal.valueOf(245.15))
                .accountId(40034)
                .categoryId(40041)
                .currencyId(19)
                .date(LocalDateTime.now())
                .build()
        );
        result.forEach(System.out::println);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsValue("inserted");
    }

    @Test
    public void testGetRecords_ok() throws IOException {
        List<GenericRecord> expenses = ddApi.fetchDebits(
                LocalDate.of(2020, 1, 1)
        );
        assertThat(expenses).isNotEmpty();
    }
}
