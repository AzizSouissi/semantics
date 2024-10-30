package org.example.semantic.entities;

import java.util.List;

public class ProgrammeDeBienEtre {
    private String nom;
    private String frequence; // e.g., "Hebdomadaire", "Mensuel"
    private String description;
    private List<Activite> comprend; // List of activities included

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getFrequence() { return frequence; }
    public void setFrequence(String frequence) { this.frequence = frequence; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Activite> getComprend() { return comprend; }
    public void setComprend(List<Activite> comprend) { this.comprend = comprend; }
}

