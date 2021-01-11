package com.example.task_management_app.models;


public class Goal {
    private Integer id;
    private String title;
    private String description;
    private String icon;
    private Integer progress;

    public Goal(Integer id, String title, String description, String icon, Integer progress) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.progress = progress;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
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
