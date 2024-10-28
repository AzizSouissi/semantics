package org.example.semantic.application.sante;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.example.semantic.tools.JenaEngine;

public class SELECT {

    private static final String NS = "http://www.semanticweb.org/daghnouj/ontologies/2024/9/untitled-ontology-19#";

    public static void main(String[] args) {
        // Charger le modèle à partir du fichier RDF
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            System.out.println("Modèle chargé avec succès.");

            // Ajouter des instances dynamiques
            addInstance(model, "Indicateur_Stress", "Indicateur_Mental", "Élevé", 5.8f);
            addInstance(model, "Rapport_Stress", "Rapport_Mental", "Rapport sur le stress élevé", null);
            addInstance(model, "Indicateur_Anxiété", "Indicateur_Mental", "Moyen", 4.2f);
            addInstance(model, "Rapport_Anxiété", "Rapport_Mental", "Rapport sur l'anxiété moyenne", null);

            // Requêtes SPARQL pour obtenir les instances
            String queryMentalIndicator = String.format("SELECT ?mentalIndicator WHERE { ?mentalIndicator rdf:type <%sIndicateur_Mental> . }", NS);
            executeQuery(model, queryMentalIndicator, "mentalIndicator");

            String queryMentalReport = String.format("SELECT ?mentalReport WHERE { ?mentalReport rdf:type <%sRapport_Mental> . }", NS);
            executeQuery(model, queryMentalReport, "mentalReport");

        } else {
            System.out.println("Erreur lors du chargement du modèle depuis l'ontologie.");
        }
    }

    // Méthode pour ajouter une instance
    private static void addInstance(Model model, String instanceName, String typeName, String niveau, Float score) {
        Resource instance = model.createResource(NS + instanceName);
        instance.addProperty(RDF.type, model.createResource(NS + typeName));

        if (niveau != null) {
            instance.addLiteral(model.createProperty(NS + "niveau"), niveau);
        }

        if (score != null) {
            instance.addLiteral(model.createProperty(NS + "score"), score);
        }

        System.out.println("Instance ajoutée : " + instanceName);
    }

    // Méthode pour exécuter la requête SPARQL et afficher les résultats
// Méthode pour exécuter la requête SPARQL et afficher les résultats
    private static void executeQuery(Model model, String queryString, String variableName) {
        // Ajouter le préfixe rdf à la requête
        String prefixedQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + queryString;

        Query query = QueryFactory.create(prefixedQueryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
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
            System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
