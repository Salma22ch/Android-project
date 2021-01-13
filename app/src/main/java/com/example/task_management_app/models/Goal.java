package com.example.task_management_app.models;


import java.io.Serializable;

public class Goal implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private Integer icon;
    private Integer progress;

    public Goal( String title, String description, Integer icon, Integer progress) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.progress = progress;
    }
    public Goal(String title,String description){
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", progress=" + progress +
                '}';
    }
}
