package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    Page<Post> getAllBy(Pageable pageable);
    Page<Post> getAllByActiveOrderByDateAsc(Boolean active, Pageable pageable);

    @Query("SELECT p from Post p WHERE p.name LIKE %:searchString% OR p.content LIKE %:searchString% " +
            "OR p.youtubeLink LIKE %:searchString% ORDER BY p.date ASC")
    Page<Post> getAllBySearchString(@Param("searchString") String searchString, Pageable pageable);

    @Query("SELECT p from Post p WHERE p.active = :active " +
            "AND (p.name LIKE %:searchString% OR p.content LIKE %:searchString% OR p.youtubeLink LIKE %:searchString%)")
    Page<Post> getAllBySearchStringAndActive(
            @Param("searchString")String searchString,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
