# BrokerMesh

A **Java-based distributed publish-subscribe system**, implementing a simple broker mesh architecture.  
This project simulates how brokers, publishers, and subscribers interact through network sockets.

---

## Features

- **Broker**
  - Handles client connections (publishers and subscribers)
  - Routes published messages to subscribed clients
  - Supports multiple topics
  - Multi-threaded client handling
- **Publisher**
  - Connects to the broker
  - Publishes messages to specified topics
- **Subscriber**
  - Connects to the broker
  - Subscribes to topics and receives messages in real-time
- **Simple protocol** for communication between clients and broker

---

## Technologies Used

- **Java SE** (Standard Edition)
- **Socket programming**
- **Multi-threading** (for concurrent client handling)

---

## Project Structure

```plaintext
BrokerMesh/
│
├── Broker/         # Broker server code
├── Publisher/      # Publisher client code
└── Subscriber/     # Subscriber client code
```

Each module (`Broker`, `Publisher`, `Subscriber`) contains:

- `src/` → Java source files
- `out/` → Compiled classes (optional to keep)
- `.idea/` → IntelliJ project settings (optional)

---

## How to Run

1. **Start the Broker**

```bash
cd Broker/src
javac *.java
java Main
```

2. **Start a Subscriber**

```bash
cd Subscriber/src
javac *.java
java Main
```
(When prompted, input which topic to subscribe to.)

3. **Start a Publisher**

```bash
cd Publisher/src
javac *.java
java Main
```
(When prompted, input which topic to publish messages to.)

4. **Interaction**

- Publisher sends messages tagged with a topic
- Broker routes messages to all Subscribers of that topic
- Subscribers display received messages

✅ Now you have a live demo of publish-subscribe communication!

---

## Example Workflow

- Subscriber 1 subscribes to "Sports"
- Subscriber 2 subscribes to "News"
- Publisher publishes "Football match tonight!" to "Sports"
- **Only Subscriber 1** receives the message

---

## Future Improvements (Ideas)

- Add broker fault tolerance
- Support message persistence
- Implement secure authentication between clients and broker

---

# 📢 Notes

This project was built for educational purposes to understand the core concepts behind real-world broker systems like **Kafka** and **RabbitMQ**, but implemented manually using raw sockets and Java threads.

