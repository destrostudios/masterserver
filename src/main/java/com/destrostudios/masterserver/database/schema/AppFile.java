package com.destrostudios.masterserver.database.schema;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class AppFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private App app;
    private String path;
    private long sizeBytes;
    private String checksumSha256;
    private LocalDateTime dateTime;

}
