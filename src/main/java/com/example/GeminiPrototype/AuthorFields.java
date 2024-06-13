package com.example.GeminiPrototype;

public class AuthorFields {


    private String name;
    private String description;
    private String regex;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }

    // implement toString()
    @Override
    public String toString() {
        return "AuthorFields [name=" + name + ", description=" + description + ", regex=" + regex + "]";
    }


}

