# 📚 Booknest - Sistema de Gestão de Biblioteca

Solução distribuída para gestão de acervo, usuários e empréstimos de uma biblioteca, composta por múltiplos serviços orquestrados via Docker Compose.

## Visão geral da arquitetura

A solução é composta por:
- `config-server`: servidor central de configurações (Spring Cloud Config)
- `booknest-mit-arquitetura-ms`: aplicação principal (catálogo, usuários)
- `booknest-emprestimo-ms`: serviço independente de empréstimo
- `rabbitmq`: broker de mensageria para comunicação assíncrona
- `postgres-principal`: banco relacional da aplicação principal
- `postgres-emprestimo`: banco relacional do serviço de empréstimo

Arquitetura resultante:

```
Aplicação Principal (booknest-mit-arquitetura-ms)
├── API REST (catálogo, usuários)
├── Banco de dados (postgres-principal)
├── Spring Batch (importação de exemplares via CSV)
│
├── HTTP → booknest-emprestimo-ms (serviço independente)
│
booknest-emprestimo-ms
├── API REST (empréstimos, devoluções)
├── Banco de dados (postgres-emprestimo)
└── Mensagem → RabbitMQ → Consumidor de notificações
```

---

## 🧱 Módulo: booknest-mit-arquitetura-ms (aplicação principal)

### Responsabilidade
Gerencia o acervo (livros, autores, categorias, editoras, exemplares) e os usuários da biblioteca, além de se comunicar com o serviço de empréstimo via HTTP (OpenFeign).

### Pacotes principais
- `catalogo.controller` / `catalogo.service` / `catalogo.repository` / `catalogo.model`
- `usuario.controller` / `usuario.service` / `usuario.repository` / `usuario.model`
- `catalogo.batch` (processamento em lote — ver seção Spring Batch)

### Módulos da aplicação

A aplicação (considerando aplicação principal + serviço de empréstimo) é organizada em três módulos de domínio:

- **Catálogo** — responsável por manter o acervo da biblioteca: livros, autores, categorias, editoras e exemplares. Controla também a disponibilidade dos exemplares.
- **Usuário** — responsável pelo cadastro e manutenção das informações dos usuários da biblioteca.
- **Empréstimo** — responsável pelas regras de empréstimo, devolução, controle de atraso/multa e pela notificação assíncrona associada a essas operações. Hoje executado como serviço independente (`booknest-emprestimo-ms`).

### Análise de dependências

Exemplo de dependência entre módulos: **Empréstimo → Catálogo**. Para registrar um empréstimo, o módulo de Empréstimo precisa consultar o exemplar informado e atualizar sua disponibilidade; sem essa informação do Catálogo, não é possível validar se o exemplar existe ou se pode ser emprestado.

Outro exemplo: **Empréstimo → Usuário**. Ao criar um empréstimo, é necessário validar o usuário informado (existência e situação de atividade) e, futuramente, consultar seu histórico de empréstimos.

### Empréstimo como candidato a serviço independente
O módulo de empréstimo foi identificado como candidato a extração por possuir regras próprias e ciclo de vida independente (registrar empréstimos, controlar devoluções, verificar atrasos, atualizar disponibilidade de exemplares), dependendo do módulo de usuário (validação/histórico) e do módulo de catálogo (exemplares/disponibilidade). Essa extração foi realizada e hoje vive em `booknest-emprestimo-ms`.

A aplicação principal chama esse serviço por HTTP usando OpenFeign, com a URL externa configurada por `SERVICO_EMPRESTIMO_URL`.

### Reflexão arquitetural (extração do serviço de empréstimo)
- Motivo da escolha: responsabilidade clara e limites bem definidos.
- O que ficou mais complexo: a comunicação entre aplicações passou a depender de rede, latência e disponibilidade.
- Se o serviço ficar indisponível: a aplicação principal responde com erro amigável de indisponibilidade, sem expor detalhes internos da falha, e a operação de empréstimo não é concluída.
- A extração é uma decisão arquitetural: em um sistema menor ela poderia permanecer no monólito sem prejuízo funcional, mas a separação aumenta independência operacional e escalabilidade.

