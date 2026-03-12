#!/usr/bin/env bash

# Exit immediately if a command fails
set -e
set -x
    
# ---- Run ----
echo "Running KWIC Server"
java -jar CalculatorServer.jar

