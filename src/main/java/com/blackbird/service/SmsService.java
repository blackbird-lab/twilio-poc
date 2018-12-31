package com.blackbird.service;

import com.twilio.exception.TwilioException;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private RestTemplate restTemplate;
    private String MESSAGES_ENDPOINT = "/Accounts/AC311dff67137d97631919803846039968/Messages.json";
    private static final String ACCOUNT_SID = "AC311dff67137d97631919803846039968";
    private static final String AUTH_TOKEN = "a1b5f305a567b72f24db663842558162";
    @Value("${twilio.api.url}")
    private String REST_API;

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
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
     * Current preferable solution. We can create REST client for each user.
     * {@link Response} contains information about created message in case of successful request
     * or if request failed it will contain error code which help us indicate the problem
     */
    public String sendMessageViaTwilioRestClient(String to, String from, String text) {
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        Request request = new Request(com.twilio.http.HttpMethod.POST, REST_API + MESSAGES_ENDPOINT);
        request.addPostParam("Body", text);
        request.addPostParam("To", to);
        request.addPostParam("From", from);
        Response response = client.request(request);
        return response.getContent();
    }

    /**
     * Using Spring RESTTemplate
     */
    public void sendMessageViaSpringREST(String to, String from, String text) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("To", to);
        params.add("From", from);
        params.add("Body", text);
        ResponseEntity<String> responseEntity = restTemplate.exchange(REST_API + MESSAGES_ENDPOINT,
                HttpMethod.POST,
                new HttpEntity<>(params, createBasicAuthorisation(ACCOUNT_SID, AUTH_TOKEN)),
                String.class);
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
