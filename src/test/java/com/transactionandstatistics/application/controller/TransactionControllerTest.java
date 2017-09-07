package com.transactionandstatistics.application.controller;

import com.transactionandstatistics.application.model.Statistics;
import com.transactionandstatistics.application.model.Transaction;
import com.transactionandstatistics.application.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private TransactionController subject;

    @Mock
    private TransactionService service;

    private Statistics statistics = new Statistics(10,10,10,1,10);
    private Transaction firstTransaction = new Transaction(10, Instant.now().toEpochMilli());
    private Transaction secondTransaction = new Transaction(20, Instant.now().toEpochMilli());
    private Transaction thirdTransaction = new Transaction(30, Instant.now().toEpochMilli()+60000);

    @Before
    public void setup(){
        subject = new TransactionController(service);

        when(service.performTransaction(firstTransaction)).thenReturn(HttpStatus.CREATED);
        when(service.performTransaction(secondTransaction)).thenReturn(HttpStatus.CREATED);
        when(service.performTransaction(thirdTransaction)).thenReturn(HttpStatus.NO_CONTENT);

        when(service.retrieveStatistics()).thenReturn(statistics);
    }

    @Test
    public void testTransaction(){

        assertEquals(HttpStatus.CREATED,subject.transaction(firstTransaction));
        assertEquals(HttpStatus.CREATED,subject.transaction(secondTransaction));
        assertEquals(HttpStatus.NO_CONTENT,subject.transaction(thirdTransaction));
    }


    @Test
    public void testStatistics(){
        assertEquals(statistics, subject.statistics());

    }



}