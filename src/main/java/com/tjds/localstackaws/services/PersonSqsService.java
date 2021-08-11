package com.tjds.localstackaws.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjds.localstackaws.dto.PersonSqsDTO;
import com.tjds.localstackaws.queue.PersonSqsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonSqsService {

    private final PersonSqsProducer personSqsProducer;
    private final ObjectMapper objectMapper;

    public void sendMessageSync(String urlQueue, PersonSqsDTO personSqsDTO) {
        try {
            personSqsProducer.sendMessageSync(urlQueue, objectMapper.writeValueAsString(personSqsDTO));
        } catch (Exception e) {
            log.error("[PersonSqsService] Unexpected error sending sqs message. Queue {}. Message {}. Error {}", urlQueue, personSqsDTO.toString(), e);
        }
    }

}
