package com.example.pratico;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer>{
    List<User> findAll();
}
