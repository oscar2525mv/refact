package org.iut.refactoring;

public record Employe(
        String id,
        String nom,
        String type,
        double salaireDeBase,
        int experience,
        String equipe
){}

