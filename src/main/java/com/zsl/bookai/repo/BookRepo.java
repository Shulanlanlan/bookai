package com.zsl.bookai.repo;

import com.zsl.bookai.entity.BookEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends CrudRepository<BookEntity, Long> {
    @Query(
        """
            SELECT * FROM book
            WHERE :tag = ANY(tags)
            """
    )
    List<BookEntity> getAllByTag(@Param("tag") String tag);

    @Query(
        """
            SELECT * FROM book
            """
    )
    List<BookEntity> getAllBooks();
}
