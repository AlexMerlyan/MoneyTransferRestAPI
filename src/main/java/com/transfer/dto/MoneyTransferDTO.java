package com.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyTransferDTO {
    private Integer from;
    private Integer to;
    private Long dollars;
    private Integer cents;
}
