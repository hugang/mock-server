package io.hugang.mock;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/mock/api")
public class MockServerController {

    private static final Logger log = LoggerFactory.getLogger(MockServerController.class);

    @Value("${db.json.path}")
    private String dbJsonPath;

    private static List<UrlEntry> urlEntries = new ArrayList<>();
    public static ObjectMapper mapper = new ObjectMapper();
    public static String BASE_DIR = new File("").getAbsolutePath();

    @PostConstruct
    public void init() {
        try {
            if (!new File(BASE_DIR + "/db.json").exists()) {
                BASE_DIR = dbJsonPath;
            }
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            urlEntries.addAll(Arrays.asList(mapper.readValue(new File(BASE_DIR + "/db.json"), UrlEntry[].class)));
        } catch (IOException e) {
            log.error("Failed to read db.json", e);
        }
    }


    public UrlEntry getUrl(UrlEntry urlEntry) {
        for (UrlEntry entry : urlEntries) {
            if (entry.getUrl().equals(urlEntry.getUrl())
                    && entry.getMethod().equals(urlEntry.getMethod())
                    && getParam(entry.getParam()).equals(getParam(urlEntry.getParam()))) {
                return entry;
            }
        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUrl(@RequestBody UrlEntry urlEntry) {
        urlEntries.add(urlEntry);
        saveEntries();
        return new ResponseEntity<>(urlEntry.getData(), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteUrl(@RequestBody UrlEntry urlEntry) {
        for (UrlEntry entry : urlEntries) {
            if (entry.getUrl().equals(urlEntry.getUrl())
                    && entry.getMethod().equals(urlEntry.getMethod())
                    && getParam(entry.getParam()).equals(getParam(urlEntry.getParam()))) {
                urlEntries.remove(entry);
                saveEntries();
                return new ResponseEntity<>(entry.getData(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("URL not found", HttpStatus.NOT_FOUND);
    }

    private void saveEntries() {
        try {
            String json = mapper.writeValueAsString(urlEntries);
            Files.write(Paths.get(BASE_DIR + "/db.json"), json.getBytes());
        } catch (IOException e) {
            log.error("Failed to read db.json", e);
        }
    }

    private String getParam(String param) {
        if (param != null) {
            return param;
        } else {
            return "";
        }
    }
}

