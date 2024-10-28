package org.example.semantic.application.sante;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.example.semantic.tools.JenaEngine;

public class ASK {

    public static void main(String[] args) {
        String NS = "";

        // Load the model from the ontology file
        Model model = JenaEngine.readModel("data/ontologie.owl"); // Ensure the path is correct

        if (model != null) {
            // Read the namespace of the ontology
            NS = model.getNsPrefixURI("");

            // Apply rules on the model to create the inferred model
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Check if the inferred model is created successfully
            if (inferredModel != null) {
                // Create a SPARQL ASK query
                String sparqlAsk = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX ns: <" + NS + ">\n" +
                        "ASK WHERE {" +
                        "  ?x rdf:type ns:Indicateur_de_Santé ." + // Check if there are instances of Indicateur_de_Santé
                        "}";

                // Execute the SPARQL ASK query on the inferred model
                try (QueryExecution qexec = QueryExecutionFactory.create(sparqlAsk, inferredModel)) {
                    boolean result = qexec.execAsk();
                    System.out.println("Is there any instance of Indicateur_de_Santé? " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error: Inferred model is null.");
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