### Documentação da API
Springdoc OpenAPI / Swagger, disponível em `http://localhost:8080/swagger-ui/index.html`.

Principais recursos:
- `GET/POST /autores`, `GET /autores/{id}`
- `GET/POST /livros`, `GET /livros/{id}`
- `GET/POST /usuarios`, `GET /usuarios/{id}`, `GET /usuarios/{id}/emprestimos`
- `GET/POST /emprestimos`, `GET /emprestimos/{id}`, `PATCH /emprestimos/{id}/devolucao`
- `GET /ceps/{cep}`
- `GET /categorias`, `GET /editoras`, `GET /exemplares`
- `POST /catalogo/exemplares/importacao` (Spring Batch — ver abaixo)

### Configurações externalizadas
- `application-dev.properties` / `application-prod.properties`
- Variáveis de ambiente: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVICO_EMPRESTIMO_URL`, `CONFIG_SERVER_URL`

### Reflexão arquitetural (configuração e infraestrutura)
- Configurações que variam entre ambientes: porta, URL do Config Server, URL do banco, usuário, senha e URL do serviço remoto.
- Externalizadas: configurações de banco, URL do serviço de empréstimo e URL do Config Server.
- Um serviço não deve acessar diretamente o banco de outro porque cada responsabilidade deve manter seus próprios dados.
- Docker empacota a aplicação para execução previsível.
- Docker Compose sobe a solução completa com rede e dependências.
- Configuração centralizada resolve a dispersão de parâmetros entre serviços e ambientes.

### Processamento em Lote (Spring Batch)

**Funcionalidade escolhida:** importação de exemplares do catálogo a partir de um arquivo CSV (`codigo,isbnLivro,estadoConservacao`), disponível via `POST /catalogo/exemplares/importacao` (multipart, campo `arquivo`).

**Por que é adequada para Batch:** cadastrar exemplares em massa é uma operação orientada a conjunto de dados, não a uma única requisição transacional: o volume de registros pode variar, alguns podem ser inválidos (ISBN inexistente, estado de conservação incorreto) e o processamento pode ser interrompido e reiniciado. Isso se encaixa no modelo leitura → processamento → escrita em chunks do Spring Batch, em vez de uma API REST que trataria tudo em uma única transação síncrona.

**Estrutura do Job:**
- Job: `importacaoExemplaresJob`
- Step: `importacaoExemplaresStep` (chunk de 5 registros)
- ItemReader: `FlatFileItemReader` lendo o CSV informado (parâmetro de job `arquivo`)
- ItemProcessor: `ExemplarItemProcessor` — normaliza o código, resolve o `Livro` pelo ISBN e valida o estado de conservação; registros inválidos são ignorados (retornam `null`) sem interromper o restante do arquivo
- ItemWriter: `RepositoryItemWriter` persistindo os exemplares via `ExemplarRepository`

Um CSV de exemplo está em `booknest-mit-arquitetura-ms/src/main/resources/batch/exemplares-exemplo.csv`.

---

## 🧱 Módulo: booknest-emprestimo-ms (serviço de empréstimo)

### Responsabilidade
Gerenciar o ciclo de vida dos empréstimos da biblioteca: criação, consulta, atualização, devolução e controle de atraso.

### Motivo da separação
A lógica de empréstimo possui autonomia de negócio, ciclo de vida próprio e dependências específicas, além de crescer com regras como multas, atrasos e devoluções sem acoplar a aplicação principal a mais regras de negócio.

### Reflexão arquitetural
A funcionalidade foi separada porque representa uma responsabilidade clara e relativamente independente dentro do domínio da biblioteca. Depois da separação, a comunicação entre aplicações passou a depender de rede, o que aumenta latência e exige tratamento de indisponibilidade. Se o serviço de empréstimos ficar indisponível, a aplicação principal não consegue registrar ou consultar empréstimos de forma consistente, mesmo que o restante do sistema continue funcionando. Ainda assim, essa separação é uma decisão arquitetural e não uma obrigação tecnológica: em um domínio menor, a funcionalidade poderia continuar dentro da aplicação monolítica sem prejuízo funcional imediato.

### Configurações externalizadas
- `application-dev.properties` / `application-prod.properties`
- Variáveis de ambiente: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

### Comunicação Assíncrona (Mensageria)

**Operação escolhida:** notificação de empréstimo/devolução. Sempre que um empréstimo é criado (`POST /emprestimos`) ou uma devolução é registrada (`PATCH /emprestimos/{id}/devolucao`), o serviço publica uma mensagem no RabbitMQ em vez de enviar a notificação de forma síncrona.

**Por que pode ser assíncrona:** avisar o usuário sobre o empréstimo/devolução não faz parte da regra que precisa ser garantida na mesma transação HTTP — o empréstimo já está salvo no banco antes da notificação ser publicada. Se o envio da notificação demorasse, falhasse ou dependesse de um serviço externo (e-mail, push), isso não deveria impedir nem atrasar a resposta ao cliente que criou o empréstimo.

**Componentes:**
- Produtor: `NotificacaoEmprestimoProducer`, chamado por `EmprestimoService` após salvar o empréstimo/devolução.
- Fila: `booknest.emprestimo.notificacao.queue`, ligada à exchange `booknest.emprestimo.exchange` pela routing key `emprestimo.notificacao` (declaradas em `RabbitMQConfig`).
- Consumidor: `NotificacaoEmprestimoConsumer`, que simula o envio da notificação via log.

**Comportamento com o consumidor indisponível:** o consumidor pode ser desligado com a variável de ambiente `RABBITMQ_CONSUMIDOR_HABILITADO=false` (ou a propriedade `app.rabbitmq.consumidor.habilitado=false`). Com o consumidor desligado, as mensagens publicadas permanecem retidas na fila (visíveis no painel do RabbitMQ, `http://localhost:15672`) até que o consumidor seja religado — diferente de uma chamada REST, que falharia imediatamente se o destino estivesse indisponível.

