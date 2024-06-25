package com.telnet.analyse.communication;

import com.telnet.analyse.config.FeignConfig;
import com.telnet.analyse.models.Kpi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "projet-service", configuration = FeignConfig.class)
public interface KpiClient {

    @GetMapping("/api/kpi/getkpi/{id}")
    Kpi getKpiById(@PathVariable("id") Long id);

    // Ajoutez d'autres méthodes pour d'autres endpoints du microservice KPI si nécessaire
}
