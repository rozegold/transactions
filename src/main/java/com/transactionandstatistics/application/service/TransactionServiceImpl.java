package com.transactionandstatistics.application.service;

import com.transactionandstatistics.application.model.Statistics;
import com.transactionandstatistics.application.model.Transaction;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionServiceImpl implements TransactionService {

    private Map<Long, Statistics> transactionMap;

    public TransactionServiceImpl() {
        this.transactionMap = new PassiveExpiringMap<>(60, TimeUnit.SECONDS);
    }


    @Override
    public HttpStatus performTransaction(Transaction transaction) {
        if (null == transaction || transaction.getTimestamp() < System.currentTimeMillis() - 60000)
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
        return new Statistics(getSum(), getMax(), getMin(), getCount(), getAvg());
    }

    private boolean isTransactionEmpty() {
        if (null == transactionMap || transactionMap.isEmpty())
            return true;
        return false;
    }

    private void updateStatistics(Transaction transaction) {
        double amount = transaction.getAmount();
        Statistics statistics = transactionMap.get(transaction.getTimestamp());
        statistics.setSum(statistics.getSum() + amount);
        statistics.setCount(statistics.getCount() + 1);
        statistics.setAvg(statistics.getSum() / statistics.getCount());
        if (statistics.getMax() < amount) {
            statistics.setMax(amount);
        }
        if (statistics.getMin() > amount) {
            statistics.setMin(amount);
        }

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
        double sum = 0;

        for (Statistics statistics : transactionMap.values()) {
            sum += statistics.getSum();
        }
        return sum;
    }

    double getAvg() {
        long count = getCount();
        if (0 == count)
            return 0;
        return getSum() / count;
    }

    long getCount() {
        long count = 0;
        for (Statistics statistics : transactionMap.values()) {
            count += statistics.getCount();
        }
        return count;
    }


    double getMax() {
        double max = Integer.MIN_VALUE;

        for (Statistics statistics : transactionMap.values()) {
            if (max < statistics.getMax()) {
                max = statistics.getMax();
            }
        }
        return max;
    }

    double getMin() {
        double min = Integer.MAX_VALUE;
        for (Statistics statistics : transactionMap.values()) {
            if (min > statistics.getMin()) {
                min = statistics.getMin();
            }
        }
        return min;
    }
}
