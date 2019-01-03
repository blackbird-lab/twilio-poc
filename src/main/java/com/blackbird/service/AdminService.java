package com.blackbird.service;

import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private static final String ACCOUNT_SID = "AC311dff67137d97631919803846039968";
    private static final String AUTH_TOKEN = "a1b5f305a567b72f24db663842558162";
    @Value("${twilio.api.url}")
    private String REST_API;

    public String createAccount(String friendlyName) {
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        String ACCOUNT_ENDPOINT = "/Accounts.json";
        Request request = new Request(com.twilio.http.HttpMethod.POST, REST_API + ACCOUNT_ENDPOINT);
        request.addPostParam("FriendlyName", friendlyName);
        Response response = client.request(request);
        return response.getContent();
    }
}
