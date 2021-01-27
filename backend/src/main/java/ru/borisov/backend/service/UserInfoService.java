package ru.borisov.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.borisov.backend.dto.UserInfoDto;
import ru.borisov.backend.entity.UserInfoEntity;
import ru.borisov.backend.repository.UserInfoRepository;
import ru.borisov.backend.responce.ResponseMessage;
import ru.borisov.backend.schema.UserInfo;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.borisov.backend.constants.UserConstants.DESCRIPTION_SUCCESS;
import static ru.borisov.backend.constants.UserConstants.DESCRIPTION_WRONG;
import static ru.borisov.backend.constants.UserConstants.JSONDATA;
import static ru.borisov.backend.constants.UserConstants.PREPARE;
import static ru.borisov.backend.constants.UserConstants.RESULT_SUCCESS;
import static ru.borisov.backend.constants.UserConstants.RESULT_WRONG;
import static ru.borisov.backend.constants.UserConstants.TIME_PATTERN;
import static ru.borisov.backend.constants.UserConstants.WORK_DIR;

@Slf4j
@Service
public class UserInfoService {

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final UserInfoRepository userInfoRepository;


    @Value("${file.name.filePathPrepare}")
    private String filePathPrepare;


    @Autowired
    public UserInfoService(Validator validator,
                           UserInfoRepository userInfoRepository) {
        this.objectMapper = new ObjectMapper();
        this.validator = validator;
        this.userInfoRepository = userInfoRepository;
    }

    public ResponseMessage savePrepare(UserInfoDto request) {
        try {
            log.info("start to save data in file");
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            File mFile = createFile(PREPARE, JSONDATA);
            Files.write(mFile.toPath(), Collections.singletonList(json), StandardOpenOption.WRITE);
            return new ResponseMessage(RESULT_SUCCESS, DESCRIPTION_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseMessage(RESULT_WRONG, DESCRIPTION_WRONG + " data not saved");
        }

    }

    /**
     *
     * @param folderName - имя папки где будет создаваться файл
     * @param fileName - имя файла
     * @return - файл, который содрежит путь до него
     */
    private File createFile(String folderName, String fileName) throws IOException {
        log.info("start to create dir and docFile");
        File folder = new File(filePathPrepare+folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File mFile = new File(folder, fileName);
        mFile.createNewFile();
        return mFile;
    }

    /**
     * метод перемещает файл в папку work и создает пустой файл флаг включая время в формате ddMMyy_HHmm
     * @return - респонс с описанием
     */
    public ResponseMessage moveFile() {
        try {
            log.info("start to move docFile");
            File file = new File(filePathPrepare+PREPARE.concat("/").concat(JSONDATA));
            if (file.exists()) {
                String time = OffsetDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN));
                Files.move(Paths.get(filePathPrepare+PREPARE.concat("/").concat(JSONDATA)),
                        Paths.get(filePathPrepare.concat(WORK_DIR).concat("/json_".concat(time).concat(".txt"))));
                log.info("file moved successfully");
                createFile(WORK_DIR, "json_".concat(time).concat(".ready"));
                log.info("create flag file");
                return new ResponseMessage(RESULT_SUCCESS, DESCRIPTION_SUCCESS);
            } else {
                log.info("file not exists");
                return new ResponseMessage(RESULT_WRONG, DESCRIPTION_WRONG + " file not found in target dir");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseMessage(RESULT_WRONG, DESCRIPTION_WRONG + " file not moved" + e.getMessage());
        }

    }

    /**
     * метод валидирует данные по json схеме
     * @param body - тело файла
     * @return - прошла ли успешно валидация
     */
    public boolean checkFile(Object body) {

        if (!body.equals("")) {
            UserInfo userInfoRequest = null;
            try {
                userInfoRequest = objectMapper.readValue(body.toString(),UserInfo.class);
                Set<ConstraintViolation<UserInfo>> constraintViolations = validator.validate(userInfoRequest);
                if (constraintViolations.size() > 0) {
                    log.error("Body validation failed"+ ("Incorrect request body. Description: " +
                            constraintViolations
                                    .stream()
                                    .map(ConstraintViolation::toString)
                                    .collect(Collectors.joining(System.lineSeparator()))));
                    return false;
                }
                log.info("Validation success");
            } catch (JsonProcessingException e) {
                log.info(e.getMessage());
            }
            System.out.println(userInfoRequest);
            return true;
        }
        return true;
    }

    /**
     * метод добавляет в тело значение из work_address по имени и фамилии
     * @param body - тело файла
     * @return - измененное тело файла, которое дальше записывается в новый файл и новую папку - done
     */
    public Object loadWorkData(Object body) {

        try {
            UserInfo userInfoRequest = objectMapper.readValue(body.toString(), UserInfo.class);
            UserInfoEntity entity = userInfoRepository.getByFirstNameAndLastName(userInfoRequest.getFirstName(), userInfoRequest.getLastName());
            if (entity!=null) {
                JsonNode node = objectMapper.valueToTree(userInfoRequest);
                ObjectNode objNode = (ObjectNode) node;
                objNode.put("workAddress",entity.getWorkAddress());
                log.info("workAddress is added");
                return objNode;
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("entity not found, write empty file");
        return objectMapper.createObjectNode();
    }
}
