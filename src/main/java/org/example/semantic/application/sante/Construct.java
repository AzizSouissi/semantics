package org.example.semantic.application.sante;

import org.example.semantic.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public class Construct {
    public static void main(String[] args) {
        // Define the namespace of your ontology
        String NS = "";

        // Read the model from an ontology
        Model model = JenaEngine.readModel("data/ontologie.owl"); // Ensure the path is correct

        if (model != null) {
            NS = model.getNsPrefixURI("");
            // Apply rules on the inferred model
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Create a SPARQL CONSTRUCT query
            String sparqlConstruct = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX ns: <" + NS + ">\n" +
                    "CONSTRUCT {" +
                    "  ?indicateur rdf:type ns:Indicateur_de_Santé ." + // Adapt according to your class
                    "} " +
                    "WHERE {" +
                    "  ?indicateur rdf:type ns:Indicateur_de_Santé ." + // Adapt according to your class
                    "}";

            // Execute the SPARQL CONSTRUCT query on the inferred model
            QueryExecution qexec = QueryExecutionFactory.create(sparqlConstruct, inferredModel);
            Model resultModel = qexec.execConstruct();

            // Display the resulting model or perform additional processing
            resultModel.write(System.out, "RDF/XML"); // Displaying in RDF/XML format

            // Don't forget to close the QueryExecution to free resources
            qexec.close();
        } else {
            System.out.println("Error reading the model from the ontology.");
        }
    }
}
