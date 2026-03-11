@startuml CalculatorServer_Architecture
skinparam classBackgroundColor #FEFEFE
skinparam classBorderColor #333333
skinparam classArrowColor #333333

' =====================================================================
' SERVER COMPONENT
' =====================================================================

class CalculatorServer {
    + {static} HOST: String = "localhost"
    + {static} PORT: int = 9090

    + start(): void
    - handle(socket: Socket): void
    - process(request: CalculatorProtocolMessage): CalculatorProtocolMessage
    + {static} main(args: String[]): void
}

note right of CalculatorServer
  CALCULATOR SERVER

  Server-side component that:
  1. Listens on localhost:9090
  2. Accepts incoming client connections
  3. Deserializes CalculatorProtocolMessage objects
  4. Processes requests (logs result strings)
  5. Sends back acknowledgement responses

  Under the client-rich design:
  - Clients handle all arithmetic
  - Server only logs results for auditing
  - Does NOT evaluate expressions
end note

' =====================================================================
' PROTOCOL MESSAGE
' =====================================================================

enum MessageType {
    REQUEST
    RESPONSE
    ERROR
}

class CalculatorProtocolMessage {
    - {static} serialVersionUID: long
    - type: MessageType
    - payload: String
    - errorMessage: String

    + {static} request(payload: String): CalculatorProtocolMessage
    + {static} response(payload: String): CalculatorProtocolMessage
    + {static} error(message: String): CalculatorProtocolMessage
    + getType(): MessageType
    + getPayload(): String
    + getErrorMessage(): String
    - CalculatorProtocolMessage(type: MessageType, payload: String, errorMessage: String)
}

note right of CalculatorProtocolMessage
  PROTOCOL MESSAGE

  Encapsulates communication between client and server.

  REQUEST (client to server):
  - Carries result string from completed calculation
  - Example: "8" (from 3 + 5 calculation)

  RESPONSE (server to client):
  - Acknowledgement that server received and logged the result
  - Payload: "OK"

  ERROR (server to client):
  - Indicates protocol error or server issue
  - Contains error message
end note

' =====================================================================
' RELATIONSHIPS
' =====================================================================

CalculatorServer --> CalculatorProtocolMessage : processes & returns
CalculatorProtocolMessage --> MessageType : has type
CalculatorProtocolMessage ..|> Serializable : implements

' =====================================================================
' ARCHITECTURE NOTES
' =====================================================================

note as Legend
  SERVER COMPONENT ARCHITECTURE

  Responsibility:
  - Accept client connections on localhost:9090
  - Receive CalculatorProtocolMessage objects (result strings)
  - Log results for auditing/persistence
  - Send acknowledgement responses

  Key Design Decision:
  Under the CLIENT-RICH architecture, the server is minimal:
  - Does NOT build expression trees
  - Does NOT evaluate expressions
  - Does NOT perform arithmetic
  All calculation happens on the client side.

  Thread Model:
  - Main thread listens for connections
  - Each client connection handled on separate thread
  - Allows concurrent client requests

  Protocol Flow:
  1. Client connects via Socket
  2. Client sends CalculatorProtocolMessage with result string
  3. Server deserializes message
  4. Server logs the result
  5. Server sends response CalculatorProtocolMessage
  6. Connection closes

  Message Types Used:
  - REQUEST: Client sends calculated result
  - RESPONSE: Server acknowledges receipt
  - ERROR: Server indicates protocol/processing error
end note

@enduml
