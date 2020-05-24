package com.destrostudios.masterserver.database;

import com.destrostudios.masterserver.database.schema.AppOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppOwnershipRepository extends JpaRepository<AppOwnership, Integer> {

}
