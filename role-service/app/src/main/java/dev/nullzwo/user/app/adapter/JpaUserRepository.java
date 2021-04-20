package dev.nullzwo.user.app.adapter;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface JpaUserRepository extends Repository<UserEntity, String> {

    @Modifying
    @Query("delete from UserEntity u where u.id = ?1")
    int deleteById(String id);

    Optional<UserEntity> findById(String id);

    List<UserEntity> findAll();

    void save(UserEntity userEntity);

    void deleteAll();
}
