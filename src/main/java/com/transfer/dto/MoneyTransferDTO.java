package com.transfer.dto;

import lombok.Data;

@Data
public class MoneyTransferDTO {
    private Integer from;
    private Integer to;
    private Long dollars;
    private Long cents;
}
