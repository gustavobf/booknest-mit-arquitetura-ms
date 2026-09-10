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
O módulo de empréstimo foi o candidato identificado na Etapa 1 e concentra regras de negócio que podem ser extraídas com pouca dependência estrutural do restante da aplicação.

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

## 🧱 Microsserviço de empréstimos

A funcionalidade de empréstimo foi extraída para um serviço independente em `booknest-emprestimo-ms`.

- Nome do serviço: Booknest Empréstimo MS
- Responsabilidade: criar, consultar, atualizar e registrar devolução de empréstimos
- Funcionalidade separada: módulo de empréstimo da API principal
- Motivo: regras próprias de negócio, ciclo de vida independente e comunicação via rede

A aplicação principal agora chama esse serviço por HTTP usando OpenFeign, configurado pelo valor `servico.emprestimo.url` em `application.properties`.

### Reflexão arquitetural
- Funcionalidade separada: empréstimo.
- Motivo da escolha: possui responsabilidade clara e limites bem definidos.
- O que ficou mais complexo: a comunicação entre aplicações passou a depender de rede, latência e disponibilidade.
- Se o serviço ficar indisponível: a aplicação principal responde com erro amigável de indisponibilidade, sem expor detalhes internos da falha, e a operação de empréstimo não é concluída.
- A extração é uma decisão arquitetural: em um sistema menor ela poderia permanecer no monólito sem prejuízo funcional, mas a separação aumenta independência operacional e escalabilidade.

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
