package com.example.task_management_app.models;

import java.util.Date;

public class Note {

    // Attributes
    private Integer id;
    private String title;
    private String description;
    private String category;
    private Long date;
    private String priority;
    private String state;
    private String type; // a supprimer

    // Constructors


    public Note(Integer id, String title, String description, String category, Long date, String priority, String state, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.priority = priority;
        this.state = state;
        this.type = type;
    }

    public Note() {
    }

    // Getters and Setters


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", priority='" + priority + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
