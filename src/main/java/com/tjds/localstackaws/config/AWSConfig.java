package com.tjds.localstackaws.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfig {

    @Value("${aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${aws.profile}")
    private String profile;

    @Value("${aws.region.static}")
    private String region;

    @Value("${aws.local.accessKey}")
    private String localstackAccessKey;

    @Value("${aws.local.secretKey}")
    private String localstackSecretKey;

    @Bean
    public AWSCredentialsProvider credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(localstackAccessKey, localstackSecretKey);
        return new AWSStaticCredentialsProvider(credentials);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQS() {
        AmazonSQSAsync sqsAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(credentials())
                .withClientConfiguration(buildSQSClientConfiguration())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, region))
                .build();
        return new AmazonSQSBufferedAsyncClient(sqsAsync);
    }

    private ClientConfiguration buildSQSClientConfiguration() {
        return new ClientConfiguration()
                .withConnectionTTL(60000)
                .withMaxErrorRetry(5)
                .withMaxConnections(200);
    }

}
