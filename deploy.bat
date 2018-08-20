CALL mvn package -Dmaven.test.skip=true

CALL aws cloudformation package --template-file ./sam.yaml --output-template-file ./target/output-sam.yaml --s3-bucket mjr.lambda.test

CALL aws cloudformation deploy --template-file ./target/output-sam.yaml --stack-name lambdatest3 --capabilities CAPABILITY_IAM

CALL aws cloudformation describe-stacks --stack-name lambdatest3
