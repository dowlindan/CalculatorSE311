package edu.drexel.se311.calculator.net;

import java.io.Serializable;

import edu.drexel.se311.calculator.expressions.ExpressionNode;

/**
 * PROTOCOL MESSAGE
 *
 * The object sent over the socket between client and server.
 * Wraps an ExpressionNode tree with an optional request ID for
 * tracing and a timestamp for logging.
 *
 * Both sides of the connection work only with this type —
 * neither touches raw strings or bare ExpressionNode objects
 * directly on the wire.
 */
public class CalculatorProtocolMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type { REQUEST, RESPONSE, ERROR }

    private final Type           type;
    private final ExpressionNode expression;   // REQUEST carries this
    private final double         result;        // RESPONSE carries this
    private final String         errorMessage;  // ERROR carries this

    // ── REQUEST constructor (client → server) ─────────────────────────────
    public static CalculatorProtocolMessage request(ExpressionNode expression) {
        return new CalculatorProtocolMessage(
            Type.REQUEST, expression, 0, null
        );
    }

    // ── RESPONSE constructor (server → client) ────────────────────────────
    public static CalculatorProtocolMessage response(double result, String requestId) {
        return new CalculatorProtocolMessage(
            Type.RESPONSE, null, result, null
        );
    }

    // ── ERROR constructor (server → client) ───────────────────────────────
    public static CalculatorProtocolMessage error(String message) {
        return new CalculatorProtocolMessage(
            Type.ERROR, null, 0, message
        );
    }

    private CalculatorProtocolMessage(Type type, ExpressionNode expression,
                                      double result, String errorMessage) {
        this.type         = type;
        this.expression   = expression;
        this.result       = result;
        this.errorMessage = errorMessage;

    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public Type           getType()         { return type;         }
    public ExpressionNode getExpression()   { return expression;   }
    public double         getResult()       { return result;       }
    public String         getErrorMessage() { return errorMessage; }
}