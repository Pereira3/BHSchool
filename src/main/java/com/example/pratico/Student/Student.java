package com.example.pratico.Student;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "student")
public class Student{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Em AUTO dá valores de IDS mt grandes e sem serem sequenciais
    private int ids;
    private String name;
    private String password;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
    private String address;
    private int idc;
    private float average;
    private int state; //1 - em Curso || 2 - Finalizado || 3 - Desistiu

    public int getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdc() {
        return idc;
    }
    public void setIdc(int idc) {
        this.idc = idc;
    }

    public float getAverage() {
        return average;
    }
    public void setAverage(float average) {
        this.average = average;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return ids == student.ids && idc == student.idc && Float.compare(average, student.average) == 0 && state == student.state && Objects.equals(name, student.name) && Objects.equals(password, student.password) && Objects.equals(email, student.email) && Objects.equals(birthdate, student.birthdate) && Objects.equals(address, student.address);
    }

    @Override
    public String toString() {
        return "Student{" +
                "ids=" + ids +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", address='" + address + '\'' +
                ", idc=" + idc +
                ", average=" + average +
                ", state=" + state +
                '}';
    }
}

