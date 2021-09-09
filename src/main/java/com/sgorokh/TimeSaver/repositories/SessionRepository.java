package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

}
