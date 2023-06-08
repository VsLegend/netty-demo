package com.traning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Wang Junwei
 * @date 2023/6/8 14:59
 */

@RestController
public class Api {


    @GetMapping("/api")
    public String getAndSent(String content) {
        return content + "\n" + "Reply：" + LocalDateTime.now();
    }

}
