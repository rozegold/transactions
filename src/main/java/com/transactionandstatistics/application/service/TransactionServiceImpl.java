package com.transactionandstatistics.application.service;

import com.transactionandstatistics.application.model.Statistics;
import com.transactionandstatistics.application.model.Transaction;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionServiceImpl implements TransactionService {

    private static final int DURATION = 60;
    private Map<Long, Statistics> transactionMap;

    public TransactionServiceImpl() {
        this.transactionMap = new PassiveExpiringMap<>(DURATION, TimeUnit.SECONDS);
    }

    public TransactionServiceImpl(int duration) {
        this.transactionMap = new PassiveExpiringMap<>(duration, TimeUnit.SECONDS);
    }

    @Override
    public HttpStatus performTransaction(Transaction transaction) {
        if (null == transaction || transaction.getTimestamp() < Instant.now().minus(DURATION, ChronoUnit.SECONDS).toEpochMilli())
            return HttpStatus.NO_CONTENT;

        double amount = transaction.getAmount();

        synchronized (this) {
            if (transactionMap.containsKey(transaction.getTimestamp())) {
                updateStatistics(transaction);
            } else {
                transactionMap.put(transaction.getTimestamp(), new Statistics(amount, amount, amount, 1, amount));
            }
            return HttpStatus.CREATED;
        }
    }

    @Override
    public Statistics retrieveStatistics() {
        if (isTransactionEmpty())
            return new Statistics();
        synchronized (this) {
            return new Statistics(getSum(), getMax(), getMin(), getCount(), getAvg());
        }
    }

    private boolean isTransactionEmpty() {
        return (null == transactionMap || transactionMap.isEmpty());
    }

    private void updateStatistics(Transaction transaction) {
        Statistics statistics = transactionMap.get(transaction.getTimestamp());

        double amount = transaction.getAmount();
        double currMin = statistics.getMin();
        double currMax = statistics.getMax();

        statistics.setSum(statistics.getSum() + amount);
        statistics.setCount(statistics.getCount() + 1);
        statistics.setAvg(statistics.getSum() / statistics.getCount());
        statistics.setMax(currMax < amount ? amount : currMax);
        statistics.setMin(currMin > amount ? amount : currMin);

        transactionMap.put(transaction.getTimestamp(), statistics);
    }

    double getSumForTimestamp(long timestamp) {

        return transactionMap.get(timestamp).getSum();
    }

    double getMaxForTimestamp(long timestamp) {
        return transactionMap.get(timestamp).getMax();
    }

    double getMinForTimestamp(long timestamp) {
        return transactionMap.get(timestamp).getMin();
    }

    double getAvgForTimestamp(long timeStamp) {
        return transactionMap.get(timeStamp).getAvg();
    }

    long getCountForTimestamp(long timestamp) {
        return transactionMap.get(timestamp).getCount();
    }

    double getSum() {
        return transactionMap.values().stream().mapToDouble(Statistics::getSum).sum();
    }

    double getAvg() {
        return getSum() / getCount();
    }

    long getCount() {
        return transactionMap.values().stream().mapToLong(Statistics::getCount).sum();
    }


    double getMax() {
        return transactionMap.values().stream().mapToDouble(Statistics::getMax).max().getAsDouble();
    }

    double getMin() {
        return transactionMap.values().stream().mapToDouble(Statistics::getMin).min().getAsDouble();
    }
}
