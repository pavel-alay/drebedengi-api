package com.alay.drebedengi.api.operations;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class Exchange {
    private BigDecimal boughtSum;
    private int boughtCurrencyId;

    private BigDecimal soldSum;
    private int soldCurrencyId;

    private int accountId;
    @Builder.Default
    private String comment = "";
    private LocalDateTime date;
}
