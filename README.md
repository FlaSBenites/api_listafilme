# Movie Awards API

Este é um projeto de API RESTful para gerenciar informações sobre filmes e seus respectivos produtores no contexto dos Golden Raspberry Awards. 
A API permite operações básicas de CRUD para filmes e fornece informações sobre os intervalos de premiação dos produtores.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.3.4
- H2 Database (em memória)
- JPA (Hibernate)
- SLF4J para logging

## Pré-requisitos

Certifique-se de ter os seguintes pré-requisitos instalados:

- [Java JDK 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- [Maven](https://maven.apache.org/download.cgi) (para gerenciamento de dependências)
- [IDE](https://www.jetbrains.com/idea/) como IntelliJ IDEA ou Eclipse (opcional)

## Instalação

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/FlaSBenites/api_listafilme.git
   cd api_listafilme

## Endpoints Disponíveis
Listar todos os filmes:

- GET /api/movies

Obter detalhes de um filme específico:

- GET /api/movies/{id}

Listar intervalos de premiação dos produtores:

- GET /api/movies/intervals

## Testando a API
Você pode usar ferramentas como Postman ou cURL para testar os endpoints.

### Exemplo de chamada com cURL:

1. Listar todos os filmes:

bash
curl -X GET http://localhost:8080/api/movies

2. Obter detalhes de um filme:

bash
curl -X GET http://localhost:8080/api/movies/1

3. Listar intervalos de premiação dos produtores:

bash
curl -X GET http://localhost:8080/api/movies/intervals

## Contribuições
Sinta-se à vontade para enviar um pull request ou abrir um issue se você encontrar algum problema.
