package com.tjds.localstackaws.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonSqsConsumer {

    @JmsListener(destination = "${aws.queue.wipe.account.block}")
    public void receiveMessage(WipeAccountQueueDTO message) {
        log.info("[DefaultConsumer] Receive message: {}", message);
    }

}
