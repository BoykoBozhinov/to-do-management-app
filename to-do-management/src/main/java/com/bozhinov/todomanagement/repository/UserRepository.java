package com.bozhinov.todomanagement.repository;

import com.bozhinov.todomanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findFirstByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
}
