package com.example.pratico;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int idc;
    private String name;
    private String description;
    private int duration; // in hours
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "idu")
    private Teacher teacher;

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
