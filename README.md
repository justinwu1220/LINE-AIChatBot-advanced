# LINE-AIChatBot-advanced
基於JAVA Spring Boot、LINE Developer Messaging API、OpenAI API、CWA opendata API 實現的 Line AI 聊天機器人

此聊天機器人為進階版本，基礎版請前往 : <https://github.com/justinwu1220/LINE-AIChatBot>

## 技術
- LineBot :
  - 使用LINE Developer建立機器人
    
- JAVA Spring Boot:
  - MVC架構實作機器人後端程式
  - JDBC連接資料庫
    
- MySQL :
  - 建立用戶資料庫
    
- 第三方 api :
  - 串接OpenAI API 實現ai回覆訊息
  - 串接CWA opendata API 取得天氣預報
 
## 功能
* 輸入任意文字訊息，可得到ai回覆

<img src="https://github.com/justinwu1220/LINE-AIChatBot/blob/main/img/S__4399116.jpg" width="300"> <img src="https://github.com/justinwu1220/LINE-AIChatBot/blob/main/img/S__4399119.jpg" width="300">
<br/><br/><br/><br/>

* 輸入 今日天氣 可取得當天逐3小時天氣預報
* 輸入 今明天氣 可取得當天與隔天逐3小時天氣預報
* 預報地區預設為 臺北市 中正區

<img src="https://github.com/justinwu1220/LINE-AIChatBot-advanced/blob/main/img/S__4399135_0.jpg" width="300"> <img src="https://github.com/justinwu1220/LINE-AIChatBot-advanced/blob/main/img/S__4399134_0.jpg" width="300"> <img src="https://github.com/justinwu1220/LINE-AIChatBot-advanced/blob/main/img/S__4399132_0.jpg" width="300">
<br/><br/><br/><br/>

* 輸入 設定地區 縣市 鄉鎮市區 可將用戶地區設為指定地區
* 天氣預報功能將使用新地區作為查詢依據

<img src="https://github.com/justinwu1220/LINE-AIChatBot-advanced/blob/main/img/S__4399147_0.jpg" width="300"> <img src="https://github.com/justinwu1220/LINE-AIChatBot-advanced/blob/main/img/S__4399145_0.jpg" width="300">
<br/><br/><br/><br/>

## 如何使用
#### Line Bot
* 進入: <https://manager.line.biz> 建立chatbot的帳號
* 按照步驟建立channel
* 記下Basic settings中的Channel secret，以及Messaging API中的Channel access token
* 到Auto-reply messages中，disable自動回應訊息，啟動Webhook
  
#### OpenAI API
* 進入: <https://openai.com/index/openai-api>
* 創建帳號並取得openAi api key

#### CWA opendata API
* 進入: <https://opendata.cwa.gov.tw/index>
* 創建帳號並取得API授權碼

#### MySQL
* 建立資料庫
* SpringBoot程式會根據Line Messaging API 獲取使用者ID，並建立資料
```sql
CREATE DATABASE linebot;
CREATE TABLE user
(
    user_id         VARCHAR(128)        NOT NULL        PRIMARY KEY,
    city            VARCHAR(128)        NOT NULL,
    district        VARCHAR(128)        NOT NULL
);
```

#### Git clone 程式
在LINE-AIChatBot/src/main/resources/application.properties中
* 新增line.bot.channel-token和line.bot.channel-secret
* 新增openAi.api.key
* 新增cwa.api.token
* 新增MySQL url
```properties
spring.application.name=LINE-AIChatBot

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

server.port=8080

line.bot.channel-token=
line.bot.channel-secret=
line.bot.handler.enabled=true
line.bot.handler.path=/callback

openAi.api.key=
openAi.api.url=https://api.openai.com/v1/chat/completions

cwa.api.token=

```
* 執行SpringBoot程式

#### ngrok
* 使用ngrok建立一個臨時的https網址(接收Line Bot Webhook 需要https協定)
* 下載ngrok，並解壓縮
* 打開檔案，並輸入指令
```
ngrok http 8080
```
* 把ngrok中拿到的網址後面加上/callback
* 將網址貼在Messaging API底下的Webhook URL，同時開啟Use webhook
* 按下Verify，確定是否出現Success(需先執行SpringBoot程式)

#### 運行
* 確定出現Success
* 加入Line bot好友
* 傳送訊息查看是否得到回覆
