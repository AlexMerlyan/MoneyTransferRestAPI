package com.transfer.dao;

import com.transfer.model.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountDaoImpl implements AccountDao {

    private static final Map<Integer, Account> ACCOUNTS = new HashMap<>();
    private volatile int idCounter;

    @Override
    public Account getAccountById(Integer id) {
        return ACCOUNTS.get(id);
    }

    @Override
    public void saveAccount(Account account) {
        if (account.getId() == null) {
            account.setId(generateId());
        }
        ACCOUNTS.put(account.getId(), account);
    }

    @Override
    public void removeAccount(Integer id) {
        ACCOUNTS.remove(id);
    }

    private synchronized Integer generateId() {
        return ++idCounter;
    }
}
