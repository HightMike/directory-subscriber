package ru.borisov.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.borisov.backend.dto.UserInfoDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class UserInfoService {

    private final CamelContext camelContext;
    private final ObjectMapper objectMapper;

    @Value("${file.name.filePathPrepare}")
    private String filePathPrepare;


    @Autowired
    public UserInfoService(CamelContext camelContext) {
        this.camelContext = camelContext;
        this.objectMapper = new ObjectMapper();
    }

    public void savePrepare(UserInfoDto request) {
        try {
            File file = new File(getClass().getResource("static/files/work/test.json").getFile());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            Files.write(new File(filePathPrepare).toPath(), Collections.singletonList(json), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startRoute() {

    }

    public void testUser() {

//        ObjectMapper mapper = new ObjectMapper();
//        //добавить чтение из файла и валидацию
//        // convert JSON file to map
//        try {
//            String path = new ClassPathResource("test.json").getPath();
//            Map<?, ?> map = mapper.readValue(Paths.get(path).toFile(), Map.class);
//            System.out.println(map.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try (InputStream inputStream = getClass().getResourceAsStream(new ClassPathResource("userInfo.json").getPath())) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject("{\"hello\" : \"world\"}")); // throws a ValidationException if this object is invalid
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("here");

    }

}
