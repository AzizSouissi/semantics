package org.example.semantic.application.programme;

import org.apache.jena.rdf.model.Model;
import org.example.semantic.tools.JenaEngine;

public class Main {

    public static void main(String[] args) {
        // Set namespace according to the ontology URI
        String NS = "http://www.semanticweb.org/daghnouj/ontologies/2024/9/untitled-ontology-19#";

        // Load the model from the ontology file
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            System.out.println("Ontology model loaded successfully.");

            // Apply reasoning rules from the rules file to create inferred model
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Check if the inference was successful
            if (inferredModel != null) {
                System.out.println("Inferred model created with reasoning rules.");

                // Execute SPARQL queries from the query file
                String queryResult = JenaEngine.executeQueryFile(inferredModel, "data/query.txt");
                System.out.println("Query results:\n" + queryResult);
            } else {
                System.out.println("Error applying inference rules to the model.");
            }
        } else {
            System.out.println("Error reading model from ontology file.");
        }
    }
}
