package dev.nullzwo.user.app.adapter;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface JpaUserRepository extends Repository<UserEntity, String> {

    void deleteById(String id);

    Optional<UserEntity> findById(String id);

    List<UserEntity> findAll();

    void save(UserEntity userEntity);

    void deleteAll();
}
