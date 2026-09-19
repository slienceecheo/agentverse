<p align="center">
  <h1 align="center">AgentVerse</h1>
</p>
<p align="center">
  <strong>多智能体对话平台</strong><br />
  <span>基于 Spring AI Alibaba Graph 的企业级 AI 智能体平台</span>
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
  <a href="#功能特性"><strong>功能特性</strong></a>
  ·
  <a href="#系统架构"><strong>系统架构</strong></a>
  ·
  <a href="#快速启动"><strong>快速启动</strong></a>
  ·
  <a href="#项目模块"><strong>项目模块</strong></a>
</p>

---

## AgentVerse 是什么？

AgentVerse 是一个基于 Spring Boot 和 Spring AI Alibaba Graph 构建的企业级多智能体对话平台。它为构建智能对话 AI 应用提供了完整的解决方案，包括 RAG（检索增强生成）、工具调用、知识管理和多智能体编排。

平台覆盖了从文档入库、知识索引、混合检索、证据驱动生成，到工具调用和可观测治理的完整链路——每个组件都按照生产级工程实践设计。

## 功能特性

### 核心能力

- **ReAct 智能体**：完整的 ReAct（推理+行动）模式实现，支持工具调用、多步推理和检查点持久化
- **三层执行器体系**：智能路由到澄清、知识问答或开放式智能体执行
- **混合检索**：双通道并行检索（向量+关键词），RRF 融合排序，可选外部 Rerank
- **知识图谱**：基于 Neo4j 的文档结构图谱，支持 Document → Section → Item 层级导航
- **知识路由**：三级漏斗（Scope → Topic → Document）自动缩小知识域范围
- **证据控制**：预算控制的证据组装，无证据短路防止幻觉
- **父子块聚合**：小块保证检索精度，大块保证回答完整性

### 文档处理

- **多格式解析**：Apache Tika 集成，支持 PDF、Word、PPT 等多种文档格式
- **组合式切块引擎**：四种策略（结构化、递归、语义、LLM 驱动）流水线协作
- **异步处理**：基于 Kafka 的异步文档解析和索引构建，带任务日志
- **双引擎索引**：向量数据库 + 倒排索引，为混合检索奠定基础

### 智能体与工具

- **MCP 协议**：标准 Model Context Protocol，支持动态工具发现和调用
- **Skills 体系**：声明式技能定义，支持自动加载和热插拔
- **工具安全**：模型调用限制、工具调用限制、指数退避重试和错误兜底
- **并行执行**：每个智能体步骤最多支持 4 个工具并行执行

### 工程质量

- **集群安全**：基于 Redis 租约的互斥机制，支持多实例部署
- **全链路可观测**：端到端追踪，可视化观测面板
- **会话记忆**：三种策略（无记忆、滑动窗口、摘要压缩），MySQL 持久化
- **SSE 流式输出**：实时流式推送，附带引用来源和推荐追问问题

## 系统架构

```
┌─────────────────────────────────────────────────────────┐
│                    前端 (Vue 3)                          │
│              对话界面 + 管理后台                          │
└───────────────────────┬─────────────────────────────────┘
                        │ HTTP/SSE
┌───────────────────────▼─────────────────────────────────┐
│                   API 网关                               │
│            请求处理 + 鉴权 + SSE                         │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              编排引擎                                    │
│  ┌─────────────┐ ┌──────────────┐ ┌──────────────────┐  │
│  │   路由器     │ │  改写器       │ │  子问题           │  │
│  │  (意图判断)  │ │  (Query)     │ │  拆分器           │  │
│  └─────────────┘ └──────────────┘ └──────────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              三层执行器                                   │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐ │
│  │ 澄清执行器    │ │  知识问答     │ │  ReAct 智能体    │ │
│  │              │ │  (RAG)       │ │  (工具调用)       │ │
│  └──────────────┘ └──────────────┘ └──────────────────┘ │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              检索层                                      │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐ │
│  │ 向量检索      │ │ 关键词检索    │ │  知识图谱         │ │
│  │ (PGVector)    │ │ (ES)         │ │  (Neo4j)         │ │
│  └──────────────┘ └──────────────┘ └──────────────────┘ │
│                    RRF 融合 + Rerank                     │
└─────────────────────────────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│              基础设施                                    │
│  MySQL │ PGVector │ Elasticsearch │ Neo4j │ Redis │ Kafka │ MinIO
└─────────────────────────────────────────────────────────┘
```

## 快速启动

### 环境要求

- Java 17+
- Maven 3.8+
- Node.js 16+
- MySQL 8.0+
- PostgreSQL（需安装 PGVector 扩展）
- Elasticsearch 8.x
- Neo4j 5.x
- Redis 7.x
- Kafka 3.x
- MinIO

### 后端启动

```bash
# 克隆仓库
git clone https://github.com/slienceecheo/agentverse.git
cd agentverse

# 构建项目
mvn clean install -DskipTests

# 启动应用
cd nexus-agent-business/nexus-agent-business-chat
mvn spring-boot:run
```

### 前端启动

```bash
cd vue

# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build
```

### 配置说明

复制并修改配置文件：

```bash
cd nexus-agent-business/nexus-agent-business-chat/src/main/resources
cp application.yaml.example application.yaml
```

在 `application.yaml` 中更新以下配置：
- 数据库连接（MySQL、PostgreSQL、Elasticsearch）
- Redis 和 Kafka 配置
- Neo4j 连接
- MinIO 配置
- AI 模型 API 密钥

## 项目模块

| 模块 | 说明 |
|------|------|
| `nexus-agent-business` | 核心业务逻辑，包括对话智能体、文档管理 |
| `nexus-agent-common` | 通用工具、Web 框架、数据库辅助 |
| `nexus-agent-id-generator-framework` | 分布式 ID 生成框架 |
| `nexus-agent-redis-tool-framework` | Redis 缓存和工具框架 |
| `nexus-agent-redisson-framework` | 分布式锁、租约和延迟队列框架 |
| `ai-example` | Spring AI 示例项目 |
| `vue` | 基于 Vue 3 的前端应用 |

## 技术栈

- **后端**：Java 17, Spring Boot 3.5, Spring AI 1.1, Spring AI Alibaba 1.1
- **前端**：Vue 3, Vite, Pinia
- **数据库**：MySQL 8, PostgreSQL (PGVector), Elasticsearch 8, Neo4j 5
- **缓存/消息队列**：Redis 7, Kafka 3
- **对象存储**：MinIO
- **文档处理**：Apache Tika
- **构建工具**：Maven（多模块）

## 许可证

本项目基于 Apache License 2.0 开源 - 详见 [LICENSE](LICENSE) 文件。

---

*基于 Spring AI Alibaba Graph 构建*
