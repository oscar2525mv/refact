package org.iut.refactoring;

public enum EmployeType {
    DEVELOPPEUR {
        @Override
        public double calculerSalaire(double base, int exp) {
            double salaire = base * 1.2;
            if (exp > 5) salaire *= 1.15;
            if (exp > 10) salaire *= 1.05;
            return salaire;
        }

        @Override
        public double calculerBonus(double base, int exp) {
            double bonus = base * 0.1;
            if (exp > 5) bonus *= 1.5;
            return bonus;
        }
    },
    CHEF_DE_PROJET {
        @Override
        public double calculerSalaire(double base, int exp) {
            double salaire = base * 1.5;
            if (exp > 3) salaire *= 1.1;
            return salaire + 5000;
        }

        @Override
        public double calculerBonus(double base, int exp) {
            double bonus = base * 0.2;
            if (exp > 3) bonus *= 1.3;
            return bonus;
        }
    },
    STAGIAIRE {
        @Override
        public double calculerSalaire(double base, int exp) {
            return base * 0.6;
        }

        @Override
        public double calculerBonus(double base, int exp) {
            return 0;
        }
    },
    INCONNU {
        @Override
        public double calculerSalaire(double base, int exp) {
            return base;
        }

        @Override
        public double calculerBonus(double base, int exp) {
            return 0;
        }
    };

    public abstract double calculerSalaire(double base, int exp);
    public abstract double calculerBonus(double base, int exp);

    public static EmployeType fromString(String type) {
        return switch (type.toUpperCase()) {
            case "DEVELOPPEUR" -> DEVELOPPEUR;
            case "CHEF DE PROJET" -> CHEF_DE_PROJET;
            case "STAGIAIRE" -> STAGIAIRE;
            default -> INCONNU;
        };
    }
}
