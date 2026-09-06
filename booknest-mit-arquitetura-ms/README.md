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

### booknest-emprestimo-ms (candidato a serviço independente)
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
- `booknest-usuario-ms`, porque o empréstimo precisa validar o usuário e consultar seu histórico;
- `booknest-catalogo-ms`, porque o empréstimo depende de exemplares e da disponibilidade do acervo.

### booknest-catalogo-ms (candidato a serviço independente)
O módulo de catálogo concentra o acervo e a estrutura bibliográfica da aplicação.

Responsabilidade:
- cadastrar e manter livros, autores, categorias, editoras e exemplares;
- organizar o acervo e suas relações;
- fornecer a base consultada pelas operações de empréstimo.

Por que poderia ser executado separadamente:
- possui um domínio próprio, com regras específicas de cadastro e consulta;
- pode evoluir de forma independente conforme o acervo crescer;
- serve de base para outros fluxos sem depender diretamente das regras de empréstimo.

Quais partes da aplicação dependem dele hoje:
- `booknest-emprestimo-ms`, que precisa consultar livros e exemplares disponíveis;
- `booknest-usuario-ms`, indiretamente, quando as regras de negócio envolvem o contexto do acervo.

### booknest-usuario-ms (candidato a serviço independente)
O módulo de usuário centraliza os dados cadastrais e o relacionamento com os empréstimos.

Responsabilidade:
- cadastrar e manter os usuários da biblioteca;
- controlar dados de identificação e status;
- expor o histórico de empréstimos do usuário.

Por que poderia ser executado separadamente:
- tem ciclo de vida próprio e regras de manutenção independentes;
- pode evoluir sem alterar o módulo de empréstimos ou catálogo;
- é uma responsabilidade clara de identidade e cadastro.

Quais partes da aplicação dependem dele hoje:
- `booknest-emprestimo-ms`, que valida o usuário ao registrar empréstimos;
- `booknest-catalogo-ms`, quando o contexto da biblioteca exige associação com o usuário.

## 🔗 Dependências entre módulos

A dependência principal do sistema é:

- `booknest-emprestimo-ms` → `booknest-catalogo-ms`
- `booknest-emprestimo-ms` → `booknest-usuario-ms`

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
