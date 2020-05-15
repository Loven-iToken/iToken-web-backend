package com.loven.iToken.web.backend.controller.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by loven on 2020/5/12.
 */
@Controller
public class PageController {
    @GetMapping(value = {"", "main"})
    public String gotoMain(){
        return "main";
    }

    @GetMapping(value = "detail")
    public String gotoDetail(){
        return "detail";
    }
}
