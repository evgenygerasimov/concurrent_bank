package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
@Getter
public class BankAccount {
    private BigDecimal balance;
    private final Lock lock = new ReentrantLock();

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            BigDecimal newBalance = balance.add(amount);
            balance = newBalance;
            System.out.println("Deposit successful. New balance: " + newBalance);
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(BigDecimal amount) {
        lock.lock();
        try {
            if (balance.compareTo(amount) >= 0) {
                BigDecimal newBalance = balance.subtract(amount);
                balance = newBalance;
                System.out.println("Withdrawal successful. New balance: " + newBalance);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
}
