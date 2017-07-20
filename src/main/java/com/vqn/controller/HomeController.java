package com.vqn.controller;

import com.vqn.Service.SimsimiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by vqnguyen on 7/17/2017.
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    SimsimiChatService simsimiChatService;

    @GetMapping(value = "/")
    public ResponseEntity<String> home() {
        return  new ResponseEntity<>("running", HttpStatus.OK);
    }

    @GetMapping(value = "/chat/{lang}/{mess}")
    public ResponseEntity<String> chatting(@PathVariable String lang, @PathVariable String mess) throws Exception {
        String res = simsimiChatService.chatWithSimsimi(mess,lang);
        return  new ResponseEntity<>(res, HttpStatus.OK);
    }
}
