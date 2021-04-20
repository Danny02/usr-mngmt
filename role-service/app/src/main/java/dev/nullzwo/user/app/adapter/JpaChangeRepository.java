package dev.nullzwo.user.app.adapter;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface JpaChangeRepository extends Repository<ChangeEntity, Long> {
    void save(ChangeEntity change);

    void deleteAll();

    @Query(name = "count_changes_by_user", nativeQuery = true)
    List<UserChangesDto> changeCountByUser();

    List<ChangeEntity> findByUserid(String userId);
}
