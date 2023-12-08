package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    @Query(value = "SELECT salt_client FROM user WHERE login = :login", nativeQuery = true)
    Optional<String> findSaltClientByLogin(@Param("login") String login);
}
