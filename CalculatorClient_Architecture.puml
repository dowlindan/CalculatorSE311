@startuml CalculatorClient_Architecture
skinparam classBackgroundColor #FEFEFE
skinparam classBorderColor #333333
skinparam classArrowColor #333333

' =====================================================================
' GUI COMPONENT
' =====================================================================
class CalculatorClient {
    - display: JTextField
    - {static} BG: Color
    - {static} BTN_FACE: Color
    - {static} BTN_HOVER: Color
    - {static} BTN_PRESS: Color
    - {static} BTN_TEXT: Color
    - {static} BORDER_COLOR: Color
    - {static} BUTTON_LABELS: String[][]

    + CalculatorClient()
    - routeInput(context: CalculatorContext, label: String): void
    - makeButton(label: String): JButton
    + setVisible(visible: boolean): void
}

' =====================================================================
' STATE PATTERN - Core State Management
' =====================================================================

interface CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

note right of CalculatorState
  STATE PATTERN - State Interface

  Defines operations for each input type.
  Implementations decide how to respond
  based on their specific state behavior.
end note

class StartState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class GettingFirstOperandState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class WaitingForAddSubOperandState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class GettingAddSubOperandState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class WaitingForMulDivOperandState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class GettingMulDivOperandState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}

class CalculateState implements CalculatorState {
    + onDigit(ctx: CalculatorContext, digit: int): void
    + onAddSub(ctx: CalculatorContext, op: char): void
    + onMulDiv(ctx: CalculatorContext, op: char): void
    + onEquals(ctx: CalculatorContext): void
    + onClear(ctx: CalculatorContext): void
}



' =====================================================================
' EXPRESSION ENGINE
' =====================================================================

class ExpressionEngine {
    - currentNumber: int
    - operands: List~Integer~
    - operators: List~Character~
    - inputActive: boolean

    + ExpressionEngine()
    + appendDigit(digit: int): void
    + resetCurrentNumber(): void
    + setCurrentNumber(value: int): void
    + setPendingOp(op: char): void
    + getPendingOp(): char
    + getLeftOperand(): ExpressionNode
    + setLeftOperand(node: ExpressionNode): void
    + storeLeftOperand(newOp: char): void
    + storeLeftOperand(): void
    + evaluate(): ExpressionNode
    + currentExpressionString(): String
    + reset(): void

    - {static} precedence(op: char): int
    - buildTreeWithPrecedence(nodes: ExpressionNode): ExpressionNode
}

note right of ExpressionEngine
  Tracks expression state and builds
  ExpressionNode trees with proper
  operator precedence.

  Does NOT perform arithmetic -
  delegates to EvaluatorVisitor.
end note

' =====================================================================
' OBSERVER PATTERN - Display & Network Updates
' =====================================================================

interface CalculatorObserver {
    + onDisplayUpdate(text: String): void
    + onResultReady(result: int): void
    + onError(message: String): void
}

note right of CalculatorObserver
  OBSERVER PATTERN - Observer Interface

  Defines callbacks for calculation results
  and errors. Multiple observers can listen
  to the same subject.
end note

class DisplayObserver implements CalculatorObserver {
    - display: JTextField

    + DisplayObserver(display: JTextField)
    + onDisplayUpdate(text: String): void
    + onResultReady(result: int): void
    + onError(message: String): void
}

class ServerConnection implements CalculatorObserver {
    - host: String
    - port: int

    + ServerConnection(host: String, port: int)
    + onDisplayUpdate(text: String): void
    + onResultReady(result: int): void
    + onError(message: String): void
    - sendToServer(message: CalculatorProtocolMessage): void
}

' =====================================================================
' CALCULATOR CONTEXT - Subject & Coordinator
' =====================================================================

class CalculatorContext {
    - engine: ExpressionEngine
    - currentState: CalculatorState
    - observers: List~CalculatorObserver~

    + CalculatorContext()
    + addObserver(o: CalculatorObserver): void
    + appendDigit(digit: int): void
    + resetCurrentNumber(): void
    + setCurrentNumber(value: int): void
    + setPendingOp(op: char): void
    + getPendingOp(): char
    + getLeftOperand(): ExpressionNode
    + setLeftOperand(n: ExpressionNode): void
    + storeLeftOperand(op: char): void
    + submitEquals(): void
    + transitionTo(next: CalculatorState): void
    + onDigit(digit: int): void
    + onAddSub(op: char): void
    + onMulDiv(op: char): void
    + onEquals(): void
    + onClear(): void
    + toString(): String
    + getCurrentExpression(): String
    + reset(): void
    - notifyDisplayUpdate(): void
    - notifyResultReady(result: int): void
    - notifyError(message: String): void
}

note right of CalculatorContext
  STATE PATTERN - Context Role
  OBSERVER PATTERN - Subject

  Acts as central coordinator:
  - Directly manages current state
  - Delegates to state for input
  - Notifies observers of results
  - Manages expression engine
