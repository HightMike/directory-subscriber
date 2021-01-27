package ru.borisov.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.borisov.backend.dto.UserInfoDto;
import ru.borisov.backend.responce.ResponseMessage;
import ru.borisov.backend.service.UserInfoService;

@Slf4j
@RestController
@RequestMapping("/data")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService){
        this.userInfoService = userInfoService;

    }

    /**
     * Метод предназначенный для подготовки данных и сохранении их в папку prepare
     * @param request - запрос данными с данными с формы
     * @author - hightmike
     */
    @PostMapping(value = "/prepare")
    public ResponseMessage prepareDoc(
            @RequestBody UserInfoDto request) {
        return userInfoService.savePrepare(request);
    }

    /**
     * Метод предназначенный для перемещения файла в папку work и его переименовывании. Добавляет к названии дату и время
     * @author - hightmike
     */
    @PutMapping(value = "/move")
    public ResponseMessage moveDoc() {
        return userInfoService.moveFile();
    }

}
