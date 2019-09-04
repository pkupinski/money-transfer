package com.revolut.transfers.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * Account locking manager.
 */
class AccountLocker {
    private final ConcurrentMap<Integer, Lock> accountLocks = new ConcurrentHashMap<>();

    void lock(Integer... accountIds) {
        // always lock accounts in the same order (in this case by ascending id) to avoid deadlocks
        Stream.of(accountIds)
                .forEachOrdered(a -> {
                    accountLocks.putIfAbsent(a, new ReentrantLock());
                    accountLocks.get(a).lock();
                });
    }

    void release(Integer... accountIds) {
        Stream.of(accountIds)
                .map(accountLocks::get)
                .forEach(Lock::unlock);
    }
}
