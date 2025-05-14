# Projeto: Gerenciamento de Usuários
Este projeto é responsável por gerenciar usuários, incluindo a criação, edição, exclusão e validação de dados, como a restrição de unicidade no campo de email.

Funcionalidades
* Adicionar novos usuários e endereços.
* Editar informações de usuários e endereços existentes.
* Excluir usuários e endereços.
* Garantir que o campo de email seja único no banco de dados.

### Tecnologias Utilizadas
* Frameworks: [Spring Boot 3.4.5](https://start.spring.io/)
* Dependencias: thymeleaf, openfeign
* JDK: 17
* IDE: [Intellij](https://www.jetbrains.com/idea/)
* Gerenciado de dependencias: [Apache Maven 3.9.9](https://maven.apache.org/)
* Container: [Docker](https://www.docker.com/) e [Docker Hub](https://hub.docker.com/)
* Ferramentas: [Google Chrome
   Versão 136.0.7103.93 (Versão oficial) 64 bits](https://www.google.com/intl/pt-BR/chrome/)

### Como Executar
1. Clone o repositório: git clone https://github.com/alberes/register-manager-frontend
2. Acesso ao backend:
Certifique-se de que o backend register-manager está configurado corretamente.
3. Usando uma imagem Docker (Opcional)

4. Executar o projeto
- Abrir o terminal na raiz do projeto [SUB_DIRETORIOS]/register-manager-frontend e exeuctar o comando abaixo para gerar o pacote.
```
mvn -DskipTests=true clean package
```
- No termial entrar no diretório [SUB_DIRETORIOS]/register-manager-frontend/target
```
java -jar register-manager-frontend-0.0.1-SNAPSHOT.jar
```
A aplicação subirá na porta 8080

5. Exemplos:
   - Acessar a URL http://localhost:8080/ com usuário e senha
     - Perfil:
         - ADMIN tem acesso a todos os recursos
         - USER apenas ao próprio recurso e não tem tem permissão para criar usuário :-D
         - A aplicação criou alguns usuários:
           - admin@admin.com com
           - manager@manager.com
           - user@user.com

7. Página principal
  -  Lista usuários caso tenha o perfil ADMIN ou apenas o usuário caso tenha o perfil USER
  - Perfil Admin tem acesso total e poderá cadastrar, visualizar, editar, excluir
  - Perfil USER poderá apenas visualizar, editar e exluir seus próprios dados.

## Docker
No projeto já existe uma imagem versionada no Docker Hub e precisa apenas ter o ambiente Docker.
Abrir um terminal no mesmo diretório do arquivo docker-compose.yaml e execute o comando abaixo.
```
docker-compose up -d
```
Baixar o ambiente
```
docker-compose down
```