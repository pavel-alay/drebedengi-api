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
import com.alay.drebedengi.api.operations.OperationType;
import com.alay.drebedengi.api.operations.Transfer;
import com.alay.drebedengi.soap.functions.GetBalance;
import com.alay.drebedengi.soap.functions.GetCategoryList;
import com.alay.drebedengi.soap.functions.GetCurrencyList;
import com.alay.drebedengi.soap.functions.GetCurrentRevision;
import com.alay.drebedengi.soap.functions.GetPlaceList;
import com.alay.drebedengi.soap.functions.GetRecordList;
import com.alay.drebedengi.soap.functions.GetSourceList;
import com.alay.drebedengi.soap.functions.GetTagList;
import com.alay.drebedengi.soap.functions.SetRecordList;
import com.alay.drebedengi.soap.functions.base.BaseFunction;
import com.alay.drebedengi.soap.responses.GetBalanceReturn;
import com.alay.drebedengi.soap.responses.GetCategoryListReturn;
import com.alay.drebedengi.soap.responses.GetCurrencyListReturn;
import com.alay.drebedengi.soap.responses.GetCurrentRevisionReturn;
import com.alay.drebedengi.soap.responses.GetPlaceListReturn;
import com.alay.drebedengi.soap.responses.GetRecordListReturn;
import com.alay.drebedengi.soap.responses.GetSourceListReturn;
import com.alay.drebedengi.soap.responses.GetTagListReturn;
import com.alay.drebedengi.soap.responses.SetRecordListReturn;
import com.alay.drebedengi.soap.responses.base.BaseResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@SuppressWarnings("rawtypes")
public class DrebedengiApi {

    private final WebClient webClient;
    private static final AtomicInteger clientIdCounter = new AtomicInteger(getTimeBasedId());

    public DrebedengiApi(DrebedengiConf conf) {
        this.webClient = new WebClient(conf);
    }

    public int getCurrentRevision() throws IOException {
        GetCurrentRevisionReturn response = request(new GetCurrentRevision());
        return Integer.parseInt(response.getValue());
    }

    public List<Balance> getBalances() throws IOException {
        return getBalances(LocalDate.now(), false, false);
    }

    public List<Category> getDebitCategories() throws IOException {
        GetCategoryListReturn response = request(new GetCategoryList());

        return response.getList()
                .stream()
                .map(Category::new)
                .collect(Collectors.toList());
    }

    public List<Category> getIncomeSources() throws IOException {
        GetSourceListReturn response = request(new GetSourceList());

        return response.getList()
                .stream()
                .map(Category::new)
                .collect(Collectors.toList());
    }

