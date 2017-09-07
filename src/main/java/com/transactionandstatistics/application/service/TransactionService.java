package com.transactionandstatistics.application.service;

import com.transactionandstatistics.application.model.Statistics;
import com.transactionandstatistics.application.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public interface TransactionService {

    HttpStatus performTransaction(Transaction transaction);

    Statistics retrieveStatistics();
}
