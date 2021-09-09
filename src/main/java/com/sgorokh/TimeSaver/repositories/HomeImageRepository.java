package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.HomeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeImageRepository extends JpaRepository<HomeImage, Long> {

}
