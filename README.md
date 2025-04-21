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

1. Clone o repositório:
   ```bash
   git clone git@github.com:Nmartins6/startup-rush-backend.git
   cd startup-rush
   ```

2. Vá para o diretório do projeto:
   ```bash
   cd startuprush
   ```

3. Rode o projeto com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Acesse a documentação interativa no navegador:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## ✅ Como testar o projeto

Você pode usar o Swagger para testar todos os endpoints, mas aqui estão os passos gerais:

1. **Cadastrar startups**:  
   Use o endpoint `POST /api/startups` com nome, slogan e foundationYear.
```json
{
  "name": "InovaMax",
  "slogan": "Building the future",
  "foundationYear": 2023
}
```

2. **Iniciar torneio**:  
   Após cadastrar entre 4 e 8 startups, chame `POST /api/battles/start-tournament`.

3. **Consultar batalhas pendentes**:  
   Use `GET /api/battles/pending`.

4. **Aplicar eventos a uma batalha**:  
   Use `POST /api/battles/events` passando os eventos da batalha.
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

5. **Ver relatório geral**:  
   Use `GET /api/battles/report`.

6. **Ver histórico por startup**:  
   Use `GET /api/startups/{id}/history`.

7. **Ver campeã**:  
   Use `GET /api/battles/champion`.

## 🔎 Banco de dados H2

Durante a execução, você pode acessar o banco em:
```
http://localhost:8080/h2-console
```

Use os dados:
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: *(em branco)*

## 🧪 Rodar os testes

```bash
./mvnw test
```

---