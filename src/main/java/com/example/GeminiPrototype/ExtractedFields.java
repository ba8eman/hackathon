package com.example.GeminiPrototype;

import java.util.List;

public class ExtractedFields {
    List<Fields> CaseFields;
    List<Fields> AuthorFields;

    public List<Fields> getCaseFields() {
        return CaseFields;
    }

    public void setCaseFields(List<Fields> caseFields) {
        CaseFields = caseFields;
    }

    public List<Fields> getAuthorFields() {
        return AuthorFields;
    }

    public void setAuthorFields(List<Fields> authorFields) {
        this.AuthorFields = authorFields;
    }
}
