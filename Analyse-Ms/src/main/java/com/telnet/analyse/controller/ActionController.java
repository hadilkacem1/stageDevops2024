package com.telnet.analyse.controller;


import com.telnet.analyse.models.*;
import com.telnet.analyse.repository.ActionRepository;
import com.telnet.analyse.repository.AnalyseRepository;
import com.telnet.analyse.repository.CauseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController

@RequestMapping("/api/action")
@CrossOrigin("*")
public class ActionController {

    @Autowired
    AnalyseRepository analyseRepository;
    @Autowired
    CauseRepository causeRepository;

    @Autowired
    ActionRepository actionRepository;

    @PostMapping("/planifier/{causeId}")
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLEQUALITE') or hasAuthority('ROLE_CHEFDEPROJET')")
    public ResponseEntity<?> planifierAction(@PathVariable Long causeId, @RequestBody Action action) {
        try {
            Optional<Cause> optionalCause = causeRepository.findById(causeId);

            if (!optionalCause.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Cause non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Cause cause = optionalCause.get();
            Analyse analyse = cause.getAnalyse();

            Action newAction = new Action(
                    action.getTypeAction(), // Assurez-vous que le champ "type" est initialisé avec un TypeAction
                    action.getResponsable(),
                    action.getDatePlanification(),
                    action.getDateRealisation(),
                    action.getCritereEfficacite(),
                    action.isEfficace(),
                    action.getCommentaire()
            );

            newAction.setCause(cause);

            // Sauvegarder l'action dans la base de données en utilisant le repository correspondant
            actionRepository.save(newAction);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Action planifiée avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erreur lors de la planification de l'action: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/details/{actionId}")
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLEQUALITE') or hasAuthority('ROLE_CHEFDEPROJET')")

    public ResponseEntity<?> getActionById(@PathVariable Long actionId) {
        try {
            Optional<Action> optionalAction = actionRepository.findById(actionId);

            if (!optionalAction.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Action non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Action action = optionalAction.get();
            // Vous pouvez ajouter d'autres opérations si nécessaire

            return ResponseEntity.ok(action);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération de l'action: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/cause/{causeId}/actions")
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLEQUALITE') or hasAuthority('ROLE_CHEFDEPROJET')")


    public ResponseEntity<?> getActionsByCauseId(@PathVariable Long causeId) {
        try {
            Optional<Cause> optionalCause = causeRepository.findById(causeId);

            if (!optionalCause.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Cause non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Cause cause = optionalCause.get();
            List<Action> actions = cause.getActions();

            if (actions.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Aucune action associée à cette cause");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }

            return ResponseEntity.status(HttpStatus.OK).body(actions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erreur lors de la récupération des actions pour cette cause: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/modifier/{actionId}")
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLEQUALITE') or hasAuthority('ROLE_CHEFDEPROJET')")
    public ResponseEntity<?> modifierAction(@PathVariable Long actionId, @RequestBody Action actionDetails) {
        try {
            Optional<Action> optionalAction = actionRepository.findById(actionId);

            if (!optionalAction.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Action non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Action action = optionalAction.get();
            action.setTypeAction(actionDetails.getTypeAction()); // Mise à jour des détails de l'action
            action.setResponsable(actionDetails.getResponsable());
            action.setDatePlanification(actionDetails.getDatePlanification());
            action.setDateRealisation(actionDetails.getDateRealisation());
            action.setCritereEfficacite(actionDetails.getCritereEfficacite());
            action.setEfficace(actionDetails.isEfficace());
            action.setCommentaire(actionDetails.getCommentaire());

            // Sauvegarde de l'action mise à jour dans la base de données
            actionRepository.save(action);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Action modifiée avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erreur lors de la modification de l'action: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/supprimer/{actionId}")
    @PreAuthorize("hasAuthority('ROLE_RESPONSABLEQUALITE') or hasAuthority('ROLE_CHEFDEPROJET')")
    public ResponseEntity<?> supprimerAction(@PathVariable Long actionId) {
        try {
            Optional<Action> optionalAction = actionRepository.findById(actionId);

            if (!optionalAction.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Action non trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Action action = optionalAction.get();

            // Suppression de l'action de la base de données
            actionRepository.delete(action);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Action supprimée avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erreur lors de la suppression de l'action: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
