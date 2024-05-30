package com.example.pratico.Admin;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Em AUTO dá valores de IDS mt grandes e sem serem sequenciais
    private int ida;
    private String name;
    private String password;
    private String email;

    public int getIda() {
        return ida;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return ida == admin.ida && Objects.equals(name, admin.name) && Objects.equals(password, admin.password) && Objects.equals(email, admin.email);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "ida=" + ida +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
