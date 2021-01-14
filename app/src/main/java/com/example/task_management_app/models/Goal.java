package com.example.task_management_app.models;


import java.io.Serializable;

public class Goal implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private Integer icon;
    private Integer MaxProgress;
    private Integer progressCurrent;

    public Goal(Integer id, String title, String description, Integer icon, Integer maxProgress, Integer progressCurrent) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        MaxProgress = maxProgress;
        this.progressCurrent = progressCurrent;
    }

    public Goal(String title, String description, Integer icon, Integer maxProgress, Integer progressCurrent) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        MaxProgress = maxProgress;
        this.progressCurrent = progressCurrent;
    }

    public Integer getMaxProgress() {
        return MaxProgress;
    }

    public void setMaxProgress(Integer maxProgress) {
        MaxProgress = maxProgress;
    }

    public Integer getProgressCurrent() {
        return progressCurrent;
    }

    public void setProgressCurrent(Integer progressCurrent) {
        this.progressCurrent = progressCurrent;
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

}
