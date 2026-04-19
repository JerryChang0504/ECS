# ECS 專案教學說明

## 文件目的

這份文件的目標，是用教學角度說明此專案的：

- 專案架構
- 重點處理事項
- JWT 驗證流程
- 權限管理流程

讀完之後，應該能回答這幾個問題：

- 這個專案各層負責什麼？
- JWT 在這個專案中是怎麼發出去、怎麼驗證的？
- 哪些 API 要登入？哪些 API 要 `ADMIN`？
- 401 與 403 分別在哪裡處理？

---

## 一、專案整體定位

這是一個使用 Spring Boot 建立的後端 API 專案，主要功能包含：

- 使用者註冊、登入、登出
- JWT Access Token / Refresh Token 簽發與刷新
- 商品查詢、商品管理
- 透過 Spring Security 保護特定 API

這個專案的核心練習點，不只是「登入成功後拿到 token」，而是完整做到：

1. 使用者登入取得 JWT
2. 前端呼叫 API 時帶上 JWT
3. 後端用 Filter 攔截請求並驗證 token
4. Spring Security 根據登入狀態與角色做授權判斷
5. 針對未登入、token 錯誤、token 過期、權限不足，回傳不同結果

---

## 二、專案架構

### 1. 分層概念

此專案採用典型的 Spring Boot 分層設計：

```text
Client
  -> Controller
     -> Service
        -> Repository
           -> Database
```

在這個基礎上，又額外加入了 JWT 與 Spring Security 的安全機制：

```text
Client
  -> SecurityConfig
     -> JwtAuthenticationFilter
        -> JwtUtil / UserService
  -> Controller
     -> Service
        -> Repository
```

### 2. 主要資料夾用途

#### `config`

放系統設定。

- `SecurityConfig`
  - 設定 Spring Security
  - 設定哪些路徑公開、哪些需要登入、哪些需要 `ADMIN`
  - 設定 CORS
  - 把 JWT Filter 掛進 Security Filter Chain
- `SwaggerConfig`
  - 設定 Swagger / OpenAPI 文件
  - 定義 `bearerAuth` 安全機制

#### `controller`

負責接 HTTP Request，接到請求後轉交給 Service。

- `AuthController`
  - 註冊、登入、刷新 token、登出、查詢使用者資料
- `ProductController`
  - 商品新增、查詢、更新、刪除

#### `service`

負責商業邏輯。

- `AuthService`
  - 驗證帳號密碼
  - 產生 access token / refresh token
  - refresh token rotation
  - logout 時撤銷 refresh token
- `ProductService`
  - 商品查詢、更新、刪除邏輯
- `UserService`
  - 使用者資料查詢與儲存

#### `repository`

透過 JPA 與資料庫互動。

- `UserRepository`
- `ProductRepository`
- `RefreshTokenRepository`

#### `entity`

資料表對應物件。

- `UserInfo`
- `Product`
- `RefreshToken`

#### `dto`

定義 request / response 結構，避免 controller 直接操作 entity。

#### `filter`

請求攔截層。

- `JwtAuthenticationFilter`
  - 驗證 Bearer Token
  - 解析 JWT
  - 建立登入狀態
  - 回傳 token 過期或無效的錯誤

#### `entryPoint`

安全相關例外的統一輸出點。

- `JwtAuthenticationEntryPoint`
  - 處理未登入或未認證，回 401
- `JwtAccessDeniedHandler`
  - 處理已登入但權限不足，回 403

#### `utils`

工具類別。

- `JwtUtil`
  - 負責 JWT 的產生、解析、驗證、判斷過期

#### `enums`

列舉系統代碼，例如：

- `UNAUTHORIZED`
- `INVALID_TOKEN`
- `TOKEN_EXPIRED`
- `FORBIDDEN`

---

## 三、重要處理事項

這個專案在實作上，有幾個特別重要的觀念。

### 1. JWT 只負責身分認證，不直接等於權限

JWT 代表「你已經登入」。

但你能不能呼叫某支 API，還要再看：

- 這支 API 是否要求登入
- 這支 API 是否限制角色，例如 `ADMIN`

所以 JWT 驗證與權限控管是兩件事：

- JWT 驗證：你是不是合法登入者
- 權限控管：你有沒有資格做這件事

### 2. 沒帶 token 不代表一定錯

在 `JwtAuthenticationFilter` 中，若沒有帶 token，系統不會立刻報錯，而是交給 Spring Security 後續規則判斷。

原因是：

- 有些 API 本來就是公開的，例如登入、註冊、刷新 token
- 有些 API 則需要登入

所以「沒帶 token」要不要回 401，必須交由授權規則決定。

### 3. token 錯誤與 token 過期要分開處理

這個專案目前已將兩者分開：

- `INVALID_TOKEN`
  - token 格式錯誤
  - token 被竄改
  - token 類型錯誤
- `TOKEN_EXPIRED`
  - token 合法，但已超過有效時間

這樣前端才能判斷：

- 是不是應該導回登入頁
- 還是要先走 refresh token 流程

### 4. Refresh Token 要能被撤銷

這個專案不是只把 refresh token 發出去而已，還有把 refresh token 存進資料庫。

原因是：

- 可以在 logout 時撤銷 token
- 可以在 refresh token 使用後做 rotation
- 可以避免舊 refresh token 一直重複使用

---

## 四、JWT 流程

這一段是整個專案最核心的流程。

### 1. 登入並簽發 token

