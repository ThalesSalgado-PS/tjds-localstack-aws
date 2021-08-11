package com.tjds.localstackaws.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjds.localstackaws.dto.PersonSqsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonSqsConsumer {

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${aws.sqs.person}")
    public void receiveMessage(@Headers Map<String, Object> messageAttributes,
            @Payload String message) throws JsonProcessingException {
        try {
            log.info("[PersonSqsConsumer] Receive message: {}", message);
            PersonSqsDTO personSqsDTO = objectMapper.readValue(message, PersonSqsDTO.class);
            log.info("[PersonSqsConsumer] Receive person: {}", personSqsDTO.toString());
        } catch (Exception e) {
            log.error("[PersonSqsConsumer] Unexpected error consuming message {}. Error {}", message, e);
        }
    }

}
