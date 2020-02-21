package com.transfer.dao;

import com.transfer.model.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountDaoImpl implements AccountDao {

    private final Map<Integer, Account> accounts = new HashMap<>();
    private int idCounter;

    @Override
    public Account getAccountById(Integer id) {
        return accounts.get(id);
    }

    @Override
    public void saveAccount(Account account) {
        if (account.getId() == null) {
            account.setId(++idCounter);
        }
        accounts.put(account.getId(), account);
    }

    @Override
    public void removeAccount(Integer id) {
        accounts.remove(id);
    }

}
