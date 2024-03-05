# Teste Técnico ERPX

## Descrição do Projeto

### Tecnologias mínimas que devem ser utilizadas:
- [x] Banco de dados PostgreSQL
- [x] Java 8+
- [x] Maven
- [x] Spring
- [x] JPA
- [x] Bean Validation
- [ ] QueryDSL
- [x] REST com JSON

### Requisitos da prova:
- [x] Deverá ser desenvolvido um cadastro (Create/Read/Update/Delete/List com paginação)
para as seguintes entidades: produto/serviço, pedido e itens de pedido.
- [ ] Deverá ser possível aplicar filtros na listagem
- [x] As entidades deverão utilizar Bean Validation
- [x] Deverá ser implementado um ControllerAdvice para customizar os HTTP Response das
requisições (mínimo BAD REQUEST)
- [x] Todos as entidades deverão ter um ID único do tipo UUID gerado automaticamente
- [x] No cadastro de produto/serviço deverá ter uma indicação para diferenciar um produto de
um serviço
- [x] Deverá ser possível aplicar um percentual de desconto no pedido, porém apenas para os
itens que sejam produto (não serviço); o desconto será sobre o valor total dos produtos
- [x] Somente será possível aplicar desconto no pedido se ele estiver na situação Aberto
(Fechado bloqueia)
- [x] Não deve ser possível excluir um produto/serviço se ele estiver associado a algum pedido
- [x] Não deve ser possível adicionar um produto desativado em um pedido

### Para rodar o projeto:

1. Clone o repositório
2. Abra o projeto em sua IDE de preferência
3. Execute o projeto
4. Acesse o endereço `http://localhost:8080/swagger-ui.html` para acessar a documentação da API
5. Importe o arquivo `erpx.postman_collection.json` no Postman para testar a API

