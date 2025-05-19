# 🛒 Order Parser
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=victorfconti_order-parser&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=victorfconti_order-parser)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=victorfconti_order-parser&metric=coverage)](https://sonarcloud.io/summary/new_code?id=victorfconti_order-parser)
## 🚀 Como Rodar o Projeto
### Pré-requisitos
Certifique-se de que você possui o seguinte instalado na sua máquina:
- **Docker**
  [Instale o Docker aqui](https://docs.docker.com/get-docker/) se ainda não o tiver instalado.

### Passo 1: Clonar o Repositório
Clone o repositório do projeto para a sua máquina local:
``` bash
git clone https://github.com/sua-organizacao/order-parser.git
cd order-parser
```
### Passo 2: Build da Imagem Docker
No diretório do projeto, execute o seguinte comando para construir a imagem Docker:
``` bash
docker build -t order-parser .
```
### Passo 3: Executar o Contêiner
Agora, inicie o contêiner com o seguinte comando:
``` bash
docker run -p 8080:8080 order-parser
```
### Passo 4: Acessar a Aplicação
A aplicação será iniciada e estará acessível em:
- **URL:** [http://localhost:8080](http://localhost:8080)

