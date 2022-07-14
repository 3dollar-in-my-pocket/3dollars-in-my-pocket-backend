HOST=localhost
PORT=4566

echo "Configure aws credentials"
aws configure set aws_access_key_id threedollars
aws configure set aws_secret_access_key threedollars
aws configure set region ap-northeast-2

echo "Creating SQS"
aws sqs create-queue --endpoint-url=http://${HOST}:${PORT} --queue-name=threedollars-boss-single-push-sqs
aws sqs create-queue --endpoint-url=http://${HOST}:${PORT} --queue-name=threedollars-boss-bulk-push-sqs

echo "Listing SQS"
aws sqs list-queues --endpoint-url=http://${HOST}:${PORT}
