package com.transfer.dao;

import com.transfer.model.Account;

public interface AccountDao {

    Account getAccountById(Integer id);

    void saveAccount(Account account);

    void removeAccount(Integer id);

}
