package ru.borisov.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class UserInfoService {

    private final CamelContext camelContext;

    @Autowired
    public UserInfoService(CamelContext camelContext) {
        this.camelContext = camelContext;
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
