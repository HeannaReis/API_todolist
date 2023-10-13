package com.heannareis.todolist.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heannareis.todolist.domain.model.UserModel;

public interface IUserRepository extends JpaRepository <UserModel, UUID>{
    UserModel findByUsername(String username);
    
}
