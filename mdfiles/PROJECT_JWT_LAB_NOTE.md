# ECS 專案 JWT 與 Spring Security 說明

## 1. 先用生活化方式理解 JWT

可以把這個專案想成一座遊樂園。

- 使用者先到入口櫃台登入，驗證帳號密碼成功後，園方會發一條手環。
- 這條手環就像 JWT Token，代表你已經通過身分確認。
- 之後你去玩各個設施，不需要每一站都重新出示帳號密碼，只要出示手環，工作人員就知道你是不是已經驗證過的人。
- 如果手環已經過期、被偽造、或格式不對，就不能通行。

管理員身分可以再比喻成園區內部的員工區域。

- 一般遊客可以去公開設施，例如商品查詢。
- 但像員工休息室、後台通道、設備管理區這種地方，不是只要有手環就能進。
- 你還必須有「員工權限」，在這個專案裡就是 `ADMIN` 角色。
- 所以系統不只會判斷「你有沒有登入」，還會判斷「你的角色是不是管理員」。

簡單講：

- JWT 負責回答：你是不是已經登入的人？
- Spring Security 權限控管負責回答：你有沒有資格進這個區域？

---

## 2. 專案用途

這是一個以 Spring Boot 建立的後端服務專案，主要提供：

- 使用者註冊、登入、登出
- JWT Access Token / Refresh Token 的簽發與刷新
- 商品資料查詢、管理、更新、刪除
- 依照登入狀態與角色限制 API 存取權限
- Swagger/OpenAPI 文件查看

目前看起來，這個專案的核心教學重點是：

- 練習 JWT 驗證流程
- 練習 Filter Middleware 攔截請求
- 練習 Spring Security 對特定 API 做授權保護

---

## 3. 專案技術與依賴

