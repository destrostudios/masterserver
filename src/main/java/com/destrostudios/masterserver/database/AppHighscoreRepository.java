package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.AppHighscore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppHighscoreRepository extends JpaRepository<AppHighscore, Integer> {

    AppHighscore findByAppIdAndContextAndUserId(int appId, String context, int userId);
}
