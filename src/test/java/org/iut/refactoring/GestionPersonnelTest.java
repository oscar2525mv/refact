package org.iut.refactoring;


import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests de la classe GestionPersonnel")
public class GestionPersonnelTest {

    private GestionPersonnel gestion;

    @BeforeEach
    void setUp() {
        gestion = new GestionPersonnel();
    }

    @Test
    @DisplayName("Ajout d’un employé développeur calcule correctement le salaire initial")
    void testAjoutDeveloppeur() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");

        assertThat(gestion.employes).hasSize(1);
        var emp = gestion.employes.get(0);
        assertThat(emp[2]).isEqualTo("Alice");

        String id = (String) emp[0];
        Double salaire = gestion.salairesEmployes.get(id);
        // salaireDeBase * 1.2 * 1.15 = 69000
        assertThat(salaire).isCloseTo(69000.0, within(0.1));
    }

    @Test
    @DisplayName("Calcul du salaire d’un chef de projet expérimenté")
    void testCalculSalaireChefProjet() {
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");
        var id = (String) gestion.employes.get(0)[0];

        double salaire = gestion.calculSalaire(id);
        // salaireDeBase * 1.5 * 1.1 + 5000 = 99000 + 5000 = 104000
        assertEquals(104000.0, salaire, 0.1);
    }

    @Test
    @DisplayName("Calcul du salaire d’un chef de projet pas expérimenté")
    void testCalculSalaireChefProjetPasExperimente() {
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 1, "RH");
        var id = (String) gestion.employes.get(0)[0];

        double salaire = gestion.calculSalaire(id);
        assertEquals(95000.0, salaire, 0.1);
    }



    @Test
    @DisplayName("Calcul du salaire d’un stagiaire")
    void testCalculSalaireStagiaire() {
        gestion.ajouteSalarie("STAGIAIRE", "Charlie", 20000, 0, "IT");
        var id = (String) gestion.employes.get(0)[0];

        double salaire = gestion.calculSalaire(id);
        assertThat(salaire).isEqualTo(20000 * 0.6);
    }

    @Test
    @DisplayName("Calcul du salaire d’un développeur très expérimenté (bonus 10 ans)")
    void testCalculSalaireDeveloppeurSenior() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Dan", 55000, 12, "IT");
        var id = (String) gestion.employes.get(0)[0];

        double salaire = gestion.calculSalaire(id);
        // 55000 * 1.2 * 1.15 * 1.05 = 79695
        assertThat(salaire).isCloseTo(79695, within(0.1));
    }

    @Test
    @DisplayName("Avancement d’un employé met à jour le type et le salaire")
    void testAvancementEmploye() {
        gestion.ajouteSalarie("STAGIAIRE", "Emma", 30000, 2, "IT");
        var id = (String) gestion.employes.get(0)[0];
        gestion.avancementEmploye(id, "DEVELOPPEUR");

        var emp = gestion.employes.get(0);
        assertThat(emp[1]).isEqualTo("DEVELOPPEUR");

        Double salaire = gestion.salairesEmployes.get(id);
        assertThat(salaire).isGreaterThan(30000.0);
    }

    @Test
    @DisplayName("Avancement d’un employé avec id incorrect met à jour le type et le salaire")
    void testAvancementEmployeIncorrect() {
        gestion.ajouteSalarie("STAGIAIRE", "Emma", 30000, 2, "IT");
        gestion.avancementEmploye("id_incorrect", "DEVELOPPEUR");

        // Aucun changement attendu
        var emp = gestion.employes.get(0);
        assertThat(emp[1]).isEqualTo("STAGIAIRE");
    }

    @Test
    @DisplayName("Recherche des employés par division")
    void testGetEmployesParDivision() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");
        gestion.ajouteSalarie("STAGIAIRE", "Charlie", 20000, 0, "IT");

        ArrayList<Object[]> itTeam = gestion.getEmployesParDivision("IT");
        assertThat(itTeam).hasSize(2);
        assertThat(itTeam).extracting(e -> e[2]).containsExactlyInAnyOrder("Alice", "Charlie");
    }

    @Test
    @DisplayName("Calcul du bonus annuel pour développeur expérimenté")
    void testBonusDeveloppeur() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        String id = (String) gestion.employes.get(0)[0];

        double bonus = gestion.calculBonusAnnuel(id);
        // base * 0.1 * 1.5 = 7500
        assertThat(bonus).isEqualTo(7500.0);
    }

    @Test
    @DisplayName("Calcul du bonus annuel pour développeur pas expérimenté")
    void testBonusDeveloppeurPasExperimente() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 1, "IT");
        String id = (String) gestion.employes.get(0)[0];

        double bonus = gestion.calculBonusAnnuel(id);
        // base * 0.1 * 1.5 = 7500
        assertThat(bonus).isEqualTo(5000.0);
    }

    @Test
    @DisplayName("Calcul du bonus annuel pour chef de projet")
    void testBonusChefDeProjet() {
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");
        String id = (String) gestion.employes.get(0)[0];

        double bonus = gestion.calculBonusAnnuel(id);
        // base * 0.2 * 1.3 = 15600
        assertThat(bonus).isEqualTo(15600.0);
    }

    @Test
    @DisplayName("Calcul du bonus annuel pour stagiaire (doit être nul)")
    void testBonusStagiaire() {
        gestion.ajouteSalarie("STAGIAIRE", "Charlie", 20000, 0, "IT");
        String id = (String) gestion.employes.get(0)[0];

        double bonus = gestion.calculBonusAnnuel(id);
        assertThat(bonus).isZero();
    }



    @Test
    @DisplayName("Bonus d’un chef de projet non expérimenté (≤3 ans)")
    void testBonusChefProjetSansExperience() {
        gestion.ajouteSalarie("CHEF DE PROJET", "Paul", 50000, 3, "RH");
        var id = (String) gestion.employes.get(0)[0];
        double bonus = gestion.calculBonusAnnuel(id);
        // salaireDeBase * 0.2 = 10000
        assertThat(bonus).isEqualTo(10000.0);
    }

    @Test
    @DisplayName("Bonus d’un type inconnu (aucune règle spéciale)")
    void testBonusTypeInconnu() {
        gestion.ajouteSalarie("INCONNU", "Mystère", 40000, 5, "IT");
        var id = (String) gestion.employes.get(0)[0];
        double bonus = gestion.calculBonusAnnuel(id);
        // Pas de bonus : bonus = 0
        assertThat(bonus).isZero();
    }

    @Test
    @DisplayName("Bonus employé inexistant renvoie 0")
    void testBonusEmployeInexistant() {
        gestion.ajouteSalarie("CHEF DE PROJET", "Paul", 50000, 3, "RH");
        double bonus = gestion.calculBonusAnnuel("id-inexistant");
        assertThat(bonus).isZero();
    }


    @Test
    @DisplayName("Calcul salaire - employé inexistant affiche erreur et renvoie 0")
    void testCalculSalaireEmployeInexistant() {
        double salaire = gestion.calculSalaire("id-inexistant");
        assertThat(salaire).isZero();
    }

    @Test
    @DisplayName("Calcul salaire - type inconnu (aucune règle spéciale)")
    void testCalculSalaireTypeInconnu() {
        gestion.ajouteSalarie("INCONNU", "Mystère", 40000, 5, "IT");
        var id = (String) gestion.employes.get(0)[0];
        double salaire = gestion.calculSalaire(id);
        // Pas de modification : salaireFinal = salaireDeBase
        assertThat(salaire).isEqualTo(40000.0);
    }

    // === getEmployesParDivision ===
    @Test
    @DisplayName("Retourne liste vide si aucune correspondance de division")
    void testGetEmployesParDivisionVide() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        ArrayList<Object[]> result = gestion.getEmployesParDivision("RH");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Retourne plusieurs employés pour une division")
    void testGetEmployesParDivisionPlusieurs() {
        gestion.ajouteSalarie("DEVELOPPEUR", "A", 40000, 3, "IT");
        gestion.ajouteSalarie("STAGIAIRE", "B", 20000, 0, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "C", 60000, 5, "RH");

        ArrayList<Object[]> itTeam = gestion.getEmployesParDivision("IT");
        assertThat(itTeam).hasSize(2);
        assertThat(itTeam).extracting(e -> e[2]).contains("A", "B");
    }


    @Test
    @DisplayName("Rapport EXPERIENCE affiche nom et années d’expérience")
    void testGenerationRapportExperience() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("STAGIAIRE", "Bob", 20000, 1, "RH");

        gestion.generationRapport("EXPERIENCE", "IT");
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: EXPERIENCE"));
    }

    @Test
    @DisplayName("Rapport EXPERIENCE affiche nom et années d’expérience sans filtre")
    void testGenerationRapportExperienceSansFiltre() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("STAGIAIRE", "Bob", 20000, 1, "RH");

        gestion.generationRapport("EXPERIENCE", "");
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: EXPERIENCE"));
    }

    @Test
    @DisplayName("Rapport EXPERIENCE affiche nom et années d’expérience avec filtre null")
    void testGenerationRapportExperienceFiltreNull() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("STAGIAIRE", "Bob", 20000, 1, "IT");

        gestion.generationRapport("EXPERIENCE", null);
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: EXPERIENCE"));
    }

    @Test
    @DisplayName("Rapport DIVISION affiche le nombre d’employés par division")
    void testGenerationRapportDivision() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");
        gestion.ajouteSalarie("STAGIAIRE", "Charlie", 20000, 0, "IT");

        gestion.generationRapport("DIVISION", null);
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: DIVISION"));
    }

    @Test
    @DisplayName("Rapport SALAIRE affiche le nombre d’employés par division")
    void testGenerationRapportSalaire() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");

        gestion.generationRapport("SALAIRE", "RH" );
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: SALAIRE"));
    }

    @Test
    @DisplayName("Rapport SALAIRE affiche le nombre d’employés par division sans filtre")
    void testGenerationRapportSalaireSansFiltre() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");

        gestion.generationRapport("SALAIRE", "" );
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: SALAIRE"));
    }

    @Test
    @DisplayName("Rapport SALAIRE affiche le nombre d’employés par division avec filtre null")
    void testGenerationRapportSalaireFiltreNull() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");

        gestion.generationRapport("SALAIRE", null );
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: SALAIRE"));
    }

    @Test
    @DisplayName("Rapport avec type de rapport inconnu n’échoue pas")
    void testGenerationRapportTypeIncorrect() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");

        gestion.generationRapport("INCORRECT", null );
        assertThat(gestion.logs).anyMatch(s -> s.contains("Rapport généré: INCORRECT"));
    }



    @Test
    @DisplayName("Affiche tous les logs correctement")
    void afficheTousLesLogsCorrectement() {
        gestion.ajouteSalarie("DEVELOPPEUR", "Alice", 50000, 6, "IT");
        gestion.ajouteSalarie("CHEF DE PROJET", "Bob", 60000, 4, "RH");
        gestion.printLogs();

        // Aucun assert spécifique ici, juste vérifier qu'aucune exception n'est levée
    }
}
