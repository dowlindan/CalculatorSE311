#!/usr/bin/env bash

# Exit immediately if a command fails
set -e
set -x

# ---- Configuration ----
SRC_DIR="src/main"
OUT_DIR="out"

# ---- Compile ----

echo "Setting up output directory..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "Compiling Java sources..."
javac -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
jar cfve CalculatorClient.jar edu.drexel.se311.calculator.CalculatorClient -C out .
jar cfve CalculatorServer.jar edu.drexel.se311.calculator.CalculatorServer -C out .

