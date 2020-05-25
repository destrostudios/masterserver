package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.App;
import com.destrostudios.masterserver.database.schema.AppFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppFileRepository extends JpaRepository<AppFile, Integer> {

    List<AppFile> findByApp(App app);

}
