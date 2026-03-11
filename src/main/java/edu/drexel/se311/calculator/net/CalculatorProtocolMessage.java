package edu.drexel.se311.calculator.net;

import java.io.Serializable;

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

    private final Type   type;
    private final String payload;       // REQUEST carries this (result string, or ack)
    private final String errorMessage;  // ERROR carries this

    // ── REQUEST constructor (client → server) ─────────────────────────────
    public static CalculatorProtocolMessage request(String payload) {
        return new CalculatorProtocolMessage(
            Type.REQUEST, payload, null
        );
    }

    // ── RESPONSE constructor (server → client) ────────────────────────────
    public static CalculatorProtocolMessage response(String payload) {
        return new CalculatorProtocolMessage(
            Type.RESPONSE, payload, null
        );
    }

    // ── ERROR constructor (server → client) ───────────────────────────────
    public static CalculatorProtocolMessage error(String message) {
        return new CalculatorProtocolMessage(
            Type.ERROR, null, message
        );
    }

    private CalculatorProtocolMessage(Type type, String payload,
                                      String errorMessage) {
        this.type         = type;
        this.payload      = payload;
        this.errorMessage = errorMessage;
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public Type   getType()         { return type;         }
    public String getPayload()      { return payload;      }
    public String getErrorMessage() { return errorMessage; }
}