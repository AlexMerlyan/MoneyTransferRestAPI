package com.transfer.service;

import com.transfer.dao.AccountDao;
import com.transfer.dto.MoneyTransferDTO;
import com.transfer.dto.ResponseDTO;
import com.transfer.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private static final String ACCOUNTS_NOT_EXISTS = "Accounts with ids %s does not exist";
    private static final String NOT_ENOUGH_MONEY = "Not enough money on account with id %d";
    private static final String SUCCESS_TRANSFER = "Transfer was finished successfully";

    private AccountDao accountDao;

    @Override
    public synchronized ResponseDTO transfer(MoneyTransferDTO dto) {
        Account fromAccount = accountDao.getAccountById(dto.getFrom());
        Account toAccount = accountDao.getAccountById(dto.getTo());
        if (fromAccount == null || toAccount == null) {
            String message = generateMessage(dto, fromAccount, toAccount);
            return createResponse(message, false);
        }
        if (isNotEnoughMoney(fromAccount, dto)) {
            return createResponse(String.format(NOT_ENOUGH_MONEY, dto.getFrom()), false);
        }

        makeTransfer(fromAccount, toAccount, dto.getDollars(), dto.getCents());
        return createResponse(SUCCESS_TRANSFER, true);
    }

    private void makeTransfer(Account from, Account to, Long dollars, Long cents) {
        from.setDollars(from.getDollars() - dollars);
        from.setCents(from.getCents() - cents);
        to.setDollars(to.getDollars() + dollars);
        to.setCents(to.getCents() + cents);
        accountDao.saveAccount(from);
        accountDao.saveAccount(to);
    }

    private boolean isNotEnoughMoney(Account account, MoneyTransferDTO dto) {
        return account.getDollars() < dto.getDollars()
                || (account.getDollars() == dto.getDollars() && account.getCents() < dto.getCents());
    }

    private String generateMessage(MoneyTransferDTO dto, Account fromAccount, Account toAccount) {
        String arguments;
        if (fromAccount == null && toAccount == null) {
            arguments = dto.getFrom() + ", " + dto.getTo();
        } else if (fromAccount == null) {
            arguments = String.valueOf(dto.getFrom());
        } else {
            arguments = String.valueOf(dto.getTo());
        }
        return String.format(ACCOUNTS_NOT_EXISTS, arguments);
    }

    private ResponseDTO createResponse(String message, boolean isSuccessful) {
        return new ResponseDTO(message, isSuccessful);
    }
}
