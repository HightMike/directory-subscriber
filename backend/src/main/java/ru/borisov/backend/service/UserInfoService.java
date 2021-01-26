package ru.borisov.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.borisov.backend.dto.UserInfoDto;
import ru.borisov.backend.schema.UserInfo;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.borisov.backend.constants.UserConstants.JSONDATA;
import static ru.borisov.backend.constants.UserConstants.PREPARE;
import static ru.borisov.backend.constants.UserConstants.TIME_PATTERN;
import static ru.borisov.backend.constants.UserConstants.WORK_DIR;

@Slf4j
@Service
public class UserInfoService {

    private final ObjectMapper objectMapper;
    private final Validator validator;


    @Value("${file.name.filePathPrepare}")
    private String filePathPrepare;


    @Autowired
    public UserInfoService(Validator validator) {
        this.objectMapper = new ObjectMapper();
        this.validator = validator;
    }

    public void savePrepare(UserInfoDto request) {
        try {
            log.info("start to save data in file");
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            File mFile = createFile(PREPARE, JSONDATA);
            Files.write(mFile.toPath(), Collections.singletonList(json), StandardOpenOption.WRITE);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("data not saved" + e.getMessage());
        }

    }

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

    public void moveFile() {
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
            } else {
                log.info("file not exists");
                throw new RuntimeException("file not found in target dir");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("file not moved" + e.getMessage());
        }

    }

    public boolean checkFile(Object body) {

        if (!body.equals("")) {
            //TODO сделать проверку на одинаковые файлы
            UserInfo userInfoRequest = null;
            try {
                userInfoRequest = objectMapper.readValue(body.toString(),UserInfo.class);
                Set<ConstraintViolation<UserInfo>> constraintViolations = validator.validate(userInfoRequest);
                if (constraintViolations.size() > 0) {
                    log.error("Body validation failed");
                    throw new IllegalArgumentException("Incorrect request body. Description: " +
                            constraintViolations
                                    .stream()
                                    .map(ConstraintViolation::toString)
                                    .collect(Collectors.joining(System.lineSeparator())));
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

    public void loadData(Object body) {

        try {
            UserInfo userInfoRequest = objectMapper.readValue(body.toString(),UserInfo.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        log.info("file save to done success");

    }
}