package org.example;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentBank bank = new ConcurrentBank();

        bank.createAccount(1, new BigDecimal(100000));
        bank.createAccount(2, new BigDecimal(200000));

        Thread t1 = new Thread(() -> bank.transfer(1, 2, new BigDecimal(500)));
        Thread t2 = new Thread(() -> bank.transfer(2, 1, new BigDecimal(300)));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}