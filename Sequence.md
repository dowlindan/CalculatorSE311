@startuml CalculatorSequence_US3

skinparam sequence {
  ArrowColor DarkBlue
  ActorBorderColor DarkBlue
  LifeLineBorderColor DarkBlue
  LifeLineBackgroundColor LightBlue
  ParticipantBorderColor DarkBlue
  ParticipantBackgroundColor LightBlue
  ParticipantFontName Arial
  BoxBorderColor DarkBlue
  NoteBackgroundColor LightYellow
  NoteBorderColor DarkBlue
}

title User Story 3: Perform Arithmetic Calculation\n4 + 5 * 6 = (displaying 34)

participant User
participant CalculatorClient as GUI
participant CalculatorContext as Context
participant CalculatorState as State
participant ExpressionEngine as Engine
participant EvaluatorVisitor as Evaluator
participant DisplayObserver as Display
participant ServerConnection as Network
participant CalculatorServer as Server

autonumber

' =====================================================================
' INITIALIZATION
' =====================================================================

User -> GUI : Launch calculator
GUI -> Context : new CalculatorContext()
activate Context
  Context -> Engine : new ExpressionEngine()
  activate Engine
  Engine --> Context : engine
  Context -> State : transitionTo(StartState)
  activate State
  State --> Context : void
GUI -> Display : new DisplayObserver(textField)
activate Display
Display --> GUI : displayObserver
GUI -> Network : new ServerConnection(host, port)
activate Network
Network --> GUI : serverConnection
Context -> Display : addObserver(displayObserver)
Context -> Network : addObserver(serverConnection)
Display -> GUI : onDisplayUpdate("0")
note right of GUI
  Display shows "0"
end note

' =====================================================================
' USER PRESSES "4"
' =====================================================================

User -> GUI : click "4"
GUI -> Context : onDigit(4)
Context -> State : onDigit(context, 4)
State -> Engine : appendDigit(4)
Engine --> State : void
State --> Context : void
Context -> Display : notifyDisplayUpdate()
Display -> GUI : onDisplayUpdate("4")
note right of GUI
  Display shows "4"
  State: GettingFirstOperandState
end note

' =====================================================================
' USER PRESSES "+"
' =====================================================================

User -> GUI : click "+"
GUI -> Context : onAddSub('+')
Context -> State : onAddSub(context, '+')
State -> Engine : storeLeftOperand('+')
activate Engine
  Engine -> Engine : operands.add(4)
  Engine -> Engine : operators.add('+')
deactivate Engine
State -> Engine : resetCurrentNumber()
State -> Engine : setPendingOp('+')
State -> Context : transitionTo(WaitingForAddSubOperandState)
Context -> Display : notifyDisplayUpdate()
Display -> GUI : onDisplayUpdate("4+")
note right of GUI
  Display shows "4+"
  State: WaitingForAddSubOperandState
  operands=[4], operators=[+]
end note

' =====================================================================
' USER PRESSES "5"
' =====================================================================

User -> GUI : click "5"
GUI -> Context : onDigit(5)
Context -> State : onDigit(context, 5)
State -> Engine : resetCurrentNumber()
State -> Engine : appendDigit(5)
State -> Context : transitionTo(GettingAddSubOperandState)
Context -> Display : notifyDisplayUpdate()
Display -> GUI : onDisplayUpdate("4+5")
note right of GUI
  Display shows "4+5"
  State: GettingAddSubOperandState
  currentNumber=5
end note

' =====================================================================
' USER PRESSES "*"
' =====================================================================

User -> GUI : click "*"
GUI -> Context : onMulDiv('*')
Context -> State : onMulDiv(context, '*')
note right of State
  '+' has lower precedence than '*'
  so 5 is NOT yet committed to the tree.
  currentNumber=5 is held pending the
  higher-precedence MulDiv operand.
end note
State -> Engine : storeLeftOperand('*')
activate Engine
  Engine -> Engine : operands.add(5)
  Engine -> Engine : operators.add('*')
