package org.example.semantic.application.programme;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.example.semantic.tools.JenaEngine;

public class SELECT {

    private static final String ONTOLOGY_PATH = "data/ontologie.owl";
    private static final String RULES_FILE = "data/rules.txt";
    private static final String NS = "http://www.semanticweb.org/daghnouj/ontologies/2024/9/untitled-ontology-19#";

    public static void main(String[] args) {
        // Charger le modèle de l'ontologie
        Model model = JenaEngine.readModel(ONTOLOGY_PATH);
        if (model == null) {
            System.out.println("Erreur lors du chargement du modèle depuis l'ontologie.");
            return;
        }
        System.out.println("Modèle chargé avec succès.");

        // Charger le modèle inféré
        Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, RULES_FILE);
        if (inferredModel == null) {
            System.out.println("Erreur lors du chargement du modèle inféré.");
            return;
        }

        // Ajouter des instances pour tester
        addInstance(inferredModel, "Activité_Méditation", "Activité_Mentale", "Séance de méditation guidée", "Activité de relaxation et de concentration.");
        addInstance(inferredModel, "Activité_Course", "Activité_Physique", "Course à pied de 5 km", "Activité de cardio.");

        addInstance(inferredModel, "Programme_Complet_Santé", "Programme_Complet", "Programme de bien-être complet", "Programme incluant des activités mentales et physiques.");
        addInstance(inferredModel, "Programme_Spécialisé_Yoga", "Programme_Spécialisé", "Programme de yoga spécialisé", "Programme centré sur la pratique du yoga.");

        // Exécuter des requêtes SPARQL pour récupérer les individus de la classe Programme_de_Bien-Être
        executeQuery(inferredModel, "SELECT ?programme WHERE { ?programme rdf:type <" + NS + "Programme_de_Bien-Être> . }", "programme");
    }

    // Méthode pour ajouter une instance dans le modèle
    private static void addInstance(Model model, String instanceName, String typeName, String nom, String description) {
        Resource instance = model.createResource(NS + instanceName);
        instance.addProperty(RDF.type, model.createResource(NS + typeName));

        Property nomProperty = model.createProperty(NS + "description");
        Property descriptionProperty = model.createProperty(NS + "description");

        if (nom != null) {
            instance.addLiteral(nomProperty, nom);
        }

        if (description != null) {
            instance.addLiteral(descriptionProperty, description);
        }

        System.out.println("Instance ajoutée : " + instanceName);
    }

    // Méthode pour exécuter une requête SPARQL
    private static void executeQuery(Model model, String queryString, String variableName) {
        String prefixedQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns: <" + NS + "> " +
                queryString;
        try (QueryExecution qexec = QueryExecutionFactory.create(prefixedQueryString, model)) {
            ResultSet results = qexec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Aucune instance trouvée pour la requête : " + queryString);
            } else {
                while (results.hasNext()) {
                    RDFNode node = results.next().get(variableName);
                    System.out.println("Instance de " + variableName + ": " + node);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de la requête SPARQL : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
