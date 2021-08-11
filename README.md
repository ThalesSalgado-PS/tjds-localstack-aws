# Java Spring localstack aws services

# Localstack Aws
Projeto java simples que interage com alguns recursos da AWS provisionados localmente por meio de um container da imagem localstack

# SetUp
Para iniciar, deve-se ter instalado o AWS Cli, o Docker e o Docker-Compose para executar alguns comandos.
Em um terminal execute o comando 'aws configure --profile localstack' e em seguida entre com os valores a seguir:
 - AWS Access Key ID: 1234
 - AWS Secret Access Key: 1234
 - Default region name [us-east-1]: us-east-1
 - Default output format [json]: json
 ```
 aws configure --profile localstack
 ```
Depois de configurar o perfil aws, rode o docker-compose presente na pasta '/ci' na raiz do projeto
```
docker-compose up -d
```

# SQS
Com o docker-compose iniciado execute o script 'create_sqs_queues.sh' dentro da pasta '/ci', com isso será criada a fila sqs 'person-queue'.
A seguir tem uma breve explicação das configurações feitas no back-end e alguns comandos que podem ser úteis.

## Configs no back-end do SQS
No pacote 'config', tem a classe 'AWSConfig' na qual tem um bean de nome 'amazonSQS' que é o bean configurado para o client da AWS SQS.
No msm pacote 'config', a classe 'JMSConfig' tem a injeção do bean do SQS criado na classe 'AWSConfig' e tem a criação do bean de nome 'defaultJmsTemplate'.
Ao subir o projeto tem: 
 - Listener/consumer da fila 'person-queue' na classe 'PersonSqsConsumer' usando o @JmsListener.
 - Rota post '/sqs/person-queue' na 'SqsController' que recebe um json no corpo da requisição e envia msg pra fila sqs 'person-queue'.

## Comandos no terminal
Conferir as filas existentes:
```
aws --endpoint-url http://localhost:4576 sqs list-queues
```

Criar fila:
```
aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name {nomeDaFila}
aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name person-queue
```

Conferir se há msgs na fila: (checar os campos 'ApproximateNumberOfMessages', 'ApproximateNumberOfMessagesNotVisible' e 'ApproximateNumberOfMessagesDelayed')
```
aws --endpoint-url http://localhost:4576 sqs get-queue-attributes --queue-url http://localstack:4576/queue/{nomeDaFila} --attribute-names All
aws --endpoint-url http://localhost:4576 sqs get-queue-attributes --queue-url http://localstack:4576/queue/person-queue --attribute-names All
```

Produzir msgs na fila:
```
aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url http://localhost:4576/queue/{nomeDaFila} --message-body "Mensagem teste"
aws --endpoint-url=http://localhost:4576 sqs send-message --queue-url http://localhost:4576/queue/person-queue --message-body "{\"document\":\"11122233344\",\"name\":\"Aaa Bbb\"}"
```

Consumir msgs na fila:
```
aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/queue/{nomdeDaFila} --max-number-of-messages 10
aws --endpoint-url=http://localhost:4576 sqs receive-message --queue-url http://localhost:4576/queue/person-queue --max-number-of-messages 10
```

Limpa fila:
```
aws --endpoint-url http://localhost:4576 sqs purge-queue --queue-url http://localstack:4576/queue/{nomeDaFila}
aws --endpoint-url http://localhost:4576 sqs purge-queue --queue-url http://localstack:4576/queue/person-queue
```

Para mais comandos do recurso SQS: https://docs.aws.amazon.com/cli/latest/reference/sqs/


# DynamoDb

## Comandos no terminal
Criar tabela:
``` 
Tabela chave primaria simples (executar esse):
aws --endpoint-url http://localhost:4569 dynamodb create-table --table-name tb_person \
    --attribute-definitions AttributeName=document,AttributeType=S \
    --key-schema AttributeName=document,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --profile localstack

Tabela chave primaria composta e com tags (apenas como exemplo):
aws dynamodb create-table \
    --table-name MusicCollection \
    --attribute-definitions AttributeName=Artist,AttributeType=S AttributeName=SongTitle,AttributeType=S \
    --key-schema AttributeName=Artist,KeyType=HASH AttributeName=SongTitle,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --tags Key=Owner,Value=blueTeam
```

Verificar se há registros na tabela:
```
aws --endpoint-url http://localhost:4569 dynamodb scan --table-name {nomeDaTabela}
aws --endpoint-url http://localhost:4569 dynamodb scan --table-name tb_person
```

Inserir registro na tabela por lote de um arquivo .json:
```
aws --endpoint-url http://localhost:4569 dynamodb batch-write-item --request-items file://{nomeDoArquivoJsonDeEntrada} --return-consumed-capacity TOTAL
aws --endpoint-url http://localhost:4569 dynamodb batch-write-item --request-items file://personDynamoDb.json --return-consumed-capacity TOTAL
```

Listar tabelas existentes:
```
aws --endpoint-url http://localhost:4569 dynamodb list-tables
```

Deletar tabela:
```
aws --endpoint-url http://localhost:4569 dynamodb delete-table --table-name {nomeDaTabela}
aws --endpoint-url http://localhost:4569 dynamodb delete-table --table-name tb_person
```

Para mais comandos do recurso DynamoDB: https://docs.aws.amazon.com/cli/latest/reference/dynamodb/

