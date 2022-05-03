package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.database.schema.AppFileProtection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppFileProtectionRepository extends JpaRepository<AppFileProtection, Integer> {

    List<AppFileProtection> findByApp(App app);

}
