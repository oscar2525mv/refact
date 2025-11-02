package org.iut.refactoring;

import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

public class GestionPersonnel {
    
    public ArrayList<Employe> employes = new ArrayList<>();
    public HashMap<String, Double> salairesEmployes = new HashMap<>();

    private final Logger logger = new Logger();
    
    public void ajouteSalarie(String type, String nom, double salaireDeBase, int experience, String equipe) {
        Employe emp = new Employe(UUID.randomUUID().toString(), nom, type, salaireDeBase, experience, equipe);
        employes.add(emp);

        EmployeType empType = EmployeType.fromString(type);
        double salaireFinal = empType.calculerSalaire(salaireDeBase, experience);
        
        salairesEmployes.put(emp.id(), salaireFinal);
        logger.log("Ajout de l'employé: " + nom);
    }
    
    public double calculSalaire(String employeId) {
        Employe emp = getEmployeById(employeId);
        if (emp == null) {
            System.out.println("ERREUR: impossible de trouver l'employé");
            return 0;
        }
        EmployeType empType = EmployeType.fromString(emp.type());
        return empType.calculerSalaire(emp.salaireDeBase(), emp.experience());
    }


    public void generationRapport(String typeRapport, String filtre) {
        System.out.println("=== RAPPORT: " + typeRapport + " ===");
        switch (typeRapport) {
            case "SALAIRE" -> employes.stream()
                    .filter(e -> filtre == null || filtre.isEmpty() || e.equipe().equals(filtre))
                    .forEach(e -> {
                        String id =  e.id();
                        String nom =  e.nom();
                        double salaire = calculSalaire(id);
                        System.out.println(nom + ": " + salaire + " €");
                    });
            case "EXPERIENCE" -> employes.stream()
                    .filter(e -> filtre == null || filtre.isEmpty() || e.equipe().equals(filtre))
                    .forEach(e -> System.out.println(e.nom() + ": " + e.experience() + " années"));
            case "DIVISION" -> {
                Map<String, Long> divisions = employes.stream()
                        .collect(Collectors.groupingBy(e -> e.equipe(), Collectors.counting()));
                divisions.forEach((div, count) ->
                        System.out.println(div + ": " + count + " employés"));
            }
            default -> System.out.println("Type de rapport inconnu: " + typeRapport);
        }
        logger.log("Rapport généré: " + typeRapport);
    }

    public void avancementEmploye(String employeId, String newType) {
        java.util.concurrent.atomic.AtomicBoolean found = new java.util.concurrent.atomic.AtomicBoolean(false);

        employes = employes.stream()
                .map(e -> {
                    if (e.id().equals(employeId)) {
                        found.set(true);
                        return new Employe(e.id(), e.nom(), newType, e.salaireDeBase(), e.experience(), e.equipe());
                    }
                    return e;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        if (!found.get()) {
            System.out.println("ERREUR: impossible de trouver l'employé");
            return;
        }

        double nouveauSalaire = calculSalaire(employeId);
        salairesEmployes.put(employeId, nouveauSalaire);

        Employe emp = getEmployeById(employeId);
        logger.log("Employé promu: " + emp.nom() + " au poste de " + newType);
        System.out.println("Employé promu avec succès!");
    }
    
    public ArrayList<Employe> getEmployesParDivision(String division) {
        return employes.stream()
                .filter(e -> e.equipe().equals(division))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public void printLogs() {
        System.out.println("=== LOGS ===");
        logger.getLogs().forEach(System.out::println);
    }

    public double calculBonusAnnuel(String employeId) {
        Employe emp = getEmployeById(employeId);
        if (emp == null) return 0;
        EmployeType type = EmployeType.fromString(emp.type());
        return type.calculerBonus(emp.salaireDeBase(), emp.experience());
    }

    private Employe getEmployeById(String id) {
        return employes.stream()
                .filter(e -> e.id().equals(id))
                .findFirst()
                .orElse(null);
    }


}



