package org.example.semantic.entities;

import java.util.List;

public class ProgrammeComplet extends ProgrammeDeBienEtre {
    private List<ActivitePhysique> activitesPhysiques;
    private List<ActiviteMentale> activitesMentales;

    // Getters et Setters
    public List<ActivitePhysique> getActivitesPhysiques() { return activitesPhysiques; }
    public void setActivitesPhysiques(List<ActivitePhysique> activitesPhysiques) { this.activitesPhysiques = activitesPhysiques; }
    public List<ActiviteMentale> getActivitesMentales() { return activitesMentales; }
    public void setActivitesMentales(List<ActiviteMentale> activitesMentales) { this.activitesMentales = activitesMentales; }
}

