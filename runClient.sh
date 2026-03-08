#!/usr/bin/env bash

# Exit immediately if a command fails
set -e
set -x
    
# ---- Run ----
echo "Running Calculator Client"
java -jar CalculatorClient.jar "$@"

# ---- Run Alternative ----
# OUT_DIR="out"
# MAIN_CLASS="edu.drexel.se311.kwic.Main"
# java -cp "$OUT_DIR" "$MAIN_CLASS" "$@"
