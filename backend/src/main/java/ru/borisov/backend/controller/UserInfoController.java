package ru.borisov.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.borisov.backend.dto.UserInfoDto;
import ru.borisov.backend.service.UserInfoService;

@Slf4j
@RestController
@RequestMapping("/data")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;

    }

    @PostMapping(value = "/prepare")
    public void prepareDoc(
            @RequestBody UserInfoDto request) {
        userInfoService.savePrepare(request);
        log.info("doc prepared");
    }

    @PutMapping(value = "/move")
    public void moveDoc() {
        userInfoService.moveFile();
        log.info("move file complete");
    }

}
