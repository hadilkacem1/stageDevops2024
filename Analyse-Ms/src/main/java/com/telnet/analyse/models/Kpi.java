package com.telnet.analyse.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telnet.analyse.models.Analyse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "kpis")
public class Kpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String name;


    private Integer objectif;

    @Enumerated(EnumType.STRING)
    private FrequenceKPI frequence;



    @OneToMany(mappedBy = "kpi", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Analyse> analyses = new ArrayList<>();  // Ajoutez cette ligne pour la relation avec Analyse


    public Kpi() {
    }


    @JsonCreator
    public Kpi(@JsonProperty("name") String name) {
        this.name = name;
    }




    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public FrequenceKPI getFrequence() {
        return frequence;
    }

    public void setFrequence(FrequenceKPI frequence) {
        this.frequence = frequence;
    }

    public Integer getObjectif() {
        return objectif;
    }

    public void setObjectif(Integer objectif) {
        this.objectif = objectif;
    }

    public List<Analyse> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<Analyse> analyses) {
        this.analyses = analyses;
    }


}
