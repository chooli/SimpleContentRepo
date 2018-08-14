package com.jumkid.media.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * This is the data model for blog
 */

@Document(indexName = "answer", type="doc")
public class Answer {

    @Id
    @Field(type = FieldType.keyword)
    private String id;

    @Field(type = FieldType.text)
    private String author;

    @Field(type = FieldType.text)
    private String title;

    @Field(type = FieldType.Date)
    private Date postDate;

    @Field(type = FieldType.text)
    private String colorCode;

    @Field(type = FieldType.text)
    private String response;

    @Field(type = FieldType.text)
    private String representedQuestion;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRepresentedQuestion() {
        return representedQuestion;
    }

    public void setRepresentedQuestion(String representedQuestion) {
        this.representedQuestion = representedQuestion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}