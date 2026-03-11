#!/usr/bin/env bash

# Exit immediately if a command fails
set -e
set -x
    
# ---- Run ----
echo "Running KWIC Server"
java -jar CalculatorServer.jar "$@"

# ---- Run Alternative ----
# OUT_DIR="out"
# MAIN_CLASS="edu.drexel.se311.kwic.Main"
# java -cp "$OUT_DIR" "$MAIN_CLASS" "$@"
