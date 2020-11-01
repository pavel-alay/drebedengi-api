package com.alay.drebedengi.api;

import com.alay.drebedengi.soap.functions.GetAccessStatus;
import com.alay.drebedengi.soap.functions.GetAccumList;
import com.alay.drebedengi.soap.functions.GetBalance;
import com.alay.drebedengi.soap.functions.GetCategoryList;
import com.alay.drebedengi.soap.functions.GetCurrencyList;
import com.alay.drebedengi.soap.functions.GetCurrentRevision;
import com.alay.drebedengi.soap.functions.GetPlaceList;
import com.alay.drebedengi.soap.functions.GetRecordList;
import com.alay.drebedengi.soap.functions.GetUserIdByLogin;
import com.alay.drebedengi.soap.functions.SetRecordList;
import com.alay.drebedengi.soap.responses.GetAccessStatusReturn;
import com.alay.drebedengi.soap.responses.GetAccumListReturn;
import com.alay.drebedengi.soap.responses.GetBalanceReturn;
import com.alay.drebedengi.soap.responses.GetCategoryListReturn;
import com.alay.drebedengi.soap.responses.GetCurrencyListReturn;
import com.alay.drebedengi.soap.responses.GetCurrentRevisionReturn;
import com.alay.drebedengi.soap.responses.GetRecordListReturn;
import com.alay.drebedengi.soap.responses.GetUserIdByLoginReturn;
import com.alay.drebedengi.soap.responses.SetRecordListReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DrebedengiApiRequestTest {

    private DrebedengiApi api;

    @BeforeEach
    public void setUp() {
        api = new DrebedengiApi(DrebedengiConf.builder()
                .apiKey("demo_api")
                .login("demo@example.com")
                .pass("demo")
                .build()
        );
    }

    @Test
    public void testGetAccessStatus_ok() throws IOException {
        GetAccessStatusReturn response = api.request(new GetAccessStatus());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getValue()).isEqualTo(1);
    }

    @Test
    public void testGetUserIdByLogin_ok() throws IOException {
        GetUserIdByLoginReturn response = api.request(new GetUserIdByLogin());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getValue()).isNotEmpty();
    }

    @Test
    public void testGetCurrentRevision() throws IOException {
        GetCurrentRevisionReturn response = api.request(new GetCurrentRevision());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getValue()).isNotEmpty();
    }

    @Test
    public void testGetAccessStatus_fail() {
        DrebedengiConf malformedConfig = DrebedengiConf.builder()
                .apiKey("-------")
                .login("demo@example.com")
                .pass("demo")
                .build();

        DrebedengiServerException thrown = assertThrows(DrebedengiServerException.class,
                () -> new DrebedengiApi(malformedConfig).request(new GetAccessStatus()));

        assertThat(thrown.getFailCode()).isEqualTo(50);
        assertThat(thrown.getFailMessage()).isNotEmpty().contains(malformedConfig.getApiKey());
    }

    @Test
    public void testGetRecordList() throws IOException {
        var response = api.request(
                new GetRecordList()
                        .withParam("is_report", false)// Data not for report, but for export
                        .withParam("is_show_duty", true)// Include duty records
                        .withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
                        .withParam("r_how", 1)// Show by detail, not grouped
                        .withParam("r_what", 6)// Show all operations (waste, income, moves and currency changes)
                        .withParam("r_currency", 0)// Show in original currency
                        .withParam("r_is_place", 0)// All places
                        .withParam("r_is_tag", 0)// All tags
        );

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMap()).isNotEmpty();
    }

    @Test
    public void testGetPlaceList() throws IOException {
        var response = api.request(new GetPlaceList());

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getList()).isNotEmpty();
    }


    @Test
    public void testSetRecordList() throws IOException {
        SetRecordListReturn response = api.request(new SetRecordList()
                .add(SetRecordList.itemBuilder()
                        .param("client_id", 1)
                        .param("place_id", 40032)
                        .param("budget_object_id", 40010)
                        .param("sum", -1791)
                        .param("operation_date", "2020-01-03 14:58:11")
                        .param("comment", "Some comment")
                        .param("currency_id", 17)
                        .param("is_duty", false)
                        .param("operation_type", 3)
                        .build()
                )
                .add(SetRecordList.itemBuilder()
                        .param("client_id", 2)
                        .param("place_id", 40032)
                        .param("budget_object_id", 40010)
                        .param("sum", -1700)
                        .param("operation_date", "2020-01-03 14:59:11")
                        .param("comment", "Some comment2")
                        .param("currency_id", 17)
                        .param("is_duty", false)
                        .param("operation_type", 3)
                        .build()
                )
        );

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getList()).isNotEmpty();
    }

    @Test
    @Disabled
    public void testRecordList_filterById() throws IOException {
        GetRecordListReturn response = api.request(new GetRecordList()
                .withParam("is_report", false)// Data not for report, but for export
                .withParam("is_show_duty", true)// Include duty records
                .withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
                .withParam("r_how", 1)// Show by detail, not grouped
                .withParam("r_what", 6)// Show all operations (waste, income, moves and currency changes)
                .withParam("r_currency", 0)// Show in original currency
                .withParam("r_is_place", 0)// All places
                .withParam("r_is_tag", 0)// All tags
                .withId(53133)
                .withId(53131)
        );

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMap())
                .hasSize(2)
                .containsOnlyKeys(53133, 53131);
    }

    @Test
    public void testRecordList_filterByPlaces() throws IOException {
        List<Integer> places = List.of(40040, 41439);
        GetRecordListReturn response = api.request(new GetRecordList()
                .withParam("is_report", false)// Data not for report, but for export
                .withParam("is_show_duty", true)// Include duty records
                .withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
                .withParam("r_how", 1)// Show by detail, not grouped
                .withParam("r_what", 3)// waste
                .withParam("r_currency", 0)// Show in original currency
                .withParam("r_is_place", 1)// Include only selected
                .withParam("r_is_tag", 0)// All tags
                .withParam("r_place", places)
        );

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMap().values()).allSatisfy(m -> {
            int place_id = Integer.parseInt(m.get("place_id"));
            assertThat(place_id).isIn(places);
        });
        assertThat(response.getMap())
                .hasSize(20);
    }

    @Test
    public void testGetAccumList_ok() throws IOException {
        GetAccumListReturn response = api.request(new GetAccumList());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getList()).hasSize(12);
    }

    @Test
    public void testGetBalance_ok() throws IOException {
        GetBalanceReturn response = api.request(new GetBalance()
                .withParam("restDate", "NOW")
                .withParam("is_with_accum", true)
                .withParam("is_with_duty", true));

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getList()).isNotEmpty();
    }

    @Test
    public void testGetCategoryList_ok() throws IOException {
        GetCategoryListReturn response = api.request(new GetCategoryList());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getList()).isNotEmpty();
    }

    @Test
    public void testGetCurrencyList_ok() throws IOException {
        GetCurrencyListReturn response = api.request(new GetCurrencyList());

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getList()).isNotEmpty();
    }

    @Test
    public void testGetRecordList_today() throws IOException {
        GetRecordListReturn response = api.request(new GetRecordList()
                        .withParam("is_report", false)// Data not for report, but for export
                        .withParam("is_show_duty", true)// Include duty records
                        .withParam("r_period", 7)// Show records for today (for each operation type, if not one, see 'r_what')
                        .withParam("r_how", 1)// Show by detail, not grouped
                        .withParam("r_what", 6)// Show all operations (waste, income, moves and currency changes)
                        .withParam("r_currency", 0)// Show in original currency
                        .withParam("r_is_place", 0)// All places
                        .withParam("r_is_tag", 0)// All tags
        );

        assertThat(response).isNotNull();
        assertThat(response.getFailMessage()).isNullOrEmpty();
        assertThat(response.getFailCode()).isNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMap()).isNotEmpty();
    }
}