Variáveis de ambiente adicionais: `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD`, `RABBITMQ_CONSUMIDOR_HABILITADO`.

---

## 🧱 Módulo: config-server

### Responsabilidade
Servidor central de configurações (Spring Cloud Config Server), expondo as propriedades de cada serviço/ambiente a partir de `config-server/config-repo`.

### Reflexão arquitetural

**Quais configurações da aplicação podem variar entre ambientes?**
Porta da aplicação, URL do banco de dados, usuário e senha do banco, URL do serviço de comunicação entre aplicações, perfil ativo do ambiente (`dev`/`prod`) e configurações específicas de execução e integração. No projeto, isso aparece em propriedades como `server.port`, `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `servico.emprestimo.url` e no perfil ativo do Spring.

**Quais dessas configurações foram externalizadas?**
Foram externalizadas para `application.properties` dos serviços, arquivos `application-dev.properties`/`application-prod.properties`, variáveis de ambiente do Docker Compose (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVICO_EMPRESTIMO_URL`, `SPRING_PROFILES_ACTIVE`) e o projeto `config-server` (arquivos em `config-server/config-repo`). A ideia foi remover valores sensíveis e específicos do ambiente do código Java e deixá-los em fontes externas.

**Quais configurações foram mantidas localmente e quais ficaram centralizadas?**
Localmente, mantivemos apenas o mínimo necessário para inicialização: import do Config Server, perfil ativo e bootstrap da aplicação. A configuração de negócio e ambiente (URLs e portas por serviço, banco e contexto de execução, configurações específicas por profile) foi centralizada no `config-server`.

**Por que um serviço não deve acessar diretamente o banco de outro serviço?**
Porque isso quebra a separação de responsabilidades e aumenta o acoplamento entre módulos. Cada serviço deve ser responsável por seus próprios dados e expor interfaces de integração (APIs HTTP) para comunicação com outros domínios, evitando dependência de estrutura interna de outro serviço, problemas de integridade/manutenção, acoplamento forte e falhas em cascata.

