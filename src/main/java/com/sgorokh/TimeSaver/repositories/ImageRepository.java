package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query( "select i from Image i where i.id in :ids")
    List<Image> getByIds(@Param("ids") List<Long> imageIds);

}