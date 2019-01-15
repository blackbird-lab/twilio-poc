package com.blackbird.controller;

import com.blackbird.service.QueuePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    private QueuePublisherService publisherService;

    //take in a message via HTTP, publish to broker
    @RequestMapping(path="/publishMessage", method= RequestMethod.POST)
    public String publishMessage(@RequestBody String payload) {
        publisherService.publishMessage(payload);
        return "success";
    }
}
