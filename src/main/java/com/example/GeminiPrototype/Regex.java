package com.example.GeminiPrototype;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Regex {

    public class Pair<T, U> {
        public final T first;
        public final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public Object getKey() {
            return null;
        }
    }

    private String removeUnwantedCharacters(String supportedValue) {
        if (supportedValue != null && !supportedValue.isEmpty()) {
            if (supportedValue.charAt(0) == '^' && supportedValue.charAt(supportedValue.length() - 1) == '$') {
                supportedValue = supportedValue.substring(1, supportedValue.length() - 1);
            }
        }

        return supportedValue;
    }

    public ExtractedFields RegexExtraction(RegexType regexType){
        String content = regexType.getContent();
        System.out.println("Content is : "+content);

        List<Pair<String,String>> combinedSupportedValues = new ArrayList<>();
        List<AuthorFields> authorFields = regexType.getAuthorFields();

        ExtractedFields extractedFields = new ExtractedFields();

        for(AuthorFields authorField:authorFields){
            String regex = authorField.getRegex();
            String displayName = authorField.getName();
            Pair<String,String> supportedValue = new Pair<String,String>(regex,displayName);
            combinedSupportedValues.add(supportedValue);
        }

        List<Fields> fields1 = new ArrayList<>();
        List<Fields> fields2 = new ArrayList<>();




        for (Pair<String, String> supportedValue : combinedSupportedValues) {

                String regex = supportedValue.first;
                String displayName = supportedValue.second;

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    String match = matcher.group();
                    Fields field = new Fields();
                    field.setFieldName(displayName);
                    System.out.println("Matched value in author is : "+match);
                    field.setFieldValue(match);
                    fields1.add(field);
                }
        }

        // empty combinedSupportedValues
        combinedSupportedValues.clear();

        // get case fields


        List<CaseFields> caseFields = new ArrayList<>();
        caseFields=regexType.getCaseFields();

        for(CaseFields caseField:caseFields){
            String regex = caseField.getRegex();
            String displayName = caseField.getName();
            Pair<String,String> supportedValue = new Pair<String,String>(regex,displayName);
            combinedSupportedValues.add(supportedValue);
        }

        for (Pair<String, String> supportedValue : combinedSupportedValues) {

                String regex = supportedValue.first;
                String displayName = supportedValue.second;

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(content);
                while (matcher.find()) {
                    String match = matcher.group();
                    Fields field = new Fields();
                    field.setFieldName(displayName);
                    System.out.println("Matched value in case is : "+match);
                    field.setFieldValue(match);
                    fields2.add(field);

                }
        }

        extractedFields.setAuthorFields(fields1);
        extractedFields.setCaseFields(fields2);


    return  extractedFields;
    }
}
