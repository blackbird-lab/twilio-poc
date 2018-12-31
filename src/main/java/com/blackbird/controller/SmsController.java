package com.blackbird.controller;

import com.blackbird.service.SmsService;
import com.twilio.http.Response;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * Webhook for incoming messages
     **/
    @PostMapping(value = "/reply", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void receiveMessage(@RequestParam("Body") String body, @RequestParam("From") String from, @RequestParam("To") String to) {
        smsService.sendMessageViaTwilioRestClient(from, to, "received your message: " + body);
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        smsService.sendMessage(message.getTo(), message.getFrom().toString(), message.getBody());
    }


    @PostMapping("/sendViaTwilioRestClient")
    public String sendMessageViaREST(@RequestBody Message message) {
        return smsService.sendMessageViaTwilioRestClient(message.getTo(), message.getFrom().toString(), message.getBody());
    }

    @PostMapping("/sendViaSpringREST")
    public void sendMessageViaSpringREST(@RequestBody Message message) {
        smsService.sendMessageViaSpringREST(message.getTo(), message.getFrom().toString(), message.getBody());
    }

}
