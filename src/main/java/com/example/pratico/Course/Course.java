package com.example.pratico.Course;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Em AUTO dá valores de IDS mt grandes e sem serem sequenciais
    public int idc;
    private String name;
    private String description;
    private int duration; // in hours
    private int capacity;

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
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return idc == course.idc && duration == course.duration && capacity == course.capacity && Objects.equals(name, course.name) && Objects.equals(description, course.description);
    }

    @Override
    public String toString() {
        return "Course{" +
                "idc=" + idc +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", capacity=" + capacity +
                '}';
    }
}
