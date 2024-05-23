package com.example.pratico;

import java.util.Objects;

public class Course {
private static int idc = 0;
    private String name;
    private String description;
    private int duration; // in hours

    public Course(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return duration == course.duration && Objects.equals(name, course.name) && Objects.equals(description, course.description);
    }

    @Override
    public String toString() {
        return "Curso{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
}
