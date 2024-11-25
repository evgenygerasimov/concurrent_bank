package org.example;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBank {
    private final ConcurrentHashMap<Integer, BankAccount> accounts = new ConcurrentHashMap<>();

    public synchronized void createAccount(int accountId, BigDecimal initialBalance) {
        accounts.put(accountId, new BankAccount(initialBalance));
    }

    public synchronized void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID");
        }
        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
        }
    }

    public BigDecimal getTotalBalance() {
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<Integer, BankAccount> entry : accounts.entrySet()) {
            BigDecimal add = total.add(entry.getValue().getBalance());
            total = add;
        }
        return total;
    }
}