package com.zsl.bookai.repo;

import com.zsl.bookai.entity.FavoriteEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoriteRepo extends CrudRepository<FavoriteEntity, Long> {
    @Query("SELECT book_id FROM favorite WHERE user_id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM favorite f WHERE f.user_id = :userId AND f.book_id = :bookId")
    void deleteByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
