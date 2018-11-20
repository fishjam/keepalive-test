package com.fishjam.keepalivetest.server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/timer")
public class TimerController {

    @GetMapping("/current")
    public String current(HttpServletResponse response){
        response.addHeader("Keep-Alive", "timeout=10");
        return LocalDateTime.now().toString();
    }
}
