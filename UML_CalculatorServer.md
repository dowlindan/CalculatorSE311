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
    + {static} main(args: String[]): void
}

note right of CalculatorServer
  CALCULATOR SERVER

  Server-side component that:
  1. Listens on localhost:9090
  2. Accepts incoming client connections
  3. Reads result strings from clients
  4. Logs results for auditing
  5. Sends back acknowledgement responses

  Under the client-rich design:
  - Clients handle all arithmetic
  - Server only logs results
  - Does NOT evaluate expressions
  - Protocol: Plain text strings (no serialization)
end note


' =====================================================================
' ARCHITECTURE NOTES
' =====================================================================

note as Legend
  SERVER COMPONENT ARCHITECTURE

  Responsibility:
  - Accept client connections on localhost:9090
  - Read result strings from clients via BufferedReader
  - Log results for auditing/persistence
  - Send acknowledgement responses via PrintWriter

  Key Design Decision:
  Under the CLIENT-RICH architecture, the server is minimal:
  - Does NOT build expression trees
  - Does NOT evaluate expressions
  - Does NOT perform arithmetic
  All calculation happens on the client side.

  Protocol Simplification:
  - Old: Serialized CalculatorProtocolMessage objects
  - New: Plain text strings (result + acknowledgement)
  - Benefits: Simpler code, language-agnostic, debuggable

  Thread Model:
  - Main thread listens for connections
  - Each client connection handled on separate thread
  - Allows concurrent client requests

  Protocol Flow:
  1. Client connects via Socket
  2. Client sends result string: "8"
  3. Server reads line with BufferedReader
  4. Server logs: "Received result from client: 8"
  5. Server sends acknowledgement: "OK"
  6. Connection closes
end note

@enduml