deactivate Engine
State -> Engine : resetCurrentNumber()
State -> Engine : setPendingOp('*')
State -> Context : transitionTo(WaitingForMulDivOperandState)
Context -> Display : notifyDisplayUpdate()
Display -> GUI : onDisplayUpdate("4+5*")
note right of GUI
  Display shows "4+5*"
  State: WaitingForMulDivOperandState
  operands=[4, 5], operators=[+, *]
end note

' =====================================================================
' USER PRESSES "6"
' =====================================================================

User -> GUI : click "6"
GUI -> Context : onDigit(6)
Context -> State : onDigit(context, 6)
State -> Engine : resetCurrentNumber()
State -> Engine : appendDigit(6)
State -> Context : transitionTo(GettingMulDivOperandState)
Context -> Display : notifyDisplayUpdate()
Display -> GUI : onDisplayUpdate("4+5*6")
note right of GUI
  Display shows "4+5*6"
  State: GettingMulDivOperandState
  currentNumber=6
end note

' =====================================================================
' USER PRESSES "="
' =====================================================================

User -> GUI : click "="
GUI -> Context : onEquals()
Context -> State : onEquals(context)
State -> Context : submitEquals()

Context -> Engine : evaluate()
activate Engine
  Engine -> Engine : storeLeftOperand()
  Engine -> Engine : operands.add(6)
  Engine -> Engine : buildTreeWithPrecedence([4, 5, 6], [+, *])
  note right of Engine
    Precedence resolution:
      '*' binds tighter than '+'
      Step 1: build MulDiv node
        MulNode = BinaryOperationNode(
          NumberNode(5), '*', NumberNode(6)
        )
      Step 2: build AddSub node
        AddNode = BinaryOperationNode(
          NumberNode(4), '+', MulNode
        )
    Final tree:
      4 + (5 * 6)
  end note
  Engine --> Context : tree
deactivate Engine

Context -> Evaluator : new EvaluatorVisitor()
activate Evaluator
Context -> Engine : tree.accept(evaluator)
activate Engine
  Engine -> Evaluator : visitBinary(addNode)
  Evaluator -> Evaluator : visit left child (NumberNode 4)
  Evaluator --> Evaluator : 4
  Evaluator -> Evaluator : visit right child (mulNode)
  Evaluator -> Evaluator : visit left child (NumberNode 5)
  Evaluator --> Evaluator : 5
  Evaluator -> Evaluator : visit right child (NumberNode 6)
  Evaluator --> Evaluator : 6
  Evaluator -> Evaluator : return 5 * 6
  Evaluator --> Evaluator : 30
  Evaluator -> Evaluator : return 4 + 30
  Evaluator --> Engine : 34
deactivate Engine
Evaluator --> Context : 34
note right of Evaluator
  ONLY class that performs arithmetic

  5 * 6 = 30
  4 + 30 = 34
end note
deactivate Evaluator

Context -> Engine : reset()
activate Engine
  Engine -> Engine : clear operands and operators
deactivate Engine
Context -> Engine : setCurrentNumber(34)

Context -> Display : notifyResultReady(34)
Display -> GUI : onResultReady(34)
note right of GUI
  Display shows "34"
  State: CalculateState
end note

Context -> Network : notifyResultReady(34)
Network -> Server : sendToServer(message)
activate Server
note right of Network
  Creates CalculatorProtocolMessage.request("34")
  Sends via Socket to localhost:9090
end note

Server -> Server : ObjectInputStream.readObject()
Server -> Server : process(request)
note right of Server
  Receives: CalculatorProtocolMessage(REQUEST, "34")
  Logs: "Received result from client: 34"
end note
Server -> Network : ObjectOutputStream.writeObject(response)
note right of Server
  Sends back: CalculatorProtocolMessage(RESPONSE, "OK")
end note
deactivate Server

deactivate Network
deactivate Display
deactivate State
deactivate Context

' =====================================================================
' END STATE
' =====================================================================

note over User, Server
  Calculation Complete
  - Expression: 4 + 5 * 6
  - Result: 34 (operator precedence applied: 5*6=30, 4+30=34)
  - Display updated
  - Server logged result
  - Ready for next calculation
end note

@enduml