依據 [pom.xml](/C:/Users/fx2pt/Documents/STS_WorkSpace/ECS/pom.xml) 可看出此專案主要使用：

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL Connector
- Lombok
- springdoc OpenAPI / Swagger UI
- JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`)

### Maven 在這個專案中的角色

Maven 主要負責：

- 管理所有套件依賴
- 統一專案建置流程
- 管理編譯插件與 Lombok annotation processor
- 讓專案可以用 `mvnw` / `mvnw.cmd` 進行建置

目前這個專案是單一模組 Maven 專案，不是多模組專案。

---

## 4. 專案結構說明

主要程式碼位於 `src/main/java/com/giun/ecs`，可以分成以下幾層：

### `config`

設定區。

- `SecurityConfig.java`
  - 設定 Spring Security 規則
  - 設定哪些 API 可以公開存取
  - 設定哪些 API 需要 `ADMIN`
  - 設定 CORS
  - 將 `JwtAuthenticationFilter` 掛進 Security Filter Chain

### `controller`

API 入口層，負責接收 HTTP Request。

- `AuthController.java`
  - 處理登入、註冊、refresh token、個人資料等 API
- `ProductController.java`
  - 處理商品查詢、更新、刪除等 API

### `service`

商業邏輯層。

- `AuthService.java`
  - 驗證登入資訊
  - 產生 access token / refresh token
  - 處理 refresh token rotation
- `ProductService.java`
  - 商品相關邏輯
- `UserService.java` / `UserServiceImpl.java`
  - 使用者資料處理

### `repository`

資料存取層，透過 JPA 與資料庫互動。

- `UserRepository.java`
- `ProductRepository.java`
- `RefreshTokenRepository.java`

### `entity`

資料表對應的實體類別。

- `UserInfo`
- `Product`
- `RefreshToken`

### `dto`

資料傳輸物件，讓 request / response 格式更清楚。

- `dto/request`
- `dto/response`

### `filter`

請求攔截層。

- `JwtAuthenticationFilter.java`
  - 每次 API 請求進來時，會先檢查有沒有 Bearer Token
  - 若 token 合法，建立登入狀態到 `SecurityContext`
  - 若 token 過期或無效，直接回傳 401 JSON

### `entryPoint`

安全驗證失敗時的統一回應位置。

- `JwtAuthenticationEntryPoint.java`
  - 處理「未登入或未認證」的情況，回 401
- `JwtAccessDeniedHandler.java`
  - 處理「已登入但權限不足」的情況，回 403

### `utils`

工具類別。

- `JwtUtil.java`
  - 產生 JWT
  - 解析 Claims
  - 驗證 token 類型與過期時間

### `exception`

例外處理。

- `ApplicationException.java`
- `GlobalExceptionHandler.java`

### `enums`

列舉常數。

- `ResultCode.java`
  - 定義系統回傳代碼，例如 `UNAUTHORIZED`、`INVALID_TOKEN`、`TOKEN_EXPIRED`

---

## 5. JWT 在本專案中的用途與流程

### 5.1 登入並取得 Token

使用者呼叫登入 API：

- `POST /api/login`

登入成功後，系統會在 `AuthService` 中：

- 驗證帳號密碼
- 呼叫 `JwtUtil` 產生 Access Token
- 呼叫 `JwtUtil` 產生 Refresh Token
- 將 Refresh Token 寫入資料庫
- 把 token 回傳給前端

### 5.2 呼叫其他 API 時帶著 Token

前端之後呼叫受保護 API，要在 Header 帶上：

```http
Authorization: Bearer <access_token>
```

### 5.3 Filter 進行攔截與驗證

每次請求進入系統後，`JwtAuthenticationFilter` 會先處理：

1. 檢查是不是白名單路徑
2. 讀取 `Authorization` header
3. 判斷是不是 `Bearer ` 開頭
4. 驗證 token 是否為 Access Token
5. 驗證 token 是否過期、是否被竄改、使用者是否存在
6. 若通過，就把使用者資訊與角色寫入 `SecurityContext`

### 5.4 Spring Security 做授權判斷

當 `SecurityContext` 裡已經有登入資訊後，Spring Security 會根據 `SecurityConfig` 裡的規則判斷：

- 此 API 是否要登入
- 此 API 是否限定 `ADMIN`

例如：

- `/api/login`、`/api/register`、`/api/refresh` 是公開 API
- `/api/updateProducts/**`、`/api/deleteProducts/**` 等路徑需要 `ADMIN`

### 5.5 Token 過期與錯誤處理

目前專案已經將 token 錯誤拆成比較清楚的兩種：

- `INVALID_TOKEN`
  - token 格式錯誤
  - token 類型不正確
  - token 被竄改或無法解析
- `TOKEN_EXPIRED`
  - token 已超過有效期限

前端可以依照回傳 code 決定：

- 是否要導向登入頁
- 是否要走 refresh token 流程

---

## 6. Spring Security 設定重點

依據 [SecurityConfig.java](/C:/Users/fx2pt/Documents/STS_WorkSpace/ECS/src/main/java/com/giun/ecs/config/SecurityConfig.java)，此專案的重要設定如下：

### 6.1 關閉 CSRF

```java
http.csrf(csrf -> csrf.disable())
```

因為這是以前後端分離 API 為主的專案，主要透過 JWT 驗證，不依賴傳統 session 表單機制。

### 6.2 啟用 CORS

```java
.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

搭配 `CorsConfigurationSource`，目前允許來源：

- `http://localhost:5173`

這代表前端本機開發時可跨域呼叫後端 API。

### 6.3 設為 Stateless

```java
session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
```

表示伺服器端不保存 session，登入狀態改由 JWT 自行攜帶。

### 6.4 掛入 JWT Filter

```java
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

表示在 Spring Security 原本的帳密認證流程之前，先執行 JWT 驗證。

### 6.5 保護特定端點

目前權限規則大致如下：

- 公開 API
  - `/api/login`
  - `/api/register`
  - `/api/refresh`
  - Swagger 相關路徑
- 一般公開查詢
  - `GET /api/products/**`
- 需要 `ADMIN`
  - `/api/products/manage`
  - `/api/addProducts`
  - `/api/updateProducts/**`
  - `/api/deleteProducts/**`
- 其他未列出的 API
  - 需登入才可存取

---

## 7. 主要設定檔用途

### `application.properties`

位於 [application.properties](/C:/Users/fx2pt/Documents/STS_WorkSpace/ECS/src/main/resources/application.properties)

用途：

- 設定資料庫連線
- 設定 JPA 行為
- 設定 JWT secret
- 設定 Access Token / Refresh Token 有效時間
- 設定 Swagger 路徑

目前重要設定如下：

- `jwt.secret`
  - JWT 簽章密鑰
- `jwt.access-expiration-ms=3600000`
  - Access Token 有效期 1 小時
- `jwt.refresh-expiration-ms=604800000`
  - Refresh Token 有效期 7 天

### `pom.xml`

用途：

- 定義專案依賴
- 設定 Java 版本
- 設定套件建置方式
- 管理插件

### `JWT_SECURITY_MECHANISM.md`

這份文件原本就在專案中，主要是 JWT 與 Spring Security 機制說明。不過目前內容有部分編碼亂碼，若之後要對外展示，建議整理成 UTF-8 後再使用。

---

## 8. 本專案目前是否達成 Lab 目標

以下依照題目逐項檢查。

### 目標 1

`掌握 Maven 專案依賴管理與多模組設定。`

檢查結果：

- Maven 依賴管理：有達成
- 多模組設定：尚未達成

原因：

- 專案已有 `pom.xml` 管理 Spring Boot、JPA、Security、JWT、Swagger 等依賴
- 但目前只有單一 Maven 模組，沒有 parent-child modules 結構
- 沒看到 `<modules>` 設定，也沒有拆分成多個子專案

結論：

- 這一項屬於「部分達成」

### 目標 2

`理解 JWT (JSON Web Token) 的簽發、校驗與生命週期管理。`

檢查結果：

- 有達成

原因：

- 有 Access Token 與 Refresh Token 的產生流程
- 有 `JwtUtil` 處理 token 簽發與解析
- 有 token 過期時間設定
- 有 refresh token rotation 機制
- 有 logout 時撤銷 refresh token 的設計
- 現在也有把 `TOKEN_EXPIRED` 與 `INVALID_TOKEN` 分開回傳

### 目標 3

`實作 Filter Middleware，攔截 API 請求並進行身分合法性檢查。`

檢查結果：

- 有達成

原因：

- `JwtAuthenticationFilter` 已繼承 `OncePerRequestFilter`
- 會攔截請求並解析 Bearer Token
- 會驗證 token 是否合法、是否過期
- 驗證成功後會把使用者資訊寫進 `SecurityContext`
- 驗證失敗會回傳 401 JSON

### 目標 4

`配置 Spring Security 以保護特定端點（Endpoints）。`

檢查結果：

- 有達成

原因：

- 已透過 `SecurityFilterChain` 設定授權規則
- 已設定公開 API 與受保護 API
- 已限制部分 API 必須為 `ADMIN`
- 已配置 `AuthenticationEntryPoint` 與 `AccessDeniedHandler`

---

## 9. 總結

這個專案已經具備一個完整的 JWT 驗證與 Spring Security 保護流程，核心能力包含：

- 使用者登入後簽發 Access Token / Refresh Token
- 用 Filter 攔截請求並驗證 JWT
- 用 Spring Security 控制 API 是否需要登入
- 用角色控管管理員才能操作的功能
- 用統一 JSON 格式回傳未登入、token 錯誤、token 過期、權限不足等狀況

若以 Lab 的角度來看，JWT、Filter Middleware、Spring Security 這三塊已經有明確成果；較明顯還沒完整達成的是「多模組 Maven 專案」這一項。

---

## 10. 建議後續可補強項目

- 將 `ResultCode.fromCode()` 的比對 bug 修正為 `value.getCode().equals(code)`
- 增加單元測試與整合測試，驗證 401 / 403 / TOKEN_EXPIRED 行為
- 將 `JWT_SECURITY_MECHANISM.md` 的亂碼內容整理成 UTF-8
- 若要符合多模組 Lab 目標，可拆成：
  - `ecs-api`
  - `ecs-security`
  - `ecs-common`
  - `ecs-data`

