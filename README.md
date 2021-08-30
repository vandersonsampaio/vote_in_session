# Vote In Session

Vote In Session é um serviço de API REST que disponibiliza alguns recursos para que seja realizado votações em assembleias.

Em seu fluxo principal o serviço cadastra **Pauta**, para em seguida cadastrar uma **Sessão de Votação** associada a essa pauta.
Um tempo de duração é definido a sessão de votação. Ao término da sessão de votação uma mensagem contendo o **resultado do pleito** é publicada em
um serviço de mensageria.

Somente **Associados** podem **votar** nas sessões, para tanto, eles precisam estar registrados no nosso banco de dados (inserção manual).

# Recursos disponibilizados
- Cadastrar nova pauta (`POST /agenda/v1/register`)
  
  Esse recurso permite que o usuário cadastre uma nova pauta. Essa pauta poderá ser associada a uma sessão de votação.

  - **Campos de entrada**:
    1. Gestor (_texto_) - **obrigatório**
    2. Responsável (_texto_) - **obrigatório**
    3. Data da Reunião (_texto em formato 'yyyy-MM-dd'_) - **obrigatório**
    4. Hora de Início (_texto em formato 'HH:mm'_) - **obrigatório**
    5. Hora de Término (_texto em formato 'HH:mm'_) - _opcional_
    6. Tema (_texto_) - **obrigatório**
    7. Observações (_texto_) - _opcional_
    8. Itens da paula (_lista de textos_) - _opcional_

  - **Validações realizadas:** 
    1. Gestor informado;
    2. Coordenador informado;
    3. Tema informado;
    4. Data da reunião informada e não anterior a data atual;
    5. Hora de Início da reunião informada e não anterior a hora atual;
    6. Caso a Hora de Término da reunião seja informada, ela não deverá ser anterior a Hora de Início.
  

- Abrir sessão de votação (`POST /session/v1/register`)

  Esse recurso permite que o usuário cadastre uma nova Sessão de Votação associado a uma pauta existente.
  
  Uma tarefa é agendada para computar o resultado da sessão de votação após seu encerramento.
  
  - **Campos de entrada:**
    1. Código da Pauta (_inteiro_) - **obrigatório**
    2. Minutos de Duração (_inteiro_) - _opcional_
    3. Hora inicial (_texto em formato 'HH:mm') - **obrigatório**
  
  - **Validações realizadas:**
    1. Hora inicial da votação informada no padrão exigido ('HH:mm');
    2. Código da Pauta corresponde a uma pauta existente em nossa base de dados;
    3. Caso os Minutos de Duração não seja informado a Sessão de votação terá duração de **1 minuto**.

- Contabilizar votos (`GET /session/v1/result/{idSession}`)

  Esse recurso obtém o resultado de uma Sessão de Votação finalizada.

  - **Campo de entrada:**
    1. Código da Sessão (_inteiro_) - **obrigatório**
  - **Validações realizadas:**
    1. Código da Sessão corresponde a uma sessão existente em nossa base de dados;
    2. Se a Sessão de Votação já se encerrou, ou seja, se o momento da consulta é posterior ao término da sessão de votação.

- Votar na sessão em pauta (`POST /voting/v1/register`)
  
  Esse recurso permite que o usuário registre seu voto em uma Sessão de Votação aberta.

  - **Campo de entrada:**
    1. Código da Sessão (_inteiro_) - **obrigatório**
    2. Código do Associado (_inteiro_) - **obrigatório**
    3. Voto (_text_) - **obrigatório**
  - **Validações realizadas:**
    1. O Voto deve pertencer ao conjunto {Sim, Yes, Não, No}. A acentuação é removida previamente para realizar a validação do texto;
    2. Código da Sessão de Votação corresponde a uma sessão existente em nossa base de dados;
    3. Código do Associado corresponde a um associado existente em nossa base de dados;
    4. Sessão de Votação encontra-se aberta no momento da chamada do recurso;
    5. Associado só pode votar uma vez por Sessão de Votação;
    6. CPF do Associado é válido e está habilitado a votar.



