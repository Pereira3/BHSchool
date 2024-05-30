package com.example.pratico.Admin;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
    List<Admin> findAll();
    Admin findAdminByEmail(String email);
    Admin findAdminByEmailAndPassword(String email, String password);
}
