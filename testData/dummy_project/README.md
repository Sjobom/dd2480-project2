# DD2480-project-1
This repo contains source code and tests for the Launch Interceptor Program. 
This is the work of Group 5 in the KTH course DD2480 (2018). 

Group members:
- Anders Sjöbom
- Albin Remnestål
- Johan Bergdorf
- Marcus Wengelin
- Robert Wörlund
## How-to

### gradle
To create a gradle wrapper, compile the code and run the main method
```
$ gradle wrapper # to create generate gradle/wrapper/*
$ ./gradlew run
```
To run the test-cases associated with the program, run:
```
$ ./gradlew test
```
## Statement of Contributions

#### Anders
cmv6, cmv10, cmv14, PUM, documentation, DECIDE test, algebra-tests, generating input data

#### Marcus
cmv3, cmv4, cmv5, FUV, gradle setup, documentation

#### Johan
cmv0, cmv1, cmv8, cmv13, generating input data, DECIDE test, PUM

#### Albin
cmv2, cmv7, cmv9, refactoring, documentation, gradle setup, JUnit setup

#### Robert
cmv11, cmv12, CMV creation, initial setup of classes (code skeleton), documentation

## Structure
- The source code is divided into classes for the different steps in the DECIDE algorithm.
- There is an algebra class that contains methods useful for algebraic operations.
- All tests reside in the TestRunner class.
- A parser was developed to parse files with input data for the DECIDE algorithm. This helps for testing purposes and enables the program to be used in an actual production environment.

## Notes
- We have called the methods that compute the Launch Interceptor Conditions (LICs) 
for CMV# since each of them calculate a value for the Conditions Met Vector (CMV).

