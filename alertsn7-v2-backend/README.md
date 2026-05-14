# Backend Alertsn7_v2

#### Config Backend
Add "aplication.properties"
```bash
# App
spring.application.name=trading_room_backend

# SQLite
#db Prod Docker
#spring.datasource.url=jdbc:sqlite:/app/data/alertsn7.db

# Para desarrollo local (ruta relativa al directorio del proyecto)
spring.datasource.url=jdbc:sqlite:alertsn7.db

spring.datasource.driver-class-name=org.sqlite.JDBC

# Hibernate / JPA
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8077
telegram.bot.token=
telegram_chat_client_id=

spring.datasource.hikari.maximum-pool-size=1
spring.datasource.hikari.connection-timeout=20000
```
