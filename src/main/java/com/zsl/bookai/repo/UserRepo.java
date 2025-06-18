package com.zsl.bookai.repo;

import com.zsl.bookai.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> getByUsername(String username);
}
