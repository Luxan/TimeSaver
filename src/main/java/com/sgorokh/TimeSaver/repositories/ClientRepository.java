package com.sgorokh.TimeSaver.repositories;

import com.sgorokh.TimeSaver.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c from Client c WHERE c.name LIKE %:searchString% OR c.email LIKE %:searchString%")
    List<Client> searchByString(String searchString);

    Client searchByName(String name);
}
