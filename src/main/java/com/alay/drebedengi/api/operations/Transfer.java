package com.alay.drebedengi.api.operations;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class Transfer {
    private BigDecimal sum;
    private int currencyId;

    private int fromAccountId;
    private int toAccountId;

    @Builder.Default
    private String comment = "";
    private LocalDateTime date;
}
