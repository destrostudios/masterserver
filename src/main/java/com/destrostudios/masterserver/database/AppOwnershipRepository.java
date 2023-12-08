package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.AppOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppOwnershipRepository extends JpaRepository<AppOwnership, Integer> {

    List<AppOwnership> findByUserId(int userId);
}
