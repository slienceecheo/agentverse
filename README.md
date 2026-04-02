# AgentVerse

多智能体对话平台（企业级 AI 智能体工程实践）。

## 模块规划

| 模块 | 说明 |
| :--- | :--- |
| `agentverse-common` | 通用框架层：统一响应、异常体系、MyBatis-Plus 与 Web 配置 |
| `agentverse-id-generator-framework` | 雪花 ID 发号器，支持 worker 节点动态分配 |
| `agentverse-redis-tool-framework` | Redis 通用工具封装 |
| `agentverse-redisson-framework` | 分布式锁、服务租约、延迟队列、重复执行限制 |
| `agentverse-business` | 对话业务：编排、RAG 检索、文档治理、运营控制台 |
| `ai-example` | Spring AI / RAG / MCP 学习示例 |
| `vue` | 前端工程（Vue 3 + Vite） |

## 环境

- JDK 17
- Spring Boot 3.5.x / Spring AI 1.1.x
- MySQL、PostgreSQL(PGVector)、Elasticsearch、Neo4j、Redis、Kafka、MinIO
