package org.example;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBank {
    private final ConcurrentHashMap<Integer, BankAccount> accounts = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    public void createAccount(int accountId, BigDecimal initialBalance) {
        accounts.put(accountId, new BankAccount(initialBalance));
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        lock.lock();
        try {
            BankAccount fromAccount = accounts.get(fromAccountId);
            BankAccount toAccount = accounts.get(toAccountId);

            if (fromAccount == null || toAccount == null) {
                throw new IllegalArgumentException("Invalid account ID");
            }
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    if (fromAccount.withdraw(amount)) {
                        toAccount.deposit(amount);
                    }
                }
            }
        } finally {
            lock.unlock();
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