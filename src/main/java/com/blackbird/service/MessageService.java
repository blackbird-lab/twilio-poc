package com.blackbird.service;

import com.blackbird.dto.MessageDto;
import com.blackbird.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.exception.TwilioException;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@Service
public class MessageService {

    private RestTemplate restTemplate;
    private MessageRepository repository;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;

    private String MESSAGES_ENDPOINT = "/Accounts/AC311dff67137d97631919803846039968/Messages.json";
    private static final String ACCOUNT_SID = "AC311dff67137d97631919803846039968";
    private static final String AUTH_TOKEN = "a1b5f305a567b72f24db663842558162";
    @Value("${twilio.api.url}")
    private String REST_API;

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Autowired
    public MessageService(MessageRepository repository, RestTemplate restTemplate,
                          ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    /**
     * Can not use this because of static initialization Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
     */
    public void sendMessage(String to, String from, String text) {
        try {
            Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(from), text).create();
            log.debug("Message id: " + message.getSid() + " was sent");
        } catch (TwilioException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * We can create TwilioRestClient client for each user.
     * {@link Response} contains information about created message in case of successful request
     * or if request fails it will contain error code which help us indicate the problem
     */
    public MessageDto sendMessageViaTwilioRestClient(String to, String from, String text) {
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        Request request = new Request(com.twilio.http.HttpMethod.POST, REST_API + MESSAGES_ENDPOINT);
        request.addPostParam("Body", text);
        request.addPostParam("To", to);
        request.addPostParam("From", from);
        MessageDto message = null;
        try {
            message = objectMapper.readValue(client.request(request).getContent(), MessageDto.class);
            repository.save(modelMapper.map(message, com.blackbird.entity.Message.class));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return message;
    }

    /**
     * Using Spring RESTTemplate
     * In case of exception HttpClientErrorException contains number of error in Body part
     */
    public void sendMessageViaSpringREST(String to, String from, String text) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("To", to);
        params.add("From", from);
        params.add("Body", text);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(REST_API + MESSAGES_ENDPOINT,
                    HttpMethod.POST,
                    new HttpEntity<>(params, createBasicAuthorisation(ACCOUNT_SID, AUTH_TOKEN)),
                    String.class);
            log.debug("Response entity: " + responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            log.error(e.getResponseBodyAsString());
            log.error(e.getMessage(), e);
        }

    }

    private HttpHeaders createBasicAuthorisation(String username, String password) {
        HttpHeaders httpHeaders = new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }

}
