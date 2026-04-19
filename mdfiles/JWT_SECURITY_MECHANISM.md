# JWT 與 Spring Security 機制說明

本文件說明本專案目前已完成的三項安全機制：

1. JWT 簽發 / 校驗 / 基本生命週期（過期）
2. Filter Middleware 攔截與身分檢查
3. Spring Security 保護特定端點

---

## 1) JWT 簽發 / 校驗 / 基本生命週期（過期）

### 實作位置

- `src/main/java/com/giun/ecs/service/AuthService.java`
- `src/main/java/com/giun/ecs/utils/JwtUtil.java`
- `src/main/resources/application.properties`

### 機制說明

#### A. 簽發（Issue）

使用者呼叫登入 API（`/api/login`）後，後端會：

1. 先驗證帳號存在與密碼正確。
2. 驗證成功後呼叫 `jwtUtil.generateToken(userInfo)` 建立 JWT。
3. 回傳 JWT 字串給前端，前端後續請求需帶上 `Authorization: Bearer <token>`。

JWT 內容包含：

- `sub`（Subject）：使用者帳號（username）
- `role`：使用者角色
- `iat`（Issued At）：簽發時間
- `exp`（Expiration）：過期時間

並使用 `HS256` 搭配 `jwt.secret` 進行簽章。

#### B. 校驗（Validate）

收到 JWT 後會透過 `JwtUtil` 做以下檢查：

1. 使用同一把 secret 驗證簽章是否正確。
2. 解析 token 取得 subject（username）。
3. 檢查 token 是否過期（`exp`）。
4. 驗證 token 中 username 與資料庫查到的使用者是否一致。

若簽章錯誤、格式錯誤、過期，會視為無效 token。

#### C. 生命週期（Lifecycle）

目前已實作「**Access Token 過期控制**」：

- 設定鍵：`jwt.expiration-ms`
- 目前預設：`3600000`（1 小時）

也就是 token 到期後不能再通過驗證，需重新登入取得新 token。

目前已包含基本的 Refresh Token 生命週期管理：

- 登入簽發 `accessToken + refreshToken`
- `POST /api/refresh` 進行 token rotation（舊 refresh token 撤銷，發新的一組）
- `POST /api/logout` 會將該使用者所有有效 refresh token 撤銷

---

## 2) Filter Middleware 攔截與身分檢查

### 實作位置

- `src/main/java/com/giun/ecs/filter/JwtAuthenticationFilter.java`
- `src/main/java/com/giun/ecs/config/SecurityConfig.java`

### 機制說明

`JwtAuthenticationFilter` 繼承 `OncePerRequestFilter`，代表每個請求只會執行一次。流程如下：

1. 從 Header 讀取 `Authorization`。
2. 判斷是否為 `Bearer ` 開頭。
3. 取出 token，呼叫 `JwtUtil` 解析與驗證。
4. 查詢使用者資料（`UserService.findUserByUsername`）。
5. 驗證成功後建立 `UsernamePasswordAuthenticationToken`。
6. 將 Authentication 放入 `SecurityContextHolder`，讓後續流程可識別「此請求已登入」。

若 token 驗證失敗（例如過期或簽章錯誤），filter 會回傳 `401 Unauthorized`。

---

## 3) Spring Security 保護特定端點

### 實作位置

- `src/main/java/com/giun/ecs/config/SecurityConfig.java`

### 機制說明

`SecurityConfig` 目前採用 Spring Security 的 `SecurityFilterChain`，重點如下：

1. 關閉 CSRF（API 型服務常見設定）。
2. 啟用 CORS 設定（允許前端來源呼叫）。
3. Session 設為 `STATELESS`（JWT 架構不依賴伺服器 session）。
4. 將 `JwtAuthenticationFilter` 加在 `UsernamePasswordAuthenticationFilter` 之前。
5. 授權規則：
   - 白名單（不需登入）：`/api/login`、`/api/register`、Swagger 文件端點
   - 其餘端點：`anyRequest().authenticated()`，必須通過 JWT 驗證

這表示：

- 未帶 token 或 token 無效 → 不能存取受保護端點
- 帶有效 token → 可存取受保護端點

---

## 整體請求流程（端到端）

1. 使用者登入 `POST /api/login` 取得 JWT。
2. 前端呼叫受保護 API 時附上 `Authorization: Bearer <token>`。
3. 請求先進入 `JwtAuthenticationFilter`：
   - 解析並驗證 token
   - 驗證成功就把身分放入 `SecurityContext`
4. Spring Security 根據授權規則判斷：
   - 公開端點直接放行
   - 受保護端點需已驗證身分才放行
5. 到期或無效 token 會被拒絕（401 / 未通過授權）。

---

## 目前能力與邊界

### 已具備

- JWT 簽發與簽章保護
- JWT 驗證與過期判斷
- Refresh Token 續簽與撤銷
- Filter 層攔截與身分注入
- 端點授權保護（白名單 + 其餘需驗證）
- 角色權限控管（`ADMIN` 才可操作管理型產品端點）

### 可再擴充（建議）

- Refresh Token 機制（延長登入體驗）
- Logout 撤銷機制（blacklist 或 token version）
- 更細粒度角色權限控管（`hasAuthority` 到功能層級）
- 統一例外格式（401/403 回應結構一致化）
