package com.example.GeminiPrototype;

import java.util.List;

public class RegexType {

    private String content;
    private List<AuthorFields> authorFields;
    private List<CaseFields> caseFields;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public List<AuthorFields> getAuthorFields() {
        return authorFields;
    }
    public void setAuthorFields(List<AuthorFields> authorFields) {
        this.authorFields = authorFields;
    }
    public List<CaseFields> getCaseFields() {
        return caseFields;
    }
    public void setCaseFields(List<CaseFields> caseFields) {
        this.caseFields = caseFields;
    }

}
