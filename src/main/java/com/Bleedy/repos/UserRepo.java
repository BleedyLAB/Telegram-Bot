package com.Bleedy.repos;

import com.Bleedy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface UserRepo extends JpaRepository <User,Integer>{
    List<User> findByUserIdentification(Long userIdentification);
}
