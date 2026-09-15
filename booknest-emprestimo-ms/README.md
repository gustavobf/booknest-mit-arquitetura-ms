# Booknest Empréstimo Microservice

## Nome do serviço
Booknest Empréstimo MS

## Responsabilidade principal
Gerenciar o ciclo de vida dos empréstimos da biblioteca: criação, consulta, atualização, devolução e controle de atraso.

## Funcionalidade separada
A funcionalidade de empréstimo foi extraída da aplicação principal, deixando a aplicação proprietária focada em catálogo, usuários e regras de consulta.

## Motivo da separação
A lógica de empréstimo possui autonomia de negócio, ciclo de vida próprio e dependências específicas, além de crescer com regras como multas, atrasos e devoluções sem acoplar a aplicação principal a mais regras de negócio.

## Reflexão arquitetural
A funcionalidade foi separada porque representa uma responsabilidade clara e relativamente independente dentro do domínio da biblioteca. Depois da separação, a comunicação entre aplicações passou a depender de rede, o que aumenta latência e exige tratamento de indisponibilidade. Se o serviço de empréstimos ficar indisponível, a aplicação principal não consegue registrar ou consultar empréstimos de forma consistente, mesmo que o restante do sistema continue funcionando. Ainda assim, essa separação é uma decisão arquitetural e não uma obrigação tecnológica: em um domínio menor, a funcionalidade poderia continuar dentro da aplicação monolítica sem prejuízo funcional imediato.

## Configurações externalizadas:
- `application-dev.properties`
- `application-prod.properties`

Variáveis de ambiente:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Arquivos de execução:
- `Dockerfile`
- `compose.yml`
- `config-server` (centraliza as propriedades em `config-server/config-repo`)

### Reflexão arquitetural
- Configurações variáveis: porta, URL do banco, usuário e senha.
- Externalizadas: banco por propriedades e variáveis de ambiente.
- Um serviço não deve acessar o banco do outro porque cada um deve ser dono dos próprios dados.
- Docker resolve a execução previsível da aplicação.
- Docker Compose coordena o serviço e seu banco.
- Configuração centralizada resolveria a necessidade de repetir parâmetros por ambiente.
