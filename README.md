<p align="center">
  <h1 align="center">AgentVerse</h1>
</p>
<p align="center">
  <strong>Multi-agent Conversation Platform</strong><br />
  <span>An enterprise-grade AI agent platform built with Spring AI Alibaba Graph</span>
</p>

<p align="center">
  <a href="./LICENSE"><img alt="License" src="https://img.shields.io/badge/license-Apache--2.0-4a9b8f?style=flat-square" /></a>
  <img alt="Java" src="https://img.shields.io/badge/Java-17+-df5b46?style=flat-square&logo=openjdk" />
  <img alt="Maven" src="https://img.shields.io/badge/Maven-Multi--Module-cb3b32?style=flat-square&logo=apachemaven&logoColor=white" />
  <img alt="Vue 3" src="https://img.shields.io/badge/Vue-3-42b883?style=flat-square&logo=vuedotjs&logoColor=white" />
  <img alt="Spring AI" src="https://img.shields.io/badge/Spring%20AI-1.1.0-6db33f?style=flat-square&logo=spring&logoColor=white" />
  <img alt="ReAct Agent" src="https://img.shields.io/badge/ReAct%20Agent-Tool%20Calling-e87545?style=flat-square" />
  <img alt="Hybrid Search" src="https://img.shields.io/badge/Hybrid%20Search-RRF%20Fusion-7c6ee6?style=flat-square" />
  <img alt="Knowledge Graph" src="https://img.shields.io/badge/Knowledge%20Graph-Neo4j-4581c3?style=flat-square&logo=neo4j&logoColor=white" />
  <img alt="MCP" src="https://img.shields.io/badge/MCP-Tool%20Protocol-4f6bed?style=flat-square" />
</p>

<p align="center">
  <a href="#features"><strong>Features</strong></a>
  ·
  <a href="#architecture"><strong>Architecture</strong></a>
  ·
  <a href="#quick-start"><strong>Quick Start</strong></a>
  ·
  <a href="#modules"><strong>Modules</strong></a>
</p>

---

## What is AgentVerse?

AgentVerse is an enterprise-level multi-agent conversation platform built on Spring Boot and Spring AI Alibaba Graph. It provides a complete solution for building intelligent conversational AI applications with RAG (Retrieval-Augmented Generation), tool calling, knowledge management, and multi-agent orchestration.

The platform covers the full lifecycle from document ingestion, knowledge indexing, hybrid retrieval, evidence-based generation, to tool calling and observability — every component is designed with production-grade engineering practices.

## Features

### Core Capabilities

- **ReAct Agent**: Full implementation of ReAct (Reasoning + Acting) pattern with tool calling, multi-step reasoning, and checkpoint persistence
- **Three-tier Executor System**: Intelligent routing between clarification, knowledge Q&A, and open-ended agent execution
- **Hybrid Retrieval**: Dual-channel parallel retrieval (vector + keyword) with RRF fusion ranking and optional external Rerank
- **Knowledge Graph**: Neo4j-based document structure graph with Document → Section → Item hierarchy for structured navigation
- **Knowledge Routing**: Three-level funnel (Scope → Topic → Document) for automatic knowledge domain narrowing
- **Evidence Control**: Budget-controlled evidence assembly with no-evidence short-circuit to prevent hallucination
- **Parent-Child Block Aggregation**: Small chunks for retrieval precision, large chunks for answer completeness

### Document Processing

- **Multi-format Parsing**: Apache Tika integration for PDF, Word, PPT, and other document formats
- **Combinatorial Chunking Engine**: Four strategies (structure-based, recursive, semantic, LLM-powered) working as a pipeline
- **Async Processing**: Kafka-based asynchronous document parsing and indexing with task logging
- **Dual Engine Indexing**: Vector database +倒排索引 for hybrid retrieval foundation

### Agent & Tools

- **MCP Protocol**: Standard Model Context Protocol for dynamic tool discovery and invocation
- **Skills System**: Declarative skill definition with auto-loading and hot-plugging
- **Tool Safety**: Model call limits, tool call limits, exponential backoff retry, and error fallback
- **Parallel Execution**: Up to 4 tools executed in parallel per agent step

