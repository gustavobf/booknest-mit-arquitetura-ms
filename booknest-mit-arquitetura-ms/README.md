# 📚 Booknest - Sistema de Gestão de Biblioteca

Aplicação Spring Boot para gerenciamento de acervo, usuários e empréstimos de uma biblioteca. A estrutura foi revisada para refletir a organização por domínio.

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

### Comunicação (candidato a serviço independente)
Ainda não é um microserviço separado. Esta é a funcionalidade escolhida como candidata para futura extração.

Responsabilidade:
- enviar lembretes, alertas e avisos relacionados a empréstimos, atrasos e pendências.

Por que poderia ser executada separadamente:
- é uma funcionalidade transversal, com regras próprias e menor acoplamento ao fluxo principal de cadastro e consulta.
- oferece potencial de evolução independente, como envio por e-mail, notificações internas ou integrações externas.

Quais partes da aplicação dependem dela hoje:
- módulo de `emprestimo`, que precisa notificar pendências e atrasos;
- módulo de `usuario`, quando é necessário avisar o usuário sobre ações pendentes;
- o módulo de catálogo pode se relacionar com essa funcionalidade quando houver avisos sobre disponibilidades ou necessidades de comunicação.

Observação importante: integração externa, como consulta de CEP, é uma preocupação técnica e transversal, e não um módulo de negócio da biblioteca.

## 🔗 Dependências entre módulos

A dependência principal do sistema é:

- Empréstimo → Catálogo
- Empréstimo → Usuário

Exemplo: para registrar um empréstimo, a aplicação precisa consultar:
- qual usuário está solicitando o empréstimo
- qual exemplar está disponível
- qual livro e sua condição pertencem ao exemplar

Essa relação mostra o acoplamento natural entre as responsabilidades de negócio.

## 📘 Documentação da API

A aplicação usa Springdoc OpenAPI / Swagger.

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
