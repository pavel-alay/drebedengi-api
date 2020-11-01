package com.alay.drebedengi.soap.responses;

import com.alay.drebedengi.api.DrebedengiServerException;
import com.alay.drebedengi.soap.responses.base.BaseResponse;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReturnTest {

    private String readFile(String xml) throws IOException {
        String resources = "./src/test/resources/";
        return IOUtils.toString(new FileInputStream(resources + xml), StandardCharsets.UTF_8);
    }

    @Test
    public void testFault() throws IOException {
        String xml = "fault.xml";
        DrebedengiServerException thrown = assertThrows(DrebedengiServerException.class,
                () -> new GetAccessStatusReturn().init(readFile(xml)));

        assertThat(thrown.getFailCode()).isEqualTo(50);
        assertThat(thrown.getFailMessage()).isEqualTo("Login failed. API ID '----' is invalid");
    }

    @Test
    public void testGetAccessStatusResponse() throws IOException {
        String xml = "getAccessStatusResponse.xml";
        GetAccessStatusReturn getAccessStatusReturn = new GetAccessStatusReturn()
                .init(readFile(xml));

        assertSuccessStatus(getAccessStatusReturn);
        assertThat(getAccessStatusReturn.getValue()).isEqualTo(1);
    }

    @Test
    public void testGetRecordListResponse() throws IOException {
        String xml = "getRecordListResponse.xml";
        GetRecordListReturn getRecordListReturn = new GetRecordListReturn()
                .init(readFile(xml));

        int recordListSize = 21;
        assertSuccessStatus(getRecordListReturn);
        assertThat(getRecordListReturn.getMap()).hasSize(recordListSize);
    }

    @Test
    public void testSetRecordListResponse() throws IOException {
        String xml = "setRecordListResponse.xml";
        SetRecordListReturn setRecordListReturn = new SetRecordListReturn()
                .init(readFile(xml));

        int recordListSize = 2;
        assertSuccessStatus(setRecordListReturn);
        assertThat(setRecordListReturn.getList()).hasSize(recordListSize);
    }

    @Test
    public void testGetUserIdByLoginResponse() throws IOException {
        String xml = "getUserIdByLoginResponse.xml";
        GetUserIdByLoginReturn getUserIdByLoginReturn = new GetUserIdByLoginReturn()
                .init(readFile(xml));

        assertSuccessStatus(getUserIdByLoginReturn);
        assertThat(getUserIdByLoginReturn.getValue()).isEqualTo("1000000000539");
    }

    @Test
    public void testGetAccumListResponse() throws IOException {
        String xml = "getAccumListResponse.xml";
        GetAccumListReturn getAccumListReturn = new GetAccumListReturn()
                .init(readFile(xml));

        int recordListSize = 12;
        assertSuccessStatus(getAccumListReturn);
        assertThat(getAccumListReturn.getList()).hasSize(recordListSize);
    }

    @Test
    public void testGetBalanceResponse() throws IOException {
        String xml = "getBalanceResponse.xml";
        GetBalanceReturn getBalanceReturn = new GetBalanceReturn()
                .init(readFile(xml));

        int recordListSize = 7;
        assertSuccessStatus(getBalanceReturn);
        assertThat(getBalanceReturn.getList()).hasSize(recordListSize);
    }

    @Test
    public void testGetCategoryListResponse() throws IOException {
        String xml = "getCategoryListResponse.xml";
        GetCategoryListReturn getCategoryListReturn = new GetCategoryListReturn()
                .init(readFile(xml));

        int recordListSize = 36;
        assertSuccessStatus(getCategoryListReturn);
        assertThat(getCategoryListReturn.getList()).hasSize(recordListSize);
    }

    @Test
    public void testGetCurrencyListResponse() throws IOException {
        String xml = "getCurrencyListResponse.xml";
        GetCurrencyListReturn getCategoryListReturn = new GetCurrencyListReturn()
                .init(readFile(xml));

        int recordListSize = 4;
        assertSuccessStatus(getCategoryListReturn);
        assertThat(getCategoryListReturn.getList()).hasSize(recordListSize);
    }

    @SuppressWarnings("rawtypes")
    private void assertSuccessStatus(BaseResponse bxr) {
        assertThat(bxr.isSuccess()).isTrue();
    }
}