**Qual problema o Docker resolve no projeto?**
Resolve a inconsistência de ambiente entre desenvolvimento, teste e execução: os serviços, bancos e o Config Server rodam em containers padronizados, com dependências isoladas e reproduzíveis, sem depender de uma máquina específica.

**Qual é a função do Docker Compose?**
Organiza a execução de múltiplos containers com um único comando, definindo rede, volumes, dependências e variáveis de ambiente de todos os serviços (`config-server`, `booknest-emprestimo-ms`, `booknest-mit-arquitetura-ms`, `rabbitmq`, `postgres-principal`, `postgres-emprestimo`).

**Qual problema uma configuração centralizada procura resolver?**
Duplicação e inconsistência de configuração entre vários serviços e ambientes. Com o Config Server, todas as aplicações consultam uma fonte única e padronizada, reduzindo falhas humanas e facilitando a gestão de ambientes como `dev` e `prod`.

---

## Relação entre mensageria e Batch

- **Mensageria (RabbitMQ)** permite comunicação assíncrona entre componentes: uma aplicação publica um evento e segue seu fluxo sem esperar o processamento do consumidor, que pode ocorrer minutos depois.
- **Spring Batch** permite processar um conjunto estruturado de dados (um arquivo, uma tabela) em etapas controladas de leitura, transformação e gravação, normalmente disparado por uma ação pontual (upload, agendamento), não por um evento de domínio isolado.
- No projeto: **REST** é usado para operações que exigem resposta imediata (criar/consultar empréstimo, livro, usuário); **mensageria** é usada para notificar o usuário sobre empréstimo/devolução, que pode ocorrer depois; **Batch** é usado para importar exemplares em massa, quando o volume de dados e a possibilidade de registros inválidos tornam inadequado tratar tudo em uma única requisição HTTP.

---

## 🚀 Como executar

### Pré-requisitos
- Docker e Docker Compose
- (Opcional, para rodar fora do Compose) Java 21+ e Maven 3.6+

### Execução integrada com Docker Compose

Na raiz do projeto (`booknest-mit-arquitetura-ms`, onde está o `compose.yml`):

```bash
docker compose up -d --build
```

Para acompanhar logs:

```bash
docker compose logs -f
```

Para verificar os serviços:

```bash
docker compose ps
```

### Serviços e portas

| Serviço | Porta local | Descrição |
|---|---|---|
| `booknest-mit-arquitetura-ms-gustavo-figueiredo-api` | `8080` | Aplicação principal (catálogo, usuários) — Swagger em `/swagger-ui/index.html` |
| `booknest-emprestimo-ms` | `18081` (container `8081`) | Serviço de empréstimos |
| `config-server` | `8888` | Servidor de configuração |
| `rabbitmq` | `5672` (AMQP) / `15672` (painel de gestão, usuário/senha `guest`/`guest`) | Broker de mensageria |
| `postgres-principal` | `5432` | Banco da aplicação principal |
| `postgres-emprestimo` | `5433` (container `5432`) | Banco do serviço de empréstimo |

No Compose, a comunicação entre containers usa nomes de serviço na rede Docker (ex.: `http://booknest-emprestimo-ms:8081`, `rabbitmq:5672`), sem `localhost` entre aplicações.

### Execução local (sem Docker) da aplicação principal

```bash
cd booknest-mit-arquitetura-ms
./mvnw clean compile
./mvnw spring-boot:run
```

> Requer Config Server, banco e (se for testar mensageria) RabbitMQ disponíveis conforme configurado no profile `dev`.

---

## Observações finais

A solução foi organizada para seguir o modelo de arquitetura Cloud Native com:
- externalização de configuração
- bancos independentes por serviço
- configuração centralizada via Spring Cloud Config
- comunicação síncrona (REST) e assíncrona (RabbitMQ) entre serviços
- processamento em lote (Spring Batch) para operações orientadas a conjunto de dados
- containerização e orquestração local com Docker Compose

Isso reduz acoplamento, melhora previsibilidade da execução e torna a aplicação mais adequada para ambientes distribuídos e escaláveis.
