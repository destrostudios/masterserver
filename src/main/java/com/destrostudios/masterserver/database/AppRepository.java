package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {

}