使用者呼叫：

```http
POST /api/login
```

流程如下：

1. `AuthController` 收到登入請求
2. `AuthService` 驗證帳號密碼
3. 驗證成功後，呼叫 `JwtUtil`
4. 產生：
   - Access Token
   - Refresh Token
5. Refresh Token 寫入資料庫
6. 回傳 token 給前端

### 2. 前端攜帶 Access Token 呼叫受保護 API

前端呼叫 API 時，會在 Header 中帶上：

```http
Authorization: Bearer <access_token>
```

### 3. JwtAuthenticationFilter 攔截請求

每次請求進來時，`JwtAuthenticationFilter` 會先做以下事情：

1. 判斷是不是白名單路徑
2. 讀取 `Authorization` Header
3. 判斷是不是 `Bearer ` 開頭
4. 解析 JWT
5. 判斷：
   - 是否為 Access Token
   - 是否過期
   - 是否格式正確
   - 是否能找到對應使用者
6. 驗證成功就建立 `Authentication`
7. 將登入資訊放入 `SecurityContext`

### 4. 過期或錯誤的 token 怎麼處理

若 token 發生問題，filter 會直接回 JSON：

- 過期：`TOKEN_EXPIRED`
- 無效：`INVALID_TOKEN`

這表示：

- token 相關的錯誤，優先在 filter 階段處理
- 不必等到 controller 才發現問題

### 5. Refresh Token 流程

當 access token 過期時，前端可以拿 refresh token 呼叫：

```http
POST /api/refresh
```

流程如下：

1. 後端從資料庫找到該 refresh token
2. 驗證 refresh token 是否合法、是否過期、是否已撤銷
3. 若合法：
   - 舊 refresh token 標記為 revoked
   - 重新產生新的 access token
   - 重新產生新的 refresh token
4. 把新 token 回傳給前端

這就是所謂的 refresh token rotation。

---

## 五、權限管理流程

JWT 驗證完成後，接下來才是 Spring Security 的授權判斷。

### 1. SecurityConfig 的角色

`SecurityConfig` 是整個權限系統的核心設定。

它主要負責：

- 指定公開 API
- 指定需要登入的 API
- 指定需要 `ADMIN` 的 API
- 掛入 JWT Filter
- 設定 401 / 403 的處理器

### 2. 目前授權規則概念

此專案大致可以分成三種 API：

#### 第一種：公開 API

例如：

- `/api/login`
- `/api/register`
- `/api/refresh`
- Swagger 相關路徑

這些 API 不需要 token。

#### 第二種：需要登入的 API

例如一些一般會員功能。

這類 API 的邏輯是：

- 如果 `SecurityContext` 沒有登入資訊
- 就視為未認證，回 401

#### 第三種：需要 `ADMIN` 的 API

例如：

- `/api/addProducts`
- `/api/updateProducts/**`
- `/api/deleteProducts/**`
- `/api/products/manage`

這類 API 的邏輯是：

1. 先確認你有沒有登入
2. 再確認你是不是 `ROLE_ADMIN`

### 3. 401 與 403 的差別

這是教學時一定要講清楚的地方。

#### 401 Unauthorized

表示你尚未通過認證。

常見情況：

- 沒帶 token
- token 無效
- token 過期
- 尚未建立登入狀態

處理位置：

- token 本身錯誤：`JwtAuthenticationFilter`
- 未登入存取受保護資源：`JwtAuthenticationEntryPoint`

#### 403 Forbidden

表示你已經登入，但你沒有權限做這件事。

常見情況：

- 一般使用者去呼叫只有 `ADMIN` 才能使用的 API

處理位置：

- `JwtAccessDeniedHandler`

---

## 六、用教學角度理解整體流程

可以用一句話總結這個專案：

> JWT 負責確認「你是誰」，Spring Security 負責確認「你能做什麼」。

更完整地說：

1. 使用者先登入，拿到 JWT
2. 前端呼叫 API 時帶著 JWT
3. Filter 先驗證 token 是否合法
4. 驗證成功後，把使用者登入資訊放進 SecurityContext
5. Spring Security 再根據 API 規則判斷：
   - 需不需要登入
   - 是否需要 `ADMIN`
6. 最後才進入 Controller 執行真正的商業邏輯

---

## 七、學習此專案時建議先看哪些檔案

若要教學或帶同學閱讀，建議順序如下：

### 第一輪：先理解流程

1. `SecurityConfig`
2. `JwtAuthenticationFilter`
3. `JwtUtil`
4. `JwtAuthenticationEntryPoint`
5. `JwtAccessDeniedHandler`

### 第二輪：理解登入與 refresh

1. `AuthController`
2. `AuthService`
3. `RefreshToken`
4. `RefreshTokenRepository`

### 第三輪：理解角色授權

1. `ProductController`
2. `SecurityConfig`
3. `UserInfo`

---

## 八、總結

這個專案的教學價值在於，它不是只有單純登入而已，而是完整串起：

- Maven 依賴管理
- Spring Boot 分層設計
- JWT 簽發與驗證
- Filter Middleware 攔截請求
- Spring Security 授權控管
- Refresh Token rotation
- 401 / 403 / token 過期等狀況的標準化處理

如果要把這個專案當作 JWT 與 Spring Security 的入門教材，它已經具備很好的骨架。
後續若再補上：

- 完整測試
- 更乾淨的 Swagger 註解
- 統一編碼與註解整理

就會非常適合當成教學示範專案。

