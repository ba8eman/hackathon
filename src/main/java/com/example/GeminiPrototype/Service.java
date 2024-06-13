package com.example.GeminiPrototype;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Blob;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Service {

    // constant list

    // map for getting stuff from frontend
    HashMap<String, String> map = new HashMap<String, String>();

    public String promptBuilder(RegexType contentData) {
        StringBuilder sb = new StringBuilder();
        sb.append("The definitions for Case Fields are as follows : ");
        for (int i = 0; i < contentData.getCaseFields().size(); i++) {
            sb.append("Casefield name : ").append(contentData.getCaseFields().get(i).getName()).append(", ");
            sb.append("Description : ").append(contentData.getCaseFields().get(i).getDescription());
            sb.append(" /n ");
        }
        sb.append("and the definitions for author Fields are as follows : ");
        for (int i = 0; i < contentData.getAuthorFields().size(); i++) {
            sb.append("Authorfield name : ").append(contentData.getAuthorFields().get(i).getName()).append(", ");
            sb.append("Description : ").append(contentData.getAuthorFields().get(i).getDescription());
            sb.append(" /n ");
        }
        sb.append("The text you need to extract the data from is : ");

        sb.append(removeHandle(contentData.getContent()));
        return sb.toString();
    }

    public ExtractedFields callGemini(RegexType contentData) {

        ExtractedFields extractedFields = new ExtractedFields();

        try (VertexAI vertexAi = new VertexAI("dteam-codeleidoscope", "us-central1");) {
            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(2000)
                            .setTemperature(1F)
                            .setTopP(0.95F)
                            .build();
            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build()
            );
            Content systemInstruction = ContentMaker.fromMultiModalData("You are a text information extractor. You need to extract Case Fields and Author Fields from the text input i give based on the definitions of Case Fields and Author Fields I provide you with.Send the data back in this format {CaseFields: [{FieldName:CaseField1Name,FieldValue:CaseField1Value},{FieldName:CaseField2Name,FieldValue:CaseField2Value}, ...], AuthorFields: [{FieldName:AuthorField1Name,FieldValue:AuthorField1Value},{FieldName:AuthorField2Name,FieldValue:AuthorField2Value}, ...]}");
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemini-1.5-pro-001")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .setSafetySettings(safetySettings)
                            .setSystemInstruction(systemInstruction)
                            .build();
            String prompt = promptBuilder(contentData);
            System.out.println(prompt);
            Content content = ContentMaker.fromMultiModalData(prompt);
            try {
                GenerateContentResponse response = model.generateContent(content);
                StringBuilder sb = new StringBuilder();
                if (response.getCandidatesCount() > 0 && response.getCandidates(0).getContent().getPartsCount() > 0) {
                    for (int i = 0; i < response.getCandidates(0).getContent().getPartsCount(); i++) {
                        sb.append(response.getCandidates(0).getContent().getParts(i).getText()).append(" ");
                    }
                }
                String fullResponse = sb.toString().trim();
                System.out.println(fullResponse);

                try{
                    fullResponse = sb.substring(sb.indexOf("{"), sb.lastIndexOf("}") + 1);
                    System.out.println(fullResponse);
                    Gson gson = new Gson();
                    extractedFields = gson.fromJson(fullResponse, ExtractedFields.class);

                }catch(Exception e){
                    System.out.println("Error: "+e);
                }
                System.out.println(extractedFields.getAuthorFields().size());
                System.out.println(extractedFields.getCaseFields().size());
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        return extractedFields;
    }

    public String removeHandle(String content) {
        String contentWithoutHandle="";
        if (content.trim().isEmpty()){
            return contentWithoutHandle;
        }
        else{
            String[] tokens = content.split(" ");
            for(String token : tokens) {
                if(token.startsWith("@")==false) {
                    contentWithoutHandle=contentWithoutHandle+" "+token;
                }
            }
        }
        return contentWithoutHandle;
    }

}