    public List<Balance> getBalances(LocalDate date, boolean withAccum, boolean withDuty) throws IOException {
        GetBalanceReturn response = request(new GetBalance()
                .withParam("restDate", DateTimeUtils.dateToString(date))
                .withParam("is_with_accum", withAccum)
                .withParam("is_with_duty", withDuty));

        return response.getList()
                .stream()
                .map(Balance::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrievs place list (array of arrays):
     * [id] => Internal place ID;
     * [budget_family_id] => User family ID (for multiuser mode);
     * [type] => Type of object, 4 - places; [name] => Place name given by user;
     * [is_hidden] => is place hidden in user interface;
     * [is_autohide] => debts will auto hide on null balance;
     * [is_for_duty] => Internal place for duty logic, Auto created while user adds "Waste or income duty";
     * [sort] => User sort of place list;
     * [purse_of_nuid] => Not empty if place is purse of user # The value is internal user ID;
     * [icon_id] => Place icon ID from http://www.drebedengi.ru/img/pl[icon_id].gif;
     * If parameter [idList] is given, it will be treat as ID list of objects to retrieve # this is used for synchronization;
     * There is may be empty response, if user access level is limited;
     *
     * @return list of account
     * @throws RemoteException on any network error
     */
    public List<Account> getAccounts() throws IOException {
        GetPlaceListReturn response = request(new GetPlaceList());

        return response.getList()
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public List<Currency> getCurrencies() throws IOException {
        GetCurrencyListReturn response = request(
                new GetCurrencyList());

        return response.getList()
                .stream()
                .map(Currency::new)
                .collect(Collectors.toList());
    }

    public List<Tag> getTags() throws IOException {
        GetTagListReturn response = request(new GetTagList());

        return response.getList()
                .stream()
                .map(Tag::new)
                .collect(Collectors.toList());
    }

    public List<Map<String, String>> createExchange(Exchange exchange) throws IOException {
        int clientIdSold = clientIdCounter.incrementAndGet();
        int clientIdBought = clientIdCounter.incrementAndGet();
        SetRecordListReturn response = request(new SetRecordList()
                .add(Map.ofEntries(
                        entry("client_id", clientIdSold),
                        entry("client_change_id", clientIdBought),
                        entry("place_id", exchange.getAccountId()),
                        entry("budget_object_id", exchange.getAccountId()),
                        entry("sum", -1 * convertAmountToInt(exchange.getSoldSum())),
                        entry("currency_id", exchange.getSoldCurrencyId()),
                        entry("operation_type", OperationType.EXCHANGE.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(exchange.getDate())),
                        entry("comment", exchange.getComment()),
                        entry("is_duty", false)
                ))
                .add(Map.ofEntries(
                        entry("client_id", clientIdBought),
                        entry("client_change_id", clientIdSold),
                        entry("place_id", exchange.getAccountId()),
                        entry("budget_object_id", exchange.getAccountId()),
                        entry("sum", convertAmountToInt(exchange.getBoughtSum())),
                        entry("currency_id", exchange.getBoughtCurrencyId()),
                        entry("operation_type", OperationType.EXCHANGE.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(exchange.getDate())),
                        entry("comment", exchange.getComment()),
                        entry("is_duty", false)
                )));

        return response.getList();
    }

    public List<Map<String, String>> createTransfer(Transfer transfer) throws IOException {
        int clientIdFrom = clientIdCounter.incrementAndGet();
        int clientIdTo = clientIdCounter.incrementAndGet();
        SetRecordListReturn response = request(new SetRecordList()
                .add(Map.ofEntries(
                        entry("client_id", clientIdFrom),
                        entry("client_move_id", clientIdTo),
                        entry("place_id", transfer.getFromAccountId()),
                        entry("budget_object_id", transfer.getToAccountId()),
                        entry("sum", -1 * convertAmountToInt(transfer.getSum())),
                        entry("currency_id", transfer.getCurrencyId()),
                        entry("operation_type", OperationType.TRANSFER.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(transfer.getDate())),
                        entry("comment", transfer.getComment()),
                        entry("is_duty", false)
                ))
                .add(Map.ofEntries(
                        entry("client_id", clientIdTo),
                        entry("client_move_id", clientIdFrom),
                        entry("place_id", transfer.getToAccountId()),
                        entry("budget_object_id", transfer.getFromAccountId()),
                        entry("sum", convertAmountToInt(transfer.getSum())),
                        entry("currency_id", transfer.getCurrencyId()),
                        entry("operation_type", OperationType.TRANSFER.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(transfer.getDate())),
                        entry("comment", transfer.getComment()),
                        entry("is_duty", false)
                )));

        return response.getList();
    }

    public List<Map<String, String>> createDebit(Debit debit) throws IOException {
        int clientId = clientIdCounter.incrementAndGet();
        SetRecordListReturn response = request(new SetRecordList()
                .add(Map.ofEntries(
                        entry("client_id", clientId),
                        entry("sum", convertAmountToInt(debit.getSum())),
                        entry("place_id", debit.getAccountId()),
                        entry("budget_object_id", debit.getCategoryId()),
                        entry("currency_id", debit.getCurrencyId()),
                        entry("operation_type", OperationType.DEBIT.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(debit.getDate())),
                        entry("comment", debit.getComment()),
                        entry("is_duty", false)
                )));

        return response.getList();
    }

    public List<Map<String, String>> createCredit(Credit debit) throws IOException {
        int clientId = clientIdCounter.incrementAndGet();
        SetRecordListReturn response = request(new SetRecordList()
                .add(Map.ofEntries(
                        entry("client_id", clientId),
                        entry("sum", convertAmountToInt(debit.getSum())),
                        entry("place_id", debit.getAccountId()),
                        entry("budget_object_id", debit.getCategoryId()),
                        entry("currency_id", debit.getCurrencyId()),
                        entry("operation_type", OperationType.CREDIT.getValue()),
                        entry("operation_date", DateTimeUtils.dateToString(debit.getDate())),
                        entry("comment", debit.getComment()),
                        entry("is_duty", false)
                )));

        return response.getList();
    }

    public List<Map<String, String>> updateRecords(GenericRecord... records) throws IOException {
        SetRecordList recordList = new SetRecordList();
        for (GenericRecord record : records) {
            recordList.add(record.convertToMap());
        }
        SetRecordListReturn response = request(recordList);

        return response.getList();
    }

    public List<GenericRecord> fetchDebits(LocalDate from) throws IOException {
        GetRecordListReturn response = request(new GetRecordList()
                // Data not for report, but for export
                .withParam("is_report", false)
                // Show last 20 record (for each operation type, if not one, see 'r_what')
                .withParam("r_period", 0)
                // 'period_from' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_from", DateTimeUtils.dateToString(from))
                // 'period_to' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_to", DateTimeUtils.dateToString(LocalDate.now()))
                .withParam("r_what", OperationType.DEBIT.getValue())
                // show record list by detail = 1
                .withParam("r_how", 1)
        );

        return response.getMap().values()
                .stream()
                .map(GenericRecord::new)
                .collect(Collectors.toList());
    }

    public List<GenericRecord> fetchCredits(LocalDate from) throws IOException {
        GetRecordListReturn response = request(new GetRecordList()
                // Data not for report, but for export
                .withParam("is_report", false)
                // Show last 20 record (for each operation type, if not one, see 'r_what')
                .withParam("r_period", 0)
                // 'period_from' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_from", DateTimeUtils.dateToString(from))
                // 'period_to' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_to", DateTimeUtils.dateToString(LocalDate.now()))
                .withParam("r_what", OperationType.CREDIT.getValue())
                // show record list by detail = 1
                .withParam("r_how", 1)
        );

        return response.getMap().values()
                .stream()
                .map(GenericRecord::new)
                .collect(Collectors.toList());
    }

    public List<GenericRecord> fetchExchanges(LocalDate from) throws IOException {
        GetRecordListReturn response = request(new GetRecordList()
                // Data not for report, but for export
                .withParam("is_report", false)
                // Show last 20 record (for each operation type, if not one, see 'r_what')
                .withParam("r_period", 0)
                // 'period_from' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_from", DateTimeUtils.dateToString(from))
                // 'period_to' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                .withParam("period_to", DateTimeUtils.dateToString(LocalDate.now()))
                .withParam("r_what", OperationType.EXCHANGE.getValue())
                // show record list by detail = 1
                .withParam("r_how", 1)
        );

        return response.getMap().values()
                .stream()
                .map(GenericRecord::new)
                .collect(Collectors.toList());
    }

    public List<GenericRecord> fetchTransfers(LocalDate from) throws IOException {
        GetRecordListReturn response = request(
                new GetRecordList()
                        // Data not for report, but for export
                        .withParam("is_report", false)
                        // Show last 20 record (for each operation type, if not one, see 'r_what')
                        .withParam("r_period", 0)
                        // 'period_from' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                        .withParam("period_from", DateTimeUtils.dateToString(from))
                        // 'period_to' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
                        .withParam("period_to", DateTimeUtils.dateToString(LocalDate.now()))
                        .withParam("r_what", OperationType.TRANSFER.getValue())
                        // show record list by detail = 1
                        .withParam("r_how", 1)
        );

        return response.getMap().values()
                .stream()
                .map(GenericRecord::new)
                .collect(Collectors.toList());
    }

    public List<GenericRecord> fetchRecordsByAccount(int accountId) throws IOException {
        GetRecordListReturn response = request(
                new GetRecordList()
                        // Data not for report, but for export
                        .withParam("is_report", false)
                        // Show last 20 record (for each operation type, if not one, see 'r_what')
                        .withParam("r_period", 8)
                        .withParam("r_what", OperationType.ALL.getValue())
                        // Include only selected = 1
                        .withParam("r_is_place", 1)
                        // Array of numeric values for place ID
                        .withParam("r_place", List.of(accountId))
                        .withParam("r_how", 1)
        );

        return response.getMap().values()
                .stream()
                .map(GenericRecord::new)
                .collect(Collectors.toList());
    }

    <R extends BaseResponse> R request(BaseFunction<R> function) throws IOException {
        return function.request(webClient);
    }

    private static int convertAmountToInt(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    /**
     * Return 100th part of the second unit.
     * So, it's guarantee a unique id within a month and int32 will be enough.
     *
     * @return 100th part of the second unit since the beginning of the current month.
     */
    private static int getTimeBasedId() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        return (int) ChronoUnit.MILLIS.between(startOfMonth, LocalDateTime.now()) / 10;
    }
}
