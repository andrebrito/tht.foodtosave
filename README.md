![alt](https://media.licdn.com/dms/image/D4D0BAQE-20xYpkezdQ/company-logo_200_200/0/1680814196185?e=1701302400&v=beta&t=JnMSXPlmCZK8Gp1llg9iG5Fk_fOp5DYNKnTSWTItLjI "")

## Take Home Test

### Requisitos
1. Construir um serviço usando Spring Boot e Docker. :white_check_mark:
2. Conectar-se a um banco de dados relacional usando o JdbcTemplate. :white_check_mark:
3. Conectar-se a um servidor Redis. :white_check_mark:
4. Implementar uma API que retorne um objeto simples e utilize o Redis como cache. :white_check_mark:
5. O cache deve ter um TTL de 10 segundos. :white_check_mark:
6. O serviço deve ir ao banco de dados apenas na primeira requisição dentro desse intervalo de tempo, caso a resposta não seja nula. :white_check_mark:
7. Colocar ao menos um teste de integração que valide a API. :white_check_mark:
8. É importante fornecer uma visibilidade do tempo gasto na construção. :white_check_mark:
9. O serviço deverá ser executado usando docker e subindo todas as suas sub-dependências. :no_entry_sign:
	- Foi adicionado uma sinalização de erro ao lado desse item porque, infelizmente, não consegui fazer a API se conectar ao Redis quando ela é inicializada no mesmo `docker-compose`.
 	- Mesmo alterando as portas, nome do container, versão do Redis, nada teve um efeito considerável.
  	- Após a entrega na data (28/08/2023) vou seguir investigando o motivo de não ter dado certo esse ponto.

### Opcional
- Colocar um readme para explicar como subir o serviço. :white_check_mark:
- Repetir a montagem do serviço, seguindo os mesmos critérios, substituindo a linguagem Java por outra de sua escolha.
	- [Link para o projeto em NestJS](https://github.com/andrebrito/tht.foodtosave.nestjs).

### Detalhes para execução
- Há 2 arquivos `docker-compose`:
    - O arquivo com sufixo `-dev` é usado para desenvolvimento, ou seja, executa-se ele e, posteriormente, o `FoodToSaveApplication` via IDE ou `mvnw`.
    - O arquivo sem sufixo será usado após o build da imagem da aplicação via Dockerfile.
    - Ambos os arquivos contém a inicialização do Postgres e do Redis.
- `Dockerfile` será usado para o build da imagem via linha de comando:
    - Na raiz do projeto, executar `docker build -t foodtosave .`.
    - Executar o `docker-compose.yml` (pela IDE ou pela linha de comando):
      - `docker-compose up`

### Aplicação
- A aplicação é bastante simples:
  - Há um modelo, `person`, com 3 colunas: `id`, `name` e `created_at`.
  - Há um endpoint implementado, com os seguintes métodos HTTP:
    - `POST /person`, onde um JSON com o atributo `name` deve ser enviado com algum conteúdo texto.
    - `GET /person`, pelo qual será retornada lista com todos os registros persistidos (ou uma lista vazia).
    - `GET /person/:id`, endpoint para consultar um registro pelo id.
    - `DELETE /person`, para remover todas os regitros.
    - `DELETE /person/:id`, para remover registro específico.
  - Os endpoints de acesso (`GET`) fazem a leitura do(s) registro(s) do banco de dados.
  - Caso seja feita a mesma requisição antes dos 10 segundos seguintes, a consulta acontece no Redis.
  - Caso sejam realizadas requisições que alteram o modelo (`POST` ou `DELETE`), há o cache evict no Redis e as próximas consultas serão realizadas no Postgres.

### Testes Automatizados
- Para execução dos testes, foram utilizados:
    - JUnit + Mockito.
    - H2.
    - embedded-redis.
- Foram implementados diferentes tipos de testes:
    - Para o Service, o teste é estritamente unitário.
    - Para o Repository, usamos o H2 como banco de dados em memória.
    - Para o Controller e e2e usamos todo o conjunto.
- O teste e2e carece de algumas melhorias:
    - O interessante seria termos um arquivo seed, com a persistência de alguns registros para usarmos nos testes.
    - Optou-se por não seguir o caminho acima pelo tempo proposto por mim.

### Postgres & Flyway
- Inicialmente, havia pensado em implementar a solução usando algum banco de dados mais prático e leve, como  SQLite. No entanto, notaram-se algumas incompatibilidades na execução das migrations com o H2. Dessa forma, foi usado Postgres pois não há incompabilidades significativas entre ele e o H2.
- Configurações via env:
  - `DATABASE_HOST` (com a porta).

### Redis Cache
- Foi utilizado Redis como estratégia de cache, usufruindo algumas estratégias já implementadas no Spring (`@EnableCaching`).
- Caso seja necessária implementação mais controlada, posso alterar a implementação.
- Configurações via env:
  - `REDIS_HOST`
  - `REDIS_PORT`
  - `REDIS_PASSWORD`
  - `REDIS_TTL`.

### Conclusão
- Não há como precisar o tempo que foi levado no desafio, mas acredita-se que 20 horas é um valor bastante próximo do real.
- Entendo que é uma quantidade significativa de horas para uma funcionalidade simples. Justifico essa quantidade de horas pela falta de ambientação na linguagem e nos frameworks - conforme conversamos, nos últimos 3 anos, Java não tem sido minha ferramenta principal de trabalho e acabei deixando de estudá-la no tempo que não a usei. Notei, no decorrer do desenvolvimento, que muitas coisas relacionadas ao Spring não eram do meu conhecimento, então acabei estudando de forma um pouco mais detalhada.
- Mesmo não conseguindo entregar o desafio por completo (a questão do docker-compose, como mencionei acima, ficou faltando), me diverti bastante!
- Entendo perfeitamente se minha candidatura for declinada, dado que não conclui o desafio.
