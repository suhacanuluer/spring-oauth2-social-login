package com.suhacan.springoauth2sociallogin.repository;

import com.suhacan.springoauth2sociallogin.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String username);
}
