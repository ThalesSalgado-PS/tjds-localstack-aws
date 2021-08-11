#!/usr/bin/env bash
export AWS_DEFAULT_REGION=us-east-1

aws --endpoint-url http://localhost:4576 sqs create-queue --queue-name person-queue --profile localstack
