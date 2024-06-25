package com.telnet.analyse.communication;


import com.telnet.analyse.config.FeignConfig;
import com.telnet.analyse.models.Project;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "projet-service", configuration = FeignConfig.class)
public interface ProjectClient {

    @GetMapping("/api/projects/projetDetailsByKpi/{kpiId}")
    List<Project> getProjectsDetailsByKpiId(@PathVariable Long kpiId);


    @GetMapping("/api/projects/projectbyid/{id}") // Correction du chemin d'accès
    Project getProjetById(@PathVariable("id") Long id); // Correction du nom de la variable

    // Ajoutez d'autres méthodes pour les autres endpoints du microservice Projet si nécessaire
}
