package com.telnet.analyse.service;


import com.telnet.analyse.models.Analyse;
import com.telnet.analyse.models.Kpi;
import com.telnet.analyse.models.Cause;
import com.telnet.analyse.models.Project;
import com.telnet.analyse.communication.KpiClient;
import com.telnet.analyse.communication.ProjectClient;
import com.telnet.analyse.repository.AnalyseRepository;
import org.springframework.stereotype.Service;
import com.telnet.analyse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class AnalyseService {

    @Autowired
    private AnalyseRepository analyseRepository;

    @Autowired
    private ProjectClient projectClient;

    @Autowired
    private KpiClient kpiClient;

    public List<Analyse> getAnalysesForProject(Long projectId) {
        return analyseRepository.findByProjectId(projectId);
    }

    public List<Analyse> getAnalysesForKpi(Long kpiId) {
        Kpi kpi = kpiClient.getKpiById(kpiId);
        if (kpi != null) {
            return analyseRepository.findByKpi(kpi);
        } else {
            throw new EntityNotFoundException("KPI not found");
        }
    }

    public Analyse ajouterAnalyseCausale(Long projectId, Analyse analyseRequest) {
        Project project = projectClient.getProjetById(projectId);
        if (project == null) {
            throw new EntityNotFoundException("Project not found with ID: " + projectId);
        }
        Analyse analyse = new Analyse(project, analyseRequest.getTypeProbleme(),
                analyseRequest.getIdentificationProbleme(),
                analyseRequest.getMethodeUtilisee());
        analyse.setDate(new Date());
        List<Cause> causes = analyseRequest.getCauses();
        for (Cause cause : causes) {
            cause.setAnalyse(analyse);
        }
        analyse.setCauses(causes);
        return analyseRepository.save(analyse);
    }


    public List<Cause> getCausesForAnalyse(Long analyseId) {
        Analyse analyse = analyseRepository.findById(analyseId)
                .orElseThrow(() -> new EntityNotFoundException("Analyse not found"));
        return analyse.getCauses();
    }

    public Analyse getAnalyseById(Long analyseId) {
        return analyseRepository.findById(analyseId)
                .orElse(null);
    }

    public Analyse modifierAnalyseCausale(Long analyseId, Analyse analyseRequest) {
        Analyse analyse = analyseRepository.findById(analyseId)
                .orElseThrow(() -> new EntityNotFoundException("Analyse not found"));
        analyse.setTypeProbleme(analyseRequest.getTypeProbleme());
        analyse.setIdentificationProbleme(analyseRequest.getIdentificationProbleme());
        analyse.setMethodeUtilisee(analyseRequest.getMethodeUtilisee());
        analyse.setDate(new Date());
        List<Cause> causes = analyseRequest.getCauses();
        for (Cause cause : causes) {
            cause.setAnalyse(analyse);
        }
        analyse.setCauses(causes);
        return analyseRepository.save(analyse);
    }

    public void deleteAnalyseCausale(Long analyseId) {
        if (!analyseRepository.existsById(analyseId)) {
            throw new EntityNotFoundException("Analyse not found");
        }
        analyseRepository.deleteById(analyseId);
    }

    public Analyse ajouterAnalyseByKpiId(Long kpiId, Analyse analyseRequest) {
        Kpi kpi = kpiClient.getKpiById(kpiId);
        if (kpi != null) {
            Analyse analyse = new Analyse();
            analyse.setProject(null);
            analyse.setTypeProbleme(analyseRequest.getTypeProbleme());
            analyse.setIdentificationProbleme(analyseRequest.getIdentificationProbleme());
            analyse.setMethodeUtilisee(analyseRequest.getMethodeUtilisee());
            analyse.setDate(new Date());
            analyse.setKpi(kpi);
            List<Cause> causes = analyseRequest.getCauses();
            for (Cause cause : causes) {
                cause.setAnalyse(analyse);
            }
            analyse.setCauses(causes);
            return analyseRepository.save(analyse);
        } else {
            throw new EntityNotFoundException("KPI not found");
        }
    }


    public AnalyseService(AnalyseRepository analyseRepository) {
        this.analyseRepository = analyseRepository;
    }

    public void saveAnalyse(Analyse analyse) {
        analyseRepository.save(analyse);
    }

    public List<Analyse> getAllAnalyses() {
        return analyseRepository.findAll();
    }
}
