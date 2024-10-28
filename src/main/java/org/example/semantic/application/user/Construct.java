package org.example.semantic.application.user;


import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.example.semantic.tools.JenaEngine;

public class Construct {
    public Construct() {
    }

    public static void main(String[] args) {
        String NS = "";

        // Load the OWL ontology model
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            // Retrieve the namespace URI
            NS = model.getNsPrefixURI("");

            // Load the inferred model from the rules file
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // SPARQL CONSTRUCT query to create new RDF triples based on the ontology
            String sparqlConstruct =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX ns: <" + NS + ">\n" +
                            "CONSTRUCT { ?utilisateur rdf:type ns:Gestion_des_Utilisateurs . } " +
                            "WHERE { ?utilisateur rdf:type ns:Utilisateur . }"; // Update based on specific query requirements

            // Execute the query against the inferred model
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlConstruct, inferredModel)) {
                Model resultModel = qexec.execConstruct(); // Construct the model

                // Output the result model in RDF/XML format
                resultModel.write(System.out, "RDF/XML");
            } catch (Exception e) {
                System.err.println("Error during query execution: " + e.getMessage());
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
