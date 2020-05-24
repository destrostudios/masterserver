package com.destrostudios.masterserver.database.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String login;
    private String saltClient;
    private String saltServer;
    private String hashedPassword;
    @OneToMany(mappedBy = "user")
    private List<AppOwnership> appOwnerships;

}
