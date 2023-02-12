package me.qushe8r.studyspringsecurity5_7.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfigController {

    @GetMapping("/config")
    public String config() {
        return "/admin/config";
    }
}
