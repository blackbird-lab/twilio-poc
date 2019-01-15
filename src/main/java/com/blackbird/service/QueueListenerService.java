package com.blackbird.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@Slf4j
public class QueueListenerService {

    @StreamListener(target= Sink.INPUT)
    public void listen(String msg) {
        log.debug(msg);
    }
}
