package com.example.pratico.Teacher;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "teacher")
public class Teacher{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idt;
    private String name;
    private String password;
    private String email;
    private String birthdate;
    private String address;
    private int idc;
    private int salary;

    public int getIdt() {
        return idt;
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

    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
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

    public float getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return idt == teacher.idt && idc == teacher.idc && salary == teacher.salary && Objects.equals(name, teacher.name) && Objects.equals(password, teacher.password) && Objects.equals(email, teacher.email) && Objects.equals(birthdate, teacher.birthdate) && Objects.equals(address, teacher.address);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "idt=" + idt +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", address='" + address + '\'' +
                ", idc=" + idc +
                ", salary=" + salary +
                '}';
    }
}
