package com.tjds.localstackaws.queue;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonSqsProducer {

    @Value("${aws.sqs.endpoint}")
    private String sqsEndpoint;

    private final AmazonSQSAsync amazonSQSAsync;

    public void sendMessageSync(String urlQueue, String message) {
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsEndpoint + "/queue" + urlQueue);
            sendMessageRequest.setMessageBody(message);
            amazonSQSAsync.sendMessage(sendMessageRequest);
            log.debug("[PersonSqsProducer] Message sent={}", message);
        } catch (Exception e) {
            log.error("[PersonSqsProducer] Unexpected error sending message queue={} messageBody={}. Error {}", urlQueue, message, e);
        }
    }

}
