package org.example.semantic.application.communaute;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.example.semantic.tools.JenaEngine;

public class SELECT {

    private static final String NS = "http://www.semanticweb.org/daghnouj/ontologies/2024/9/untitled-ontology-19#";

    public static void main(String[] args) {
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            System.out.println("Modèle chargé avec succès.");

            addInstance(model, "CommunauteBienEtre", "Groupe_de_Soutien", "Bien-Être et Santé", "Groupe dédié au soutien mental et physique.");
            addInstance(model, "GroupeYoga", "Groupe_Intérêt", "Passion Yoga", "Groupe pour les amateurs de yoga.");
            addInstance(model, "Discussion1", "Fil_de_Discussion", "Conseils pour une meilleure santé mentale", null);
            addInstance(model, "Evenement1", "Événement_de_Partage", "Atelier de yoga pour débutants", null);

            executeQuery(model, "SELECT ?communaute WHERE { ?communaute rdf:type <" + NS + "Communauté> . }", "communaute");
            executeQuery(model, "SELECT ?discussion WHERE { ?discussion rdf:type <" + NS + "Discussion> . }", "discussion");
            executeQuery(model, "SELECT ?evenement WHERE { ?evenement rdf:type <" + NS + "Événement_de_Partage> . }", "evenement");

        } else {
            System.out.println("Erreur lors du chargement du modèle depuis l'ontologie.");
        }
    }

    private static void addInstance(Model model, String instanceName, String typeName, String nom, String description) {
        Resource instance = model.createResource(NS + instanceName);
        instance.addProperty(RDF.type, model.createResource(NS + typeName));

        Property nomProperty = model.createProperty(NS + "nom");
        Property descriptionProperty = model.createProperty(NS + "description");

        if (nom != null) {
            instance.addLiteral(nomProperty, nom);
        }

        if (description != null) {
            instance.addLiteral(descriptionProperty, description);
        }

        System.out.println("Instance ajoutée : " + instanceName);
    }

    private static void executeQuery(Model model, String queryString, String variableName) {
        String prefixedQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + queryString;
        try (QueryExecution qexec = QueryExecutionFactory.create(prefixedQueryString, model)) {
            ResultSet resultSet = qexec.execSelect();
            if (!resultSet.hasNext()) {
                System.out.println("Aucune instance trouvée pour la requête : " + queryString);
            } else {
                while (resultSet.hasNext()) {
                    RDFNode node = resultSet.next().get(variableName);
                    System.out.println("Instance de " + variableName + ": " + node);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution de la requête: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
