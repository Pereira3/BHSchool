package com.example.pratico;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teacher")
public class Teacher extends User {
    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;
    public float salary;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(courses, teacher.courses) && Objects.equals(salary, teacher.salary);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "salary='" + salary + '\'' +
                ", courses=" + courses +
                "} " + super.toString();
    }
}
