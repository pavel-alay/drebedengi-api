package com.alay.drebedengi.api;

import com.alay.drebedengi.api.data.Account;
import com.alay.drebedengi.api.data.Category;
import com.alay.drebedengi.api.data.Currency;
import com.alay.drebedengi.api.data.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DrebedengiData {

    @Getter
    private List<Account> accounts;
    @Getter
    private List<Currency> currencies;
    @Getter
    private List<Category> debitCategories;
    @Getter
    private List<Category> creditSources;
    @Getter
    private List<Tag> tags;

    private Map<Integer, Account> accountMap;
    private Map<Integer, Currency> currencyMap;
    private Map<String, Integer> currencyIdMap;
    private Map<Integer, Category> debitCategoryMap;
    private Map<String, Integer> debitCategoryIdMap;
    private Map<Integer, Category> creditSourceMap;
    private Map<String, Integer> creditSourceIdMap;
    private Map<String, Integer> tagIdMap;

    private DrebedengiData(List<Account> accounts, List<Currency> currencies, List<Category> debitCategories,
                           List<Category> incomeSources, List<Tag> tags) {
        this.accounts = accounts;
        this.currencies = currencies;
        this.debitCategories = debitCategories;
        this.creditSources = incomeSources;
        this.tags = tags;
    }

    public Account getAccount(Integer id) {
        if (accountMap == null) {
            accountMap = getAccounts().stream()
                    .collect(Collectors.toMap(Account::getId, account -> account));
        }
        return accountMap.get(id);
    }

    public String getAccountById(int id) {
        return getAccount(id).getName();
    }

    public Currency getCurrency(Integer id) {
        if (currencyMap == null) {
            currencyMap = getCurrencies().stream()
                    .collect(Collectors.toMap(Currency::getId, c -> c));
        }
        return currencyMap.get(id);
    }

    public Integer getCurrencyId(String currency) {
        if (currencyIdMap == null) {
            currencyIdMap = getCurrencies().stream()
                    .collect(Collectors.toMap(Currency::getCode, Currency::getId));
        }
        return currencyIdMap.get(currency);
    }

    public String getCurrencyById(int id) {
        return getCurrency(id).getCode();
    }

    public Integer getCurrencyId(java.util.Currency currency) {
        return getCurrencyId(currency.getCurrencyCode());
    }

    public Category getDebitCategory(Integer id) {
        if (debitCategoryMap == null) {
            debitCategoryMap = getDebitCategories().stream()
                    .collect(Collectors.toMap(Category::getId, c -> c));
        }
        return debitCategoryMap.get(id);
    }

    public Integer getDebitCategoryId(String category) {
        if (debitCategoryIdMap == null) {
            debitCategoryIdMap = getDebitCategories().stream()
                    .collect(Collectors.toMap(c -> c.getName().toLowerCase(), Category::getId));
        }
        return debitCategoryIdMap.get(category.toLowerCase());
    }

    public Category getCreditSource(Integer id) {
        if (creditSourceMap == null) {
            creditSourceMap = getCreditSources().stream()
                    .collect(Collectors.toMap(Category::getId, s -> s));
        }
        return creditSourceMap.get(id);
    }

    public Integer getCreditSourceId(String category) {
        if (creditSourceIdMap == null) {
            creditSourceIdMap = getCreditSources().stream()
                    .collect(Collectors.toMap(c -> c.getName().toLowerCase(), Category::getId));
        }
        return creditSourceIdMap.get(category.toLowerCase());
    }

    /**
     * gets tag id by tag name with brackets
     *
     * @param tag name of tag with brackets
     * @return id of tag
     */
    public Integer getTagId(String tag) {
        if (tagIdMap == null) {
            tagIdMap = getTags().stream()
                    .collect(Collectors.toMap(c -> String.format("[%s]", c.getName().toLowerCase()), Tag::getId, (a, b) -> a));
        }
        return tagIdMap.get(tag.toLowerCase());
    }

    @SneakyThrows
    public static DrebedengiData load(DrebedengiApi api) {
        // I use ForkJoinTask here, because all api methods throws IOException,
        //  so it cannot be used directly with CompletableFeature.
        ForkJoinTask<List<Account>> accounts = ForkJoinPool.commonPool().submit(api::getAccounts);
        ForkJoinTask<List<Currency>> currencies = ForkJoinPool.commonPool().submit(api::getCurrencies);
        ForkJoinTask<List<Category>> categories = ForkJoinPool.commonPool().submit(api::getDebitCategories);
        ForkJoinTask<List<Category>> incomes = ForkJoinPool.commonPool().submit(api::getIncomeSources);
        ForkJoinTask<List<Tag>> tags = ForkJoinPool.commonPool().submit(api::getTags);

        return new DrebedengiData(accounts.get(), currencies.get(), categories.get(),
                incomes.get(), tags.get()
        );
    }
}