# Limitações
Não foram implementadas rotinas de segurança de acesso a API (autenticação e autorização), por exemplo, OAuth2 Security (https://oauth.net/2/). Desta forma toda 
requisição recebida é processada, mediante validações descritas no item anterior.

# Versionamento da API

A primeira versão do serviço (v1) foi desenvolvida e descrita nesse arquivo, eventuais erros poderão ser corrigidos sem necessitar a gerão de 
nova versão da API. Porém, para novas versões da API serão criados novos controladores (_controllers_) e implementados novas versões das _Interfaces_ dos serviços.

Essas novas versões não podem alterar o funcionamento das versões anteriores, de forma a não impactar os usuários que já utilizam o serviço.

# Linguagem utilizada, framework e versão

Para realizar o desenvolvimento inicial do serviço, foi utilizado Java 8 com SpringBoot (https://spring.io/projects/spring-boot) em sua versão 2.2.4.

# Recursos utilizados:

A seguir são listados os principais recursos adicionados utilizados no serviço:
  1. Lombok (https://projectlombok.org/) - biblioteca Java que cria automaticamente alguns recursos para manipular objetos;
  2. PostgreSQL (https://www.postgresql.org/) - sistema de gerenciamento de banco de dados relacional que persiste os dados processados pelo serviço;
  3. Hibernate (https://hibernate.org/) - framework para mapeamento objeto-relacional responsável por mapear os modelos de dados do serviço com as tabelas do PostgreSQL;    
  4. RabbitMQ (https://www.rabbitmq.com/) - serviço de mensageria responsável por recepcionar os resultados das Sessões de Votação;
  5. JobRunr (https://www.jobrunr.io/en/) - agendador de tarefas responsável por monitorar o término das Sessões de Votação, computar o resultado e publicar no RabbitMQ;
  6. MapStruct (https://mapstruct.org/) - gerador de código que mapeia os Objetos de Transferência de Dados (Data Transfer Object - DTO) com os modelos de dados do serviço, vice-versa;
  7. OpenFeign (https://spring.io/projects/spring-cloud-openfeign) - criador de clientes HTTP dinâmico responsável por facilitar a comunicação com serviços externos;
  8. Mockito (https://site.mockito.org/) - estrutura de teste que permite a criação de objetos duplos de teste em testes de unidade automatizados;
  9. JoCoCo (https://www.eclemma.org/jacoco/) - biblioteca de cobertura de códigos.


# Consulta terceiros

No momento da Votação, nosso serviço consulta a API de verificação de CPF válido para votação (`GET https://user-info.herokuapp.com/users/{cpf}`). 
A API verifica se o CPF do associado é válido, caso seja, ela informa se o associado está habilitado a votar ou não.
Foi utilizado o OpenFeign para mapear e utilizar essa API.

# Testes

Foram realizados três tipos de testes automatizados: unitário, integração e carga (estresse).

- **Unitário e Integração**

  Foram desenvolvidos 56 testes, sendo eles 54 testes unitários e 2 testes de integração.
  Ao todo, foram cobertos 2.265 linhas de código, o que corresponde a 91% das linhas de código do serviço.
  
- **Carga (Estresse)**

  Para realização desses testes foi utilizado a ferramenta JMeter (https://jmeter.apache.org/) que é uma ferramenta que 
  realiza testes de carga e de estresse em recursos estáticos ou dinâmicos oferecidos por sistemas computacionais.
  
  O cenário criado foi: 
    1. inserção de Agenda, sem concorrência 
    2. inserção de Sessão, sem concorrência
    3. iniciar votação, com concorrência
  
  Todos os testes foram realizados em ambiente de desenvolvimento.
  
  O cenário criado encontra-se na pasta _load_testing_ (`./load_testing/LT_Vote-In-Session.jmx`). 
  Dentro da pasta é possível encontrar os resultados das execuções realizadas (`./load_testing/results/*`), 
  bem como as entradas com os Códigos dos Associados e seus Votos (`./load_testing/input_voting/input.csv`).

# Registro de Log

Todos os erros que acontecem no serviço são registrados (_Logados_). O serviço captura esses erros, trata e retorna ao
usuário uma mensagem amigável, além de registrar em arquivo no servidor essa massagem.

Os principais erros do sistema retornam código de status de resposta HTTP 400 (_Bad Request_), porém pode existir algumas 
situações não mapeadas que o servidor retorne código de status de resposta HTTP 500 (_Internal Server Error_).

# Documentação do código

O serviço utiliza a ferramenta Swagger (https://swagger.io/) para descrever as APIs disponíveis (`/swagger-ui/`).

# Build

O serviço foi desenvolvido utilizando o Gradle 7.0.2 (https://gradle.org/).

Para construir o projeto o usuário deverá possuir o Gradle instalado no seu computador.

No terminal do sistema operacional, vá até à pasta raiz do projeto e execute o comando:

`gradle wrapper`

Após finalizar a execução anterior execute: 

- Windows: `.\gradlew.bat clean build`
- Linux: `.\gradlew clean build`

# Run

Foi disponibilizado um arquivo _Dockerfile_ para criação da imagem do serviço. Além desse
arquivo também foi disponibilizado um arquivo _docker-compose.yml_ com as configurações necessárias
para iniciar o serviço localmente.

O arquivo _docker-compose.yml_ deve ser alterado para informar as credenciais de acessos do banco de dados
(PostgreSQL) e serviço de mensageria (RabbitMQ).

Antes de executar o comando Docker é necessário criar (build) do projeto.
No momento da execução do _docker-compose.yml_ o _Dockerfile_ também será executado, para tanto é necessário
possuir o Docker-compose (https://docs.docker.com/compose/) instalado e configurado em sua máquina.

# Autor e Contato

- Autor: Vanderson Sampaio
- Email: vandersons.sampaio@gmail.com
- Github: https://github.com/vandersonsampaio
- Linkedin: https://www.linkedin.com/in/vanderson-sampaio-399973158/