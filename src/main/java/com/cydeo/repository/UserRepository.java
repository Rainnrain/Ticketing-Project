package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    // get the user based on their userName

    User findByUserName(String userName);
    @Transactional
    void deleteByUserName(String userName);

}
