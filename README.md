A Spring-boot application which has a transaction API with two end points as mentioned below:

1. POST : /transactions : 
for each new transaction a new entry is created in the transactionMap
where key : timestamp and value : Statistics.

For each transaction with a repeated timestamp the statistics will be aggregated and updated for that particular timestamp.

2. GET : /statistics :
This will make a synchronized call to get all the aggregated statistics of all timestamps present in the transactionMap.
Statistics object will be returned which has sum,min,max,count and avg of the last 60 seconds of the transactions.


DataStructure : 

* PassiveExpiringMap : it allows us to define an ExpirationPolicy(expiration time value) to determine how long the entry should remain alive which here I have defined as 60 seconds.
When accessing the mapped value for a key, its expiration time is checked, and if it is a negative value or if it is greater than the current time, the mapped value is returned. 
Otherwise, the key is removed from the map, and null is returned.
* PassiveExpiringMap is not thread safe by default -so the map is manually synchronized when iterating over its collection for ensuring thread safety
* Key : timestamp of the transaction : which will ensure that the GET api will result in having O(k) complexity, 
where k is constant and can not be more than 60000 (in 60 seconds aggregated transactions can be 60000 only)                         

** Deployment Steps :
1. mvn spring-boot:run -Dserver.port=<port>


** Running Test Cases :
1. TransactionControllerTest.java : Run all the test cases
2. TransactionServiceImplTest : Run all the test cases



