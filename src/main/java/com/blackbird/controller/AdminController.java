package com.blackbird.controller;

import com.blackbird.service.AdminService;
import com.twilio.rest.api.v2010.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Webhook for incoming messages
     **/
    @PostMapping(value = "/account")
    public void createAccount(@RequestBody Account account) {
        adminService.createAccount(account.getFriendlyName());
    }
}
