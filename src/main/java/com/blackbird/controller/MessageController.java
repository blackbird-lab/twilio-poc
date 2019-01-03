package com.blackbird.controller;

import com.blackbird.dto.MessageDto;
import com.blackbird.service.MessageService;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Webhook for incoming messages
     **/
    @PostMapping(value = "/reply", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public MessageDto receiveMessage(@RequestParam("Body") String body, @RequestParam("From") String from, @RequestParam("To") String to) {
        return messageService.sendMessageViaTwilioRestClient(from, to, "received your message: " + body);
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message.getTo(), message.getFrom().toString(), message.getBody());
    }


    @PostMapping("/sendViaTwilioRestClient")
    public MessageDto sendMessageViaREST(@RequestBody Message message) {
        return messageService.sendMessageViaTwilioRestClient(message.getTo(), message.getFrom().toString(), message.getBody());
    }

    @PostMapping("/sendViaSpringREST")
    public void sendMessageViaSpringREST(@RequestBody Message message) {
        messageService.sendMessageViaSpringREST(message.getTo(), message.getFrom().toString(), message.getBody());
    }

}
