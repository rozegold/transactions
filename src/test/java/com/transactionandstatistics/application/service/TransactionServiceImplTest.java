package com.transactionandstatistics.application.service;

import com.transactionandstatistics.application.model.Statistics;
import com.transactionandstatistics.application.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    private TransactionServiceImpl subject;

    private Instant now = Instant.now();
    private long timestamp = now.toEpochMilli();
    private long timeStampSecond = now.plus(1000, ChronoUnit.MILLIS).toEpochMilli();
    private double delta = 0.01;
    private Transaction firstTransaction = new Transaction(20, timestamp);
    private Transaction secondTransaction = new Transaction(40, timestamp);
    private Transaction thirdTransaction = new Transaction(60, timeStampSecond);

    @Before
    public void setup() {
        subject = new TransactionServiceImpl();
        subject.performTransaction(firstTransaction);
        subject.performTransaction(secondTransaction);
        subject.performTransaction(thirdTransaction);
    }

    @Test
    public void shouldAggregateAndStoreSumIfTimeStampExists() {
        assertEquals(60, subject.getSumForTimestamp(timestamp), delta);
        assertEquals(60, subject.getSumForTimestamp(timeStampSecond), delta);
    }

    @Test
    public void shouldAggregateAndStoreMaxTransactionValueIfTimeStampExists() {
        assertEquals(40, subject.getMaxForTimestamp(timestamp), delta);
        assertEquals(60, subject.getMaxForTimestamp(timeStampSecond), delta);
    }

    @Test
    public void shouldAggregateAndStoreMinTransactionValueIfTimeStampExists() {
        assertEquals(20, subject.getMinForTimestamp(timestamp), delta);
        assertEquals(60, subject.getMinForTimestamp(timeStampSecond), delta);
    }

    @Test
    public void shouldAggregateAndStoreAvgTransactionValueIfTimeStampExists() {
        assertEquals(30, subject.getAvgForTimestamp(timestamp), delta);
        assertEquals(60, subject.getAvgForTimestamp(timeStampSecond), delta);
    }

    @Test
    public void shouldAggregateAndStoreCountTransactionValueIfTimeStampExists() {
        assertEquals(2, subject.getCountForTimestamp(timestamp));
        assertEquals(1, subject.getCountForTimestamp(timeStampSecond));
    }

    @Test
    public void shouldReturnCountOfAllTransactions() {
        assertEquals(3, subject.getCount());
    }

    @Test
    public void shouldReturnSumOfAllTransactions() {
        assertEquals(120, subject.getSum(), delta);
    }

    @Test
    public void shouldReturnAvgOfAllTransactions() {
        assertEquals(40, subject.getAvg(), delta);
    }

    @Test
    public void shouldReturnMaxOfAllTransactions() {
        assertEquals(60, subject.getMax(), delta);
    }

    @Test
    public void shouldReturnMinOfAllTransactions() {
        assertEquals(20, subject.getMin(), delta);
    }

    @Test
    public void shouldReturnStatistics() {
        Statistics statistics = subject.retrieveStatistics();
        assertEquals(3, statistics.getCount());
        assertEquals(120, statistics.getSum(), delta);
        assertEquals(40, statistics.getAvg(), delta);
        assertEquals(60, statistics.getMax(), delta);
        assertEquals(20, statistics.getMin(), delta);
    }

}