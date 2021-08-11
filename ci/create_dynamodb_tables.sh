#!/usr/bin/env bash
#export AWS_DEFAULT_REGION=us-east-1

aws --endpoint-url http://localhost:4569 dynamodb create-table --table-name tb_person \
    --attribute-definitions AttributeName=document,AttributeType=S \
    --key-schema AttributeName=document,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --profile localstack
