package com.example.GeminiPrototype;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class Controller {

    Service service = new Service();
    Regex regex = new Regex();
    @PostMapping("/gemini")
    @CrossOrigin("http://localhost:4200")
    public AllExtractions handleRequest(@RequestBody RegexType data){
         AllExtractions allExtractions = new  AllExtractions();
         ExtractedFields aiFields =service.callGemini(data);
         allExtractions.setAiFields(aiFields);
         ExtractedFields regexFields = regex.RegexExtraction(data);
         allExtractions.setRegexFields(regexFields);
         return allExtractions;
    }
}
