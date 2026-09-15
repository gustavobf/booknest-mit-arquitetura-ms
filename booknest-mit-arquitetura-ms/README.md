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

## 🧱 Microsserviço de empréstimos

A funcionalidade de empréstimo foi extraída para um serviço independente em `booknest-emprestimo-ms`.

- Nome do serviço: Booknest Empréstimo MS
- Responsabilidade: criar, consultar, atualizar e registrar devolução de empréstimos
- Funcionalidade separada: módulo de empréstimo da API principal
- Motivo: regras próprias de negócio, ciclo de vida independente e comunicação via rede

A aplicação principal chama esse serviço por HTTP usando OpenFeign, com a URL externa configurada por `SERVICO_EMPRESTIMO_URL` nos profiles.

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

## Configurações externalizadas:
- `application-dev.properties`
- `application-prod.properties`

Variáveis de ambiente:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `SERVICO_EMPRESTIMO_URL`
- `CONFIG_SERVER_URL`

Infraestrutura adicionada:
- `Dockerfile`
- `compose.yml`
- `config-server` (centraliza as propriedades em `config-server/config-repo`)

### Reflexão arquitetural
- Configurações que variam entre ambientes: porta, URL do Config Server, URL do banco, usuário, senha e URL do serviço remoto.
- Externalizadas: configurações de banco, URL do serviço de empréstimo e URL do Config Server.
- Um serviço não deve acessar diretamente o banco de outro porque cada responsabilidade deve manter seus próprios dados.
- Docker empacota a aplicação para execução previsível.
- Docker Compose sobe a solução completa com rede e dependências.
- Configuração centralizada resolve a dispersão de parâmetros entre serviços e ambientes.

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven 3.6+

### Comandos

```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### Execução integrada com Docker Compose

No diretório raiz `booknest-mit-arquitetura-ms`, execute:

```bash
docker compose up --build
```

Serviços iniciados:
- `config-server` (porta `8888`)
- `booknest-mit-arquitetura-ms-gustavo-figueiredo-api` (porta `8080`)
- `booknest-emprestimo-ms` (porta `8081`)
- `postgres-principal` (banco da aplicação principal)
- `postgres-emprestimo` (banco do serviço de empréstimo)

No Compose, a comunicação entre containers usa nomes de serviço na rede Docker, sem `localhost` entre aplicações.
---
