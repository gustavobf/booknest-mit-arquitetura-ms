# Booknest

## Visão geral

A solução é composta por:
- `config-server`: servidor central de configurações
- `booknest-mit-arquitetura-ms`: aplicação principal
- `booknest-emprestimo-ms`: serviço independente de empréstimo
- `postgres-principal`: banco relacional da aplicação principal
- `postgres-emprestimo`: banco relacional do serviço de empréstimo

A arquitetura utiliza Docker Compose para orquestrar os containers em uma rede local, e o Config Server para centralizar configurações externas por ambiente.

## Respostas da reflexão arquitetural

### 1) Quais configurações da aplicação podem variar entre ambientes?

As configurações que variam entre ambientes são, principalmente:
- porta da aplicação
- URL do banco de dados
- usuário e senha do banco
- URL do serviço de comunicação entre aplicações
- perfil ativo do ambiente (`dev`/`prod`)
- configurações específicas de execução e integração

No projeto, isso aparece em propriedades como `server.port`, `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `servico.emprestimo.url` e no perfil ativo do Spring.

### 2) Quais dessas configurações foram externalizadas?

As configurações foram externalizadas para:
- `application.properties` dos serviços
- arquivos `application-dev.properties` e `application-prod.properties`
- variáveis de ambiente do Docker Compose, como:
  - `DB_URL`
  - `DB_USERNAME`
  - `DB_PASSWORD`
  - `SERVICO_EMPRESTIMO_URL`
  - `SPRING_PROFILES_ACTIVE`
- projeto `config-server` com os arquivos em `config-server/config-repo`

A ideia foi remover valores sensíveis e específicos do ambiente do código Java e deixá-los em fontes externas.

### 3) Quais configurações foram mantidas localmente e quais ficaram centralizadas?

No projeto local, mantivemos apenas o mínimo necessário para inicialização:
- import do Config Server
- perfil ativo
- bootstrap da aplicação

Já a configuração de negócio e ambiente foi centralizada no `config-server`, como:
- URLs e portas por serviço
- banco e contexto de execução
- configurações específicas por profile (`dev` e `prod`)

### 4) Por que um serviço não deve acessar diretamente o banco de outro serviço?

Um serviço não deve acessar o banco do outro porque isso quebra a separação de responsabilidades e aumenta o acoplamento entre módulos. Cada serviço deve ser responsável por seus próprios dados e expor interfaces de integração, como APIs HTTP, para comunicação com outros domínios.

Isso evita:
- dependência de estrutura interna de outro serviço
- problemas de integridade e manutenção
- acoplamento forte entre aplicações
- falhas em cascata quando uma base é alterada sem compatibilidade

### 5) Qual problema o Docker resolve no projeto?

O Docker resolve o problema da inconsistência de ambiente entre desenvolvimento, teste e execução. Com ele, os serviços, bancos e o Config Server rodam em containers padronizados, com dependências isoladas e reproduzíveis.

Em outras palavras, o projeto não depende de uma máquina específica para funcionar corretamente, e a execução fica mais previsível.

### 6) Qual é a função do Docker Compose?

O Docker Compose organiza a execução de múltiplos containers com um único comando. Ele define a rede, os volumes, as dependências e as variáveis de ambiente de todos os serviços do sistema.

No projeto, ele orquestra:
- `config-server`
- `booknest-emprestimo-ms`
- `booknest-mit-arquitetura-ms`
- `postgres-principal`
- `postgres-emprestimo`

### 7) Qual problema uma configuração centralizada procura resolver?

Uma configuração centralizada procura resolver o problema de duplicação e inconsistência de configuração entre vários serviços e ambientes. Quando a mesma informação aparece em vários lugares, fica mais difícil manter, revisar e atualizar em produção.

Com o Config Server, todas as aplicações consultam uma fonte única e padronizada, reduzindo falhas humanas e facilitando a gestão de ambientes como `dev` e `prod`.

## Como executar

Na raiz do projeto:

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

## Observações finais

A solução foi organizada para seguir o modelo de arquitetura Cloud Native com:
- externalização de configuração
- bancos independentes por serviço
- configuração centralizada via Spring Cloud Config
- containerização e orquestração local com Docker Compose

Isso reduz acoplamento, melhora previsibilidade da execução e torna a aplicação mais adequada para ambientes distribuídos e escaláveis.
