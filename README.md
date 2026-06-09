# URL Shortener - Distributed Backend System

A scalable distributed URL Shortener backend built using:

* Java 21
* Spring Boot
* PostgreSQL
* Redis
* Apache Kafka
* Docker
* Nginx Load Balancer

This project simulates a production-style distributed backend architecture with:

* Multiple backend instances
* Distributed caching
* Asynchronous analytics processing
* Load balancing
* Event-driven architecture

---

# 🚀 Features

## URL Shortening

* Convert long URLs into short URLs
* Base62 encoded short codes
* Snowflake-based distributed ID generation
* URL deduplication to prevent duplicate short URLs

---

## URL Redirection

* Redirect short URL → original long URL
* High-speed retrieval using Redis caching
* Reduced database hits for frequently accessed URLs

---

## Click Analytics

Tracks:

* Total clicks
* Click timestamps
* Short URL usage statistics

Analytics are stored asynchronously for better performance and scalability.

---

## Kafka Event Streaming

Analytics processing is asynchronous using Kafka.

Instead of:

```text
Request → Save Analytics → Respond
```

The system does:

```text
Request → Publish Kafka Event → Immediate Response
                                      ↓
                           Kafka Consumer
                                      ↓
                           Save Analytics
```

This improves scalability and reduces response latency.

---

## Redis Caching

Redis is used to:

* Cache URL lookups
* Reduce PostgreSQL load
* Improve redirect performance
* Minimize database round trips

---

## Horizontal Scaling

The system runs multiple backend instances:

* app1
* app2
* app3

All sharing:

* PostgreSQL
* Redis
* Kafka

This demonstrates a stateless distributed architecture.

---

## Nginx Load Balancer

Nginx distributes incoming traffic across backend instances using round-robin load balancing.

```text
Request 1 → App1
Request 2 → App2
Request 3 → App3
Request 4 → App1
```

---

# 🏗️ Architecture

```text
            Client
               |
               v
         NGINX Load Balancer
               |
  --------------------------------
  |              |              |
  v              v              v
App1           App2           App3
  |              |              |
  --------------------------------
               |
      -------------------
      |        |        |
      v        v        v
  PostgreSQL  Redis   Kafka
                           |
                           v
                 Analytics Consumer
```

---

# ⚙️ Tech Stack

| Technology             | Purpose                          |
| ---------------------- | -------------------------------- |
| Java 21                | Programming Language             |
| Spring Boot            | Backend Framework                |
| PostgreSQL             | Persistent Storage               |
| Redis                  | Distributed Cache                |
| Apache Kafka           | Event Streaming                  |
| Docker                 | Containerization                 |
| Docker Compose         | Multi-Container Orchestration    |
| Nginx                  | Reverse Proxy & Load Balancer    |
| Snowflake ID Generator | Distributed Unique ID Generation |
| Base62 Encoding        | Short URL Generation             |

---

# 🚀 Running the Project

## 1. Clone Repository

```bash
git clone https://github.com/naveendp2805/URL-Shortener.git
cd URL-Shortener
```

## 2. Build Application

```bash
mvn clean package -DskipTests
```

## 3. Start Distributed Infrastructure

```bash
docker compose up --build
```

---

# 🚀 Services

| Service    | Port |
| ---------- | ---- |
| Nginx      | 80   |
| App1       | 8081 |
| App2       | 8082 |
| App3       | 8083 |
| PostgreSQL | 5433 |
| Redis      | 6379 |
| Kafka      | 9092 |

---

# 📌 Key Learning Outcomes

* Distributed System Design
* Horizontal Scaling
* Reverse Proxy & Load Balancing
* Event-Driven Architecture
* Caching Strategies
* Docker Containerization
* Infrastructure Orchestration
* Backend Performance Optimization

---

# 🔮 Future Enhancements

* Custom URL aliases
* URL expiration support
* User authentication & authorization
* Prometheus monitoring
* Grafana dashboards
* Kubernetes deployment
* Advanced analytics dashboard

---

# 👨‍💻 Author

**Naveen Durga Prasad Eddum**

Aspiring Backend Developer focused on Java, Spring Boot, Distributed Systems, Caching, Event-Driven Systems, and Cloud-Native Applications.