### Engineering Quality

- **Cluster Safety**: Redis lease-based mutual exclusion for multi-instance deployment
- **Full-chain Observability**: End-to-end tracing with visualization dashboard
- **Session Memory**: Three strategies (none, sliding window, summary compression) with MySQL persistence
- **SSE Streaming**: Real-time streaming output with citation sources and follow-up question suggestions

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (Vue 3)                      │
│              Chat UI + Admin Console                     │
└───────────────────────┬─────────────────────────────────┘
                        │ HTTP/SSE
┌───────────────────────▼─────────────────────────────────┐
│                   API Gateway                            │
│            Request Handling + Auth + SSE                  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              Orchestration Engine                         │
│  ┌─────────────┐ ┌──────────────┐ ┌──────────────────┐  │
│  │   Router     │ │  Rewriter    │ │  Sub-question    │  │
│  │  (Intent)    │ │  (Query)     │ │  Splitter        │  │
│  └─────────────┘ └──────────────┘ └──────────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              Three-tier Executor                          │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐ │
│  │ Clarification │ │  Knowledge   │ │  ReAct Agent     │ │
│  │  Executor     │ │  Q&A (RAG)   │ │  (Tool Calling)  │ │
│  └──────────────┘ └──────────────┘ └──────────────────┘ │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              Retrieval Layer                              │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐ │
│  │ Vector Search │ │ Keyword      │ │  Knowledge       │ │
│  │ (PGVector)    │ │ Search (ES)  │ │  Graph (Neo4j)   │ │
│  └──────────────┘ └──────────────┘ └──────────────────┘ │
│                    RRF Fusion + Rerank                    │
└─────────────────────────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              Infrastructure                              │
│  MySQL │ PGVector │ Elasticsearch │ Neo4j │ Redis │ Kafka │ MinIO
└─────────────────────────────────────────────────────────┘
```

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 16+
- MySQL 8.0+
- PostgreSQL (with PGVector extension)
- Elasticsearch 8.x
- Neo4j 5.x
- Redis 7.x
- Kafka 3.x
- MinIO

### Backend

```bash
# Clone the repository
git clone https://github.com/slienceecheo/agentverse.git
cd agentverse

# Build the project
mvn clean install -DskipTests

# Run the application
cd nexus-agent-business/nexus-agent-business-chat
mvn spring-boot:run
```

### Frontend

```bash
cd vue

# Install dependencies
npm install

# Development server
npm run dev

# Production build
npm run build
```

### Configuration

Copy and modify the configuration file:

```bash
cd nexus-agent-business/nexus-agent-business-chat/src/main/resources
cp application.yaml.example application.yaml
```

Update the following in `application.yaml`:
- Database connection (MySQL, PostgreSQL, Elasticsearch)
- Redis and Kafka configuration
- Neo4j connection
- MinIO configuration
- AI model API keys

## Modules

| Module | Description |
|--------|-------------|
| `nexus-agent-business` | Core business logic including chat agent, document management |
| `nexus-agent-common` | Common utilities, web framework, database helpers |
| `nexus-agent-id-generator-framework` | Distributed ID generation framework |
| `nexus-agent-redis-tool-framework` | Redis caching and utility framework |
| `nexus-agent-redisson-framework` | Distributed lock, lease, and delay queue framework |
| `ai-example` | Example projects demonstrating Spring AI features |
| `vue` | Frontend application built with Vue 3 |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.5, Spring AI 1.1, Spring AI Alibaba 1.1
- **Frontend**: Vue 3, Vite, Pinia
- **Database**: MySQL 8, PostgreSQL (PGVector), Elasticsearch 8, Neo4j 5
- **Cache/MQ**: Redis 7, Kafka 3
- **Storage**: MinIO
- **Document Processing**: Apache Tika
- **Build Tool**: Maven (multi-module)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

*Built with Spring AI Alibaba Graph*
