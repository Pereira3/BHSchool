package com.example.pratico;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
public class Student extends User{
    private float average;
    private int matricula; //1 - em Curso || 2 - Finalizado || 3 - Desistiu

    public Student(User u, float average, int matricula) {
        super(u.getIdu(), u.getName(), u.getEmail(), u.getPassword(), u.getRole(), u.getBirthDate(), u.getAddress());
        this.average = average;
        this.matricula = matricula;
    }

    public Student() {
        super(); // Chama o construtor padrão da classe pai
    }

    public float getAverage() {
        return average;
    }

    public void setMedia(int media) {
        this.average = media;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return average == student.average && matricula == student.matricula;
    }

    @Override
    public String toString() {
        return "Student{" +
                "media=" + average +
                ", matricula=" + matricula +
                '}';
    }
}

