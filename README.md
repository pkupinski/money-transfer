# Money transfer app

### Build
```
./gradlew build
```
### Run

```
./gradlew run
```
App will be running on http://localhost:8080/

### Run All Tests

Make sure that port 8080 is free. Tests are starting the REST server automatically.

```
./gradle test --rerun-tasks
```

### Technology stack
* Java 11
* Gradle
* Spark
* Guice
* JUnit/Mockito/REST Assured


## Available Services

Before each path ```/api/1.0/``` prefix should be added.

For example use http://localhost:8080/api/1.0/accounts to get all accounts

| Method | Path                         | Usage |
| ------ | ---------------------------- | ------ |
| POST   | /transfer                    | do a transfer between accounts (transfer json should be in body) | 
| GET    | /accounts                    | return all accounts (without account balance) | 
| GET    | /accounts/{id}               | get account by id (with balance) | 
| GET    | /transactions?accountId={id} | get transactions for account with given id | 

Sample JSON format for a transfer:
```
{
	"requestId": "req123"
	"sourceAccountId": "1",
	"targetAccountId": "2",
	"amount": 12.32
}
```

## Assumptions
1. I've created a minimum set of API methods
   1. focus on transfer processing
   1. no accounts management - so I'm not handling account removal
1. Persistence is based in memory (mock implementation)
   1. account/transaction data should be stored in db probably with using Event Sourcing
   1. there are 4 build in accounts with initial balance (initial transactions)
1. No currency management
1. Instead of storing account balance I'm keeping transactions
   1. account balance is calculated from those transactions
   1. final solution should have account balance caching (with eviction when new transaction related to given account is added)
1. I'm treating transfers to same account (A -> A) as error


## TODO
1. UUID's should be used as account id's
1. Event sourcing should be used to keep transactions (my solution is too simple)
1. Application port should be configurable
1. DTO and DO objects should be used
1. As mentioned in assumptions - account's balance caching with eviction should be used (for performance improvements)
1. Logging should be improved
1. Some tests should be added (I've created tests only for major classes)
   1. Integration tests should be added (there are only unit and e2e tests)
   1. Unit tests should be added for some of the classes