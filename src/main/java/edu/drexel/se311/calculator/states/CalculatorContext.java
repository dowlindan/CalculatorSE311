package edu.drexel.se311.calculator.states;

/**
 * Holds all shared mutable data for the state machine and owns
 * the current-state reference.
 *
 * The GUI creates one CalculatorContext and forwards every button
 * press to it.  The context delegates to the current state, which
 * may update the stored values and/or call transitionTo() to switch
 * to a new state.
 */
public class CalculatorContext {

    // ── Stored operands & pending operator ───────────────────────────────
    private double accumulator = 0;   // result built up so far (left-hand side)
    private double currentInput = 0;  // number being typed right now
    private char   pendingAddSub = 0; // + or -  waiting to be applied  (0 = none)
    private char   pendingMulDiv = 0; // * or /  waiting to be applied  (0 = none)

    // ── State machine ─────────────────────────────────────────────────────
    private CalculatorState currentState;

    // ── Singleton state instances (stateless, so one each is fine) ────────
    public final CalculatorState startState           = new StartState();
    public final CalculatorState gettingFirstOperand  = new GettingFirstOperandState();
    public final CalculatorState waitingForAddSub     = new WaitingForAddSubOperandState();
    public final CalculatorState gettingAddSub        = new GettingAddSubOperandState();
    public final CalculatorState waitingForMulDiv     = new WaitingForMulDivOperandState();
    public final CalculatorState gettingMulDiv        = new GettingMulDivOperandState();
    public final CalculatorState calculateState       = new CalculateState();

    public CalculatorContext() {
        currentState = startState;
    }

    // ── Transition ────────────────────────────────────────────────────────
    public void transitionTo(CalculatorState next) {
        currentState = next;
    }

    // ── Button-press entry points (called by the GUI) ─────────────────────
    public void onDigit  (int digit) { currentState.onDigit  (this, digit); }
    public void onAddSub (char op)   { currentState.onAddSub (this, op);    }
    public void onMulDiv (char op)   { currentState.onMulDiv (this, op);    }
    public void onEquals ()          { currentState.onEquals (this);        }
    public void onClear  ()          { currentState.onClear  (this);        }

    // ── Accessors ─────────────────────────────────────────────────────────
    public double getAccumulator()              { return accumulator; }
    public void   setAccumulator(double v)      { accumulator = v; }

    public double getCurrentInput()             { return currentInput; }
    public void   setCurrentInput(double v)     { currentInput = v; }

    public char   getPendingAddSub()            { return pendingAddSub; }
    public void   setPendingAddSub(char op)     { pendingAddSub = op; }

    public char   getPendingMulDiv()            { return pendingMulDiv; }
    public void   setPendingMulDiv(char op)     { pendingMulDiv = op; }

    /** Full reset — called by C or StartState */
    public void reset() {
        accumulator   = 0;
        currentInput  = 0;
        pendingAddSub = 0;
        pendingMulDiv = 0;
        transitionTo(startState);
    }
}