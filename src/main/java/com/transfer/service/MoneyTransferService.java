package com.transfer.service;

import com.transfer.dto.MoneyTransferDTO;
import com.transfer.dto.ResponseDTO;

public interface MoneyTransferService {

    ResponseDTO transfer(MoneyTransferDTO dto);

}
