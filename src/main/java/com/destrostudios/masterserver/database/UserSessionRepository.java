package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {

}
