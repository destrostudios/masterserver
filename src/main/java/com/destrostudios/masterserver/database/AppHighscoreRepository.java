package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.AppHighscore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppHighscoreRepository extends JpaRepository<AppHighscore, Integer> {

    List<AppHighscore> findByAppId(int appId);

    List<AppHighscore> findByAppIdAndContext(int appId, String context);

    AppHighscore findByAppIdAndContextAndUserId(int appId, String context, int userId);
}
