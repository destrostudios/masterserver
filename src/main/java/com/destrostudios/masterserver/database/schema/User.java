package com.destrostudios.masterserver.database.schema;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String email;
    private String emailSecret;
    private boolean emailConfirmed;
    private LocalDateTime lastRequestedEmailDate;
    private String saltClient;
    private String saltServer;
    private String hashedPassword;
    @OneToMany(mappedBy = "user")
    private List<AppOwnership> appOwnerships;

}
