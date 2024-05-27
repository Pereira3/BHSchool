package com.example.pratico;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idu;
    private String name;
    private String email;
    private String password;
    private int role; // 0 - "Admin" or 1 - "aluno" or 2 - "professor"
    private String BirthDate;
    private String address;

    public User(int id, String name, String email, String password, int role, String BirthDate, String address) {
        this.idu = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.BirthDate = BirthDate;
        this.address = address;
    }

    public User() {
        // Construtor padrão
    }

    public int getIdu() {
        return idu;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", BirthDate='" + BirthDate + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
