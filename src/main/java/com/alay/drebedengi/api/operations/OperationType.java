package com.alay.drebedengi.api.operations;

import lombok.Getter;

/**
 * // [income = 2, waste = 3 (default), move = 4, change = 5, all types = 6]
 */
public enum OperationType {
    CREDIT(2),
    DEBIT(3),
    TRANSFER(4),
    EXCHANGE(5),
    ALL(6);

    @Getter
    private final int value;

    OperationType(int value){
        this.value = value;
    }

    public static OperationType parse(int x) {
        switch(x) {
            case 2:
                return CREDIT;
            case 3:
                return DEBIT;
            case 4:
                return TRANSFER;
            case 5:
                return EXCHANGE;
            case 6:
                return ALL;
        }
        throw new IllegalArgumentException("Unknown operation type");
    }
}
