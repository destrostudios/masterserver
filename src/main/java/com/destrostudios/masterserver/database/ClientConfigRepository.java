package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.ClientConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientConfigRepository extends JpaRepository<ClientConfig, Integer> {

}
