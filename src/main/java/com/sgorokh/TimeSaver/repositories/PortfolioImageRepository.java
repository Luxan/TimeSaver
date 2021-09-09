package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.PortfolioImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioImageRepository extends JpaRepository<PortfolioImage, Long> {

}
