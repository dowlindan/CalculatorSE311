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
  - Protocol: Plain text strings (no object serialization)
end note


@enduml
