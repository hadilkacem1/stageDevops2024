package com.telnet.analyse.repository;

import com.telnet.analyse.models.Analyse;
import com.telnet.analyse.models.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyseRepository extends JpaRepository<Analyse,Long> {

    Optional<Analyse> findById(Long id);
    List<Analyse> findByProjectId(Long projectId);
    List<Analyse> findByKpi(Kpi kpi);
    long countByProjectId(Long projectId); // Ajoutez cette ligne


}
