package com.javvathehutt.hic_server.income;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {
    @GetMapping(value = "/hic/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getIndexHtml() {
        return "index";
    }
}
