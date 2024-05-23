package com.example.pratico;

import java.util.List;
import java.util.Objects;

public class Teacher extends User {
    private List<Course> courses;
    private float salary;

    public Teacher(String name, String email, String password, String specialization, List<Course> courses) {
        super(name, email, password, false, 2);
        this.courses = courses;
    }

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
