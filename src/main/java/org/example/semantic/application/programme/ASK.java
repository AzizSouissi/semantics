package org.example.semantic.application.programme;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class ASK {
    public ASK() {
    }

    public static void main(String[] args) {
        // Namespace of the ontology
        String NS = "http://www.semanticweb.org/daghnouj/ontologies/2024/9/untitled-ontology-19#";
        String ontologyFile = "data/ontologie.owl";

        // Create an empty model
        Model model = ModelFactory.createDefaultModel();

        // Load the ontology file
        FileManager.get().addLocatorClassLoader(ASK.class.getClassLoader());
        try {
            FileManager.get().readModel(model, ontologyFile);
            System.out.println("Ontology loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading ontology: " + e.getMessage());
            return;
        }

        // Define the resources to check (align with your ontology's instances)
        String[] resourcesToCheck = {
                "Activité_Méditation",
                "Activité_Course",
                "Programme_Complet_Santé",
                "Programme_Spécialisé_Yoga"
        };

        // List the types of each resource
        for (String resource : resourcesToCheck) {
            String typeQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "SELECT ?type WHERE { <" + NS + resource + "> rdf:type ?type }";
            QueryExecution typeQueryExecution = QueryExecutionFactory.create(QueryFactory.create(typeQuery), model);

            try {
                ResultSet results = typeQueryExecution.execSelect();
                if (results.hasNext()) {
                    while (results.hasNext()) {
                        System.out.println("Type of " + resource + ": " + results.next().get("type"));
                    }
                } else {
                    System.out.println("No type found for " + resource);
                }
            } catch (Exception e) {
                System.err.println("Error executing SELECT query for types of " + resource + ": " + e.getMessage());
            } finally {
                typeQueryExecution.close();
            }
        }

        // Iterate through each resource and perform ASK queries
        for (String resource : resourcesToCheck) {
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "ASK { <" + NS + resource + "> rdf:type ?type }";
            QueryExecution queryExecution = QueryExecutionFactory.create(QueryFactory.create(queryString), model);

            try {
                boolean result = queryExecution.execAsk();
                System.out.println("ASK query result for " + resource + ": " + result);
            } catch (Exception e) {
                System.err.println("Error executing ASK query for " + resource + ": " + e.getMessage());
            } finally {
                queryExecution.close();
            }
        }
    }
}
