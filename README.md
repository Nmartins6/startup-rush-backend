# 🚀 Startup Rush

Projeto desenvolvido para o desafio técnico **Startup Rush**, que simula um torneio eliminatório entre startups. Cada batalha é influenciada por eventos que afetam a pontuação das startups.

## 🧰 Tecnologias

- Java 21
- Spring Boot 3
- H2 Database (em memória)
- Maven
- Swagger/OpenAPI
- JUnit + Mockito

## 📦 Requisitos

- Java 21 instalado
- Maven 3.8+ instalado
- IDE (IntelliJ, VSCode ou Eclipse)

## ▶️ Como rodar o projeto

### 🟢 Executar normalmente (sem dados)

```bash
./mvnw spring-boot:run
```

Acesse:
```
http://localhost:8080/swagger-ui/index.html
```

---

### 🧪 Executar com seed (modo dev)

Executa o projeto com um endpoint adicional que permite popular o banco automaticamente com 4, 6 ou 8 startups e simular um torneio completo.

#### Linux/macOS:
```bash
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

#### Windows PowerShell:
```bash
./mvnw spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

#### Windows CMD:
```cmd
mvnw spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

Depois de rodar, acesse o endpoint:
```
POST http://localhost:8080/api/dev/seed?quantity=6
```

> 🔢 `quantity` atualmente pode ser 4, 6 ou 8

---

## ✅ Como testar o projeto

Você pode usar o Swagger para testar todos os endpoints:

1. **Cadastrar startups**  
   `POST /api/startups`
   ```json
   {
     "name": "InovaMax",
     "slogan": "Building the future",
     "foundationYear": 2023
   }
   ```

2. **Iniciar torneio**  
   `POST /api/battles/start-tournament`

3. **Ver batalhas pendentes**  
   `GET /api/battles/pending`

4. **Aplicar eventos à batalha**  
   `POST /api/battles/events`
   ```json
   {
     "battleId": 1,
     "eventsForStartupA": [
       { "type": "PITCH" },
       { "type": "USER_TRACTION" }
     ],
     "eventsForStartupB": [
       { "type": "BUG" },
       { "type": "FAKE_NEWS" }
     ]
   }
   ```

5. **Relatório geral**  
   `GET /api/battles/report`

6. **Histórico por startup**  
   `GET /api/startups/{id}/history`

7. **Campeã do torneio**  
   `GET /api/battles/champion`

---

## 🔎 Banco de dados H2

Acesse:
```
http://localhost:8080/h2-console
```

Credenciais:
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: *(em branco)*

---

## 🧪 Rodar os testes

```bash
./mvnw test
```
