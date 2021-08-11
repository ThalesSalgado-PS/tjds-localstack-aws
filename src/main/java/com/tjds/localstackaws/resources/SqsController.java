package com.tjds.localstackaws.resources;

import com.tjds.localstackaws.dto.PersonSqsDTO;
import com.tjds.localstackaws.services.PersonSqsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sqs")
public class SqsController {

    private final PersonSqsService personSqsService;

    @PostMapping("/person-queue")
    public ResponseEntity<Void> producePersonQueueMessage(@RequestBody PersonSqsDTO personSqsDTO) {
        personSqsService.sendMessageSync("/person-queue", personSqsDTO);
        return ResponseEntity.noContent().build();
    }
}
