# CalculatorSE311
SE311 HW 4

## Description

This system is a client-rich calculator, where the client performs calculations and the server logs results.
 
## Quick Start Guide (for graders)

```
./runServer.sh
```
Then in another
```
./runClient.sh
```


## How to run the system

### 1. Compile the program

#### Note: This shouldn't be necessary as the submission should have included a pre-compiled out folder and jar files

```
./compile.sh
```

which runs:

```
javac -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
jar cfve CalculatorClient.jar edu.drexel.se311.calculator.CalculatorClient -C out .
jar cfve CalculatorServer.jar edu.drexel.se311.calculator.CalculatorServer -C out .
```

where 

```
SRC_DIR="src/main"
OUT_DIR="out"
```

### 2. Run the program

```
./runServer.sh
```
which runs 
```
java -jar CalculatorServer.jar
```
Then in another terminal:
```
./runClient.sh
```
which runs
```
java -jar CalculatorClient.jar
```

### Submissions

- UML diagrams are in UML_* in the root directory
- DSM outputs are in ./CalculatorDSM

M Score: 67.59%
Propagation Cost: 13.22%
Decoupling Level: 58.43%



