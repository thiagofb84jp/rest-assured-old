package br.pb.thiagofb84jp.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class Recipe {

    @JsonIgnore
    private Integer id;
    private String title;
    private String body;

    public Recipe(Integer id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Recipe(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Recipe() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
