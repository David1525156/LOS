# 在线图书借阅系统 API 接口文档

| 项目 | 内容 |
| --- | --- |
| 接口版本 | v1.0 |
| 基础地址 | `http://localhost:8080` |
| 数据格式 | JSON（UTF-8） |
| 当前迭代 | Sprint 1 |

---

## 1. 通用约定

### 1.1 统一响应结构

所有接口返回统一结构：

```json
{
  "code": 200,
  "message": "ok",
  "data": {}
}
```

### 1.2 响应码说明

| code | 含义 |
| --- | --- |
| 200 | 请求成功 |
| 400 | 业务错误或参数校验失败（HTTP 状态仍为 200，以 body 中 code 为准） |
| 401 | 未认证 / token 无效或过期（HTTP 状态为 401） |
| 500 | 服务器内部异常 |

### 1.3 鉴权说明

- 除 `POST /api/auth/login` 外，**写接口**（如 `POST /api/books`）需携带管理员 token：
  - 请求头：`Authorization: Bearer <token>`
  - token 由登录接口签发，有效期 2 小时；
- 所有 `GET` 接口公开访问，无需鉴权；
- token 缺失、错误或过期时返回 401。

### 1.4 分页约定

- `page`：页码，从 1 开始，默认 1；传入非法值（如 0 或负数）时按 1 处理；
- `size`：每页条数，默认 10，上限 100。

---

## 2. 图书列表（分页）

> 对应用户故事 US-01：浏览图书列表

**接口地址**：`GET /api/books`

**鉴权**：无

### 2.1 请求参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
| --- | --- | --- | --- | --- |
| page | int | 否 | 1 | 页码，从 1 开始 |
| size | int | 否 | 10 | 每页条数，上限 100 |

### 2.2 请求示例

```
GET /api/books?page=1&size=10
```

### 2.3 响应示例（成功）

```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "三体",
        "author": "刘慈欣",
        "description": "地球文明向宇宙发出的一声啼鸣，开启了与三体文明生死搏杀的时代。",
        "stock": 8,
        "createTime": "2026-10-21 12:06:09"
      }
    ],
    "total": 25,
    "current": 1,
    "size": 10
  }
}
```

### 2.4 返回字段说明

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| data.records | array | 当前页图书列表 |
| data.records[].id | number | 图书 ID |
| data.records[].title | string | 书名 |
| data.records[].author | string | 作者 |
| data.records[].description | string | 图书简介（可为空） |
| data.records[].stock | number | 可借阅库存 |
| data.records[].createTime | string | 创建时间（yyyy-MM-dd HH:mm:ss） |
| data.total | number | 总记录数 |
| data.current | number | 当前页码 |
| data.size | number | 每页条数 |

> 排序规则：按创建时间倒序，新书在前。

---

## 3. 图书详情

> 对应用户故事 US-02：查看图书详情

**接口地址**：`GET /api/books/{id}`

**鉴权**：无

### 3.1 请求参数

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | number | 是 | 图书 ID（路径参数） |

### 3.2 请求示例

```
GET /api/books/1
```

### 3.3 响应示例（成功）

```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "id": 1,
    "title": "三体",
    "author": "刘慈欣",
    "description": "地球文明向宇宙发出的一声啼鸣，开启了与三体文明生死搏杀的时代。",
    "stock": 8,
    "createTime": "2026-10-21 12:06:09"
  }
}
```

### 3.4 响应示例（图书不存在）

```json
{
  "code": 400,
  "message": "图书不存在",
  "data": null
}
```

---

## 4. 管理员登录

> 对应用户故事 US-03：管理员登录

**接口地址**：`POST /api/auth/login`

**鉴权**：无

### 4.1 请求参数（请求体 JSON）

| 字段 | 类型 | 必填 | 校验 | 说明 |
| --- | --- | --- | --- | --- |
| username | string | 是 | 非空 | 管理员用户名 |
| password | string | 是 | 非空 | 管理员密码 |

### 4.2 请求示例

```json
{
  "username": "admin",
  "password": "123456"
}
```

### 4.3 响应示例（成功）

```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "token": "8e8a828fc06c4ab6a8dbe61975b1fda6",
    "username": "admin"
  }
}
```

### 4.4 返回字段说明

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| data.token | string | 访问令牌，有效期 2 小时，后续写接口需在请求头携带 |
| data.username | string | 登录成功的用户名 |

### 4.5 响应示例（用户名或密码错误）

```json
{
  "code": 400,
  "message": "用户名或密码错误",
  "data": null
}
```

> 默认管理员账号：`admin / 123456`

---

## 5. 新增图书

> 对应用户故事 US-03：管理员新增图书

**接口地址**：`POST /api/books`

**鉴权**：需要，请求头 `Authorization: Bearer <token>`

### 5.1 请求参数（请求体 JSON）

| 字段 | 类型 | 必填 | 校验 | 说明 |
| --- | --- | --- | --- | --- |
| title | string | 是 | 非空，≤ 100 字符 | 书名 |
| author | string | 是 | 非空，≤ 50 字符 | 作者 |
| description | string | 否 | ≤ 1000 字符 | 图书简介，可空 |
| stock | int | 否 | ≥ 0 | 可借阅库存，默认 1 |

### 5.2 请求示例

```json
{
  "title": "测试新书",
  "author": "测试作者",
  "description": "Sprint1 新增测试",
  "stock": 3
}
```

### 5.3 响应示例（成功）

```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "id": 26,
    "title": "测试新书",
    "author": "测试作者",
    "description": "Sprint1 新增测试",
    "stock": 3,
    "createTime": "2026-10-21 12:12:39"
  }
}
```

> 新增成功后，新书出现在列表第一页首位（按创建时间倒序），`total` 相应 +1。

### 5.4 错误响应

**未携带 / 无效 token（HTTP 401）**：

```json
{
  "code": 401,
  "message": "未登录或登录已过期",
  "data": null
}
```

**参数校验失败（示例）**：

```json
{
  "code": 400,
  "message": "书名不能为空",
  "data": null
}
```

**校验错误信息汇总**：

| 触发条件 | message |
| --- | --- |
| 书名为空 | 书名不能为空 |
| 书名超过 100 字符 | 书名长度不能超过100个字符 |
| 作者为空 | 作者不能为空 |
| 作者超过 50 字符 | 作者长度不能超过50个字符 |
| 简介超过 1000 字符 | 简介长度不能超过1000个字符 |
| 库存为负数 | 库存不能为负数 |

---

## 6. 服务器内部异常（通用）

当服务内部出现未预期异常时，所有接口统一返回（不泄露堆栈）：

```json
{
  "code": 500,
  "message": "服务器开小差了，请稍后再试",
  "data": null
}
```

---

## 7. curl 调用示例（开发自测）

```bash
# 1. 分页查询图书列表
curl http://localhost:8080/api/books?page=1&size=10

# 2. 查询图书详情
curl http://localhost:8080/api/books/1

# 3. 管理员登录，获取 token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 4. 新增图书（需携带 token）
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"title":"新书","author":"作者","stock":1}'
```
