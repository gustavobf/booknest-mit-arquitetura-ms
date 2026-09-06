# 📚 Booknest - Sistema de Gestão de Biblioteca

Aplicação Spring Boot para gerenciamento de acervo, usuários e empréstimos de uma biblioteca.

## 🧩 Módulos da aplicação

### Catálogo
Responsável por manter o acervo da biblioteca, incluindo:
- livros
- autores
- categorias
- editoras
- exemplares

Pacotes principais:
- `br.edu.infnet.gustavo_figueiredo_api.catalogo.controller`
- `br.edu.infnet.gustavo_figueiredo_api.catalogo.service`
- `br.edu.infnet.gustavo_figueiredo_api.catalogo.repository`
- `br.edu.infnet.gustavo_figueiredo_api.catalogo.model`

### Usuário
Responsável pelo cadastro e manutenção dos usuários da biblioteca.

Pacotes principais:
- `br.edu.infnet.gustavo_figueiredo_api.usuario.controller`
- `br.edu.infnet.gustavo_figueiredo_api.usuario.service`
- `br.edu.infnet.gustavo_figueiredo_api.usuario.repository`
- `br.edu.infnet.gustavo_figueiredo_api.usuario.model`

### Empréstimo
Responsável pelas regras de empréstimo, devolução e controle da disponibilidade dos exemplares.

Pacotes principais:
- `br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller`
- `br.edu.infnet.gustavo_figueiredo_api.emprestimo.service`
- `br.edu.infnet.gustavo_figueiredo_api.emprestimo.repository`
- `br.edu.infnet.gustavo_figueiredo_api.emprestimo.model`

### Empréstimo (candidato a serviço independente)
O módulo de empréstimo concentra regras de negócio que futuramente poderiam ser extraídas.

Responsabilidade:
- registrar empréstimos;
- controlar devoluções;
- verificar atrasos;
- atualizar a disponibilidade dos exemplares.

Por que poderia ser executado separadamente:
- possui regras próprias e ciclo de vida independente;
- conversa com usuários e catálogo, mas encapsula uma responsabilidade clara de negócio;
- pode crescer com notificações, multas e políticas de empréstimo sem alterar o restante da aplicação.

Quais partes da aplicação dependem dele hoje:
- o módulo de usuário, porque o empréstimo precisa validar o usuário e consultar seu histórico;
- o módulo de catálogo, porque o empréstimo depende de exemplares e da disponibilidade do acervo.

## 🔗 Dependências entre módulos

A dependência principal do sistema é:

- Empréstimo → Catálogo

Exemplo: para registrar um empréstimo, a aplicação precisa consultar:
- qual usuário está solicitando o empréstimo;
- qual exemplar está disponível;
- qual livro pertence ao exemplar.

Essa relação mostra o acoplamento natural entre as responsabilidades de negócio.

## 📘 Documentação da API

A aplicação usa Springdoc OpenAPI / Swagger.

Principais recursos documentados:
- `GET /autores`, `POST /autores`, `GET /autores/{id}`;
- `GET /livros`, `POST /livros`, `GET /livros/{id}`;
- `GET /usuarios`, `POST /usuarios`, `GET /usuarios/{id}`, `GET /usuarios/{id}/emprestimos`;
- `GET /emprestimos`, `POST /emprestimos`, `GET /emprestimos/{id}`, `PATCH /emprestimos/{id}/devolucao`;
- `GET /ceps/{cep}`;
- `GET /categorias`, `GET /editoras`, `GET /exemplares`.

Acesse:
- `http://localhost:8080/swagger-ui/index.html`

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven 3.6+

### Comandos

```bash
./mvnw clean compile
./mvnw spring-boot:run
```
---
