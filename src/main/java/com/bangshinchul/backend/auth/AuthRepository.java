package com.bangshinchul.backend.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<Auth, Long> {
    Auth findByUsername(String userId);
    Auth findByUsernameAndPassword(String userId, String password);
}
