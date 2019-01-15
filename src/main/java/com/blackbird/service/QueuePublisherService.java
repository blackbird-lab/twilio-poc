package com.blackbird.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;

@EnableBinding(Source.class)
@Slf4j
public class QueuePublisherService {

    @Autowired
    private Source mySource;

    public void publishMessage(@RequestBody String payload) {
        log.debug(payload);
        //send message to channel
        mySource.output().send(MessageBuilder.withPayload(payload).build());
    }
}
