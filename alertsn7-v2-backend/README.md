# Backend Alertsn7_v2

#### Config Backend
Add "aplication.properties"
```bash
# --- Config Database ---
spring.application.name=trading_room_backend
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/db_name
spring.datasource.username=root
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect


# --- Config Bot Telegram - para avisos ---
# Token bot Telegram
telegram.bot.token=
# Id del chat donde se enviaran los avisos
telegram_chat_client_id=

# Configs del servidor
server.port=8077
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=3600000
```