end note

' =====================================================================
' COMPOSITE PATTERN - Expression Trees
' =====================================================================


interface ExpressionVisitor {
    + visitNumber(node: NumberNode): T
    + visitBinary(node: BinaryOperationNode): T
}

interface ExpressionNode {
    + accept(visitor: ExpressionVisitor): T
}

class NumberNode implements ExpressionNode {
    - value: int

    + NumberNode(value: int)
    + accept(visitor: ExpressionVisitor): T
    + getValue(): int
    + toString(): String
}

note right of NumberNode
  COMPOSITE PATTERN - Leaf

  Represents a terminal operand.
  Base case for recursive visitor.
end note

class BinaryOperationNode implements ExpressionNode {
    - left: ExpressionNode
    - operator: char
    - right: ExpressionNode

    + BinaryOperationNode(left: ExpressionNode, operator: char, right: ExpressionNode)
    + accept(visitor: ExpressionVisitor): T
    + getLeft(): ExpressionNode
    + getRight(): ExpressionNode
    + getOperator(): char
    + toString(): String
}

note right of BinaryOperationNode
  COMPOSITE PATTERN - Composite

  Represents a binary operation node.
  Both children are ExpressionNode subtrees.
end note

' =====================================================================
' BUILDER PATTERN - Factory for Expressions
' =====================================================================

class ExpressionBuilder {
    - ExpressionBuilder()

    + {static} number(value: int): NumberNode
    + {static} binary(left: int, operator: char, right: int): BinaryOperationNode
    + {static} binary(left: ExpressionNode, operator: char, right: int): BinaryOperationNode
    + {static} binary(left: ExpressionNode, operator: char, right: ExpressionNode): BinaryOperationNode
}

note right of ExpressionBuilder
  BUILDER PATTERN - Builder

  Factory for creating expression trees.
  Encapsulates tree construction logic.
end note

' =====================================================================
' VISITOR PATTERN - Tree Evaluation
' =====================================================================

note right of ExpressionVisitor
  VISITOR PATTERN - Visitor Interface

  Defines visit methods for each
  expression node type.
end note

class EvaluatorVisitor {
    + EvaluatorVisitor()
    + visitNumber(node: NumberNode): Integer
    + visitBinary(node: BinaryOperationNode): Integer
}

note right of EvaluatorVisitor
  VISITOR PATTERN - Concrete Visitor

  Implements arithmetic evaluation.
  Recursively evaluates expression trees.

  ONLY class that performs calculations.
end note

EvaluatorVisitor ..|> ExpressionVisitor

' =====================================================================
' RELATIONSHIPS
' =====================================================================

CalculatorClient --> CalculatorContext : creates & routes to
CalculatorClient --> DisplayObserver : creates
CalculatorClient --> ServerConnection : creates

CalculatorContext --> "1" ExpressionEngine : owns
CalculatorContext --> CalculatorState : delegates to

CalculatorState o-- CalculatorContext : modifies via

ExpressionEngine --> ExpressionBuilder : uses
ExpressionEngine --> ExpressionNode : builds

ExpressionNode <|-- NumberNode
ExpressionNode <|-- BinaryOperationNode

NumberNode --> ExpressionNode : returns in accept
BinaryOperationNode --> ExpressionNode : has left/right children

ExpressionNode --> ExpressionVisitor : accepts
CalculatorContext --> EvaluatorVisitor : uses in submitEquals

CalculatorContext --> CalculatorObserver : notifies
CalculatorObserver <|-- DisplayObserver
CalculatorObserver <|-- ServerConnection

' =====================================================================
' PATTERN LEGEND
' =====================================================================

note as Legend
  KEY DESIGN PATTERNS:

  1. STATE PATTERN
     Context: CalculatorStateMachine
     State Interface: CalculatorState
     Concrete States: StartState, GettingFirstOperandState, etc.
     Role: Encapsulates behavior by state; enables state transitions

  2. OBSERVER PATTERN
     Subject: CalculatorContext
     Observer Interface: CalculatorObserver
     Concrete Observers: DisplayObserver, ServerConnection
     Role: Decouples calculation from UI/network updates

  3. VISITOR PATTERN
     Visitor Interface: ExpressionVisitor<T>
     Concrete Visitor: EvaluatorVisitor (arithmetic evaluation)
     Element: ExpressionNode
     Role: Separates tree operations from structure

  4. COMPOSITE PATTERN
     Component: ExpressionNode
     Leaf: NumberNode (terminal value)
     Composite: BinaryOperationNode (branch with operator and children)
     Role: Builds recursive tree structures for expressions

  5. BUILDER PATTERN
     Builder/Factory: ExpressionBuilder
     Role: Encapsulates tree construction
end note

@enduml
