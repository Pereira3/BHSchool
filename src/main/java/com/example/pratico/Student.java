package com.example.pratico;

import java.util.Objects;

public class Student extends User{
    private static int numStudent;
    private int media;
    private int matricula; //1 - em Curso || 2 - Finalizado || 3 - Desistiu

    public Student(String name, String email, String password) {
        super(name, email, password, false, 1);
    }

    public static int getNumStudent() {
        return numStudent;
    }

    public static void setNumStudent(int numStudent) {
        Student.numStudent = numStudent;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
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
        return media == student.media && matricula == student.matricula;
    }

    @Override
    public String toString() {
        return "Student{" +
                "media=" + media +
                ", matricula=" + matricula +
                '}';
    }
}

