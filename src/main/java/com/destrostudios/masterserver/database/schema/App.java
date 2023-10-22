package com.destrostudios.masterserver.database.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToOne
    private Developer developer;
    @ManyToMany
    private List<Genre> genres;
    private LocalDate date;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String executable;
    private boolean hidden;

}
