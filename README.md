# 在线图书借阅系统（Online Library System）

基于前后端分离架构的图书借阅管理系统。当前迭代为 **Sprint 1**，实现图书浏览、详情与管理员新增图书功能。

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 前端 | Vue 3 + Vite + Element Plus（Sprint 2 开发） |
| 后端 | Spring Boot 2.7.18 + MyBatis-Plus 3.5.7 |
| 数据库 | MySQL 8.0 |
| 构建工具 | Maven |
| 版本管理 | Git（GitHub） |

## 功能特性（Sprint 1）

- **US-01 图书列表**：分页展示全部图书（书名、作者、库存），每页 10 条
- **US-02 图书详情**：展示书名、作者、图书简介、可借阅库存
- **US-03 新增图书**：管理员登录后新增图书，非法输入友好提示

> 借书、还书、用户注册等将在后续迭代实现。

## 项目结构

```
OLS/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/       # 业务代码（common / entity / mapper / auth / config）
│   ├── src/main/resources/  # 配置文件
│   └── src/test/java/       # 单元测试（15 个用例）
├── db/init.sql              # 建库建表脚本 + 25 本种子图书 + 默认管理员
├── docs/                    # 项目文档（需求 / API / 数据库设计 / 分工）
└── README.md
```

## 快速开始

### 1. 初始化数据库

```bash
mysql -uroot -p < db/init.sql
```

默认管理员：`admin / 123456`

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

服务地址：`http://localhost:8080`。连接配置见 `backend/src/main/resources/application.yml`（默认 `root/123456`，可按需修改）。

### 3. 运行单元测试

```bash
cd backend
mvn test
```

## 接口一览

| 方法 | 路径 | 说明 | 鉴权 |
| --- | --- | --- | --- |
| GET | /api/books?page=1&size=10 | 图书分页列表 | 无 |
| GET | /api/books/{id} | 图书详情 | 无 |
| POST | /api/auth/login | 管理员登录 | 无 |
| POST | /api/books | 新增图书 | Bearer Token |

统一响应格式：`{"code":200,"message":"success","data":...}`。详见 [docs/API接口文档.md](docs/API接口文档.md)。

## 文档

| 文档 | 说明 |
| --- | --- |
| [项目需求文档](docs/项目需求文档.md) | 用户故事与验收标准 |
| [API接口文档](docs/API接口文档.md) | 接口定义与错误码 |
| [数据库设计文档](docs/数据库设计文档.md) | 表结构与设计决策 |
| [分工文档](docs/分工文档.md) | 小组模块划分 |

## 团队分工

| 成员 | 负责模块 |
| --- | --- |
| 孟函笑 | 项目负责人、需求与数据库设计 |
| 韩大伟 | 后端框架与登录认证 |
| 王明凯 | 后端图书查询（列表 / 详情） |
| 李自谦 | 后端新增图书 |
| 王煜哲 | 前端图书浏览 |
| 邓寅卓 | 前端管理员功能 |

## 后续规划（Sprint 2+）

- 用户注册 / 读者登录
- 借书（库存扣减 + 借阅记录）
- 还书（库存恢复 + 记录更新）
- 借阅记录查询与逾期提醒
