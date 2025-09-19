# Blog "Codigo com Café" - Backend

API REST construída com Java e Spring Boot, usando PostgreSQL como banco de dados. Suporta Swagger para documentação, upload de imagens e vídeos, e gerenciamento completo de Posts e Categorias.

### 🚀 Tecnologias

- Java 17+

- Spring Boot

- Spring Data JPA

- PostgreSQL

- Swagger / OpenAPI

- Maven

- Lombok

- Spring Web / Spring MVC

- MultipartFile (Upload de arquivos)

### 📦 Funcionalidades

- Posts

   - CRUD completo (Create, Read, Update, Delete)

   - Upload de imagem e vídeo

   - Controle de status (PUBLICADO, RASCUNHO, etc.)

- Categorias

  - CRUD completo

  - Enum TipoCategoria

  - Integração total com PostgreSQL

- Documentação via Swagger (/swagger-ui.html)

- Cross-Origin habilitado (@CrossOrigin("*"))

📂 Estrutura do Projeto
```bash
src/
├─ main/
│  ├─ java/
│  │  └─ br/com/codigocomcafe/
│  │     ├─ controller/       # Controllers REST
│  │     ├─ model/            # Entidades JPA
│  │     ├─ service/          # Lógica de negócio
│  │     ├─ repository/       # Repositórios Spring Data
│  │     └─ config/           # Configurações (Swagger, etc)
│  └─ resources/
│     ├─ application.properties  # Configurações Spring e DB
│     └─ static/                 # Uploads (opcional)
```

## 🗂 Endpoints
Posts
| Método	| Endpoint	| Descrição |
| :---      | :---:     | ---:      |
|POST       |/api/posts	|Criar post(multipart/form-data)|
|GET	    |/api/posts	|Listar todos os posts|
|GET	|/api/posts/{id}	|Buscar post por |
|PUT	|/api/posts/{id}	|Atualizar post|
|DELETE	|/api/posts/{id}	|Deletar post por ID|
Categorias
|Método	|Endpoint	|Descrição|
| :---  | :---:     | ---:      |
|POST	|/api/categorias	|Criar nova categoria|
|GET	|/api/categorias	|Listar todas as categorias|
|GET	|/api/categorias/{id}	|Buscar categoria por ID|
|PUT	|/api/categorias/{id}	|Atualizar categoria|
|DELETE	|/api/categorias/{id}|	Deletar categoria por ID|

## 📌 Exemplo JSON para Categorias
{
  "nome": "Tecnologia",
  "descricao": "Posts relacionados a tecnologia",
  "tipoCategoria": "TECNOLOGIA"
}

## 📌 Exemplo JSON para Posts (sem arquivo)
{
  "titulo": "Meu primeiro post",
  "conteudo": "Conteúdo completo do post",
  "status": "PUBLICADO",
  "categoriaId": 1
}


Observação: Imagem e vídeo devem ser enviados via multipart/form-data.

## 📤 Exemplo de upload de imagem e vídeo via curl
```bash
curl -X POST "http://localhost:8080/api/posts" \
  -H "Content-Type: multipart/form-data" \
  -F "titulo=Meu Post com Imagem" \
  -F "conteudo=Conteúdo do post" \
  -F "status=PUBLICADO" \
  -F "categoriaId=1" \
  -F "imagem=@/caminho/para/imagem.jpg" \
  -F "video=@/caminho/para/video.mp4"

  ```

## ⚙️ Configuração do banco (application.properties)
``` bash
spring.datasource.url=jdbc:postgresql://localhost:5432/codigo_com_cafe
spring.datasource.username=postgres
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Limite de upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## 🏃 Executando a aplicação

1 - Clone o repositório:
``` bash
git clone https://github.com/seu-usuario/codigo-com-cafe.git
```

2 - Configure o PostgreSQL e atualize application.properties.

3 - Build e run:
```bash
mvn clean install
mvn spring-boot:run
```

4 - Acesse a documentação Swagger:
```bash
http://localhost:8080/swagger-ui.html
```
## 📌 Observações

- CrossOrigin("*") habilitado para permitir requisições de qualquer frontend.

- Para arquivos grandes, ajuste max-file-size e max-request-size.

- Enum TipoCategoria deve ser enviado como string (ex: "TECNOLOGIA").