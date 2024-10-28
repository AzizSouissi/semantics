package org.example.semantic.application.communaute;


import org.example.semantic.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

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
                    "  ?communaute rdf:type ns:Communauté ;" +
                    "              ns:nom ?nomCommunaute ;" +
                    "              ns:description ?descriptionCommunaute ;" +
                    "              ns:organise ?evenement ." +
                    "  ?evenement rdf:type ns:Événement_de_Partage ;" +
                    "             ns:sujet ?sujetEvenement ." +
                    "  ?discussion rdf:type ns:Fil_de_Discussion ;" +
                    "              ns:sujet ?sujetDiscussion ." +
                    "  ?utilisateur ns:participeÀ ?discussion ." +
                    "} " +
                    "WHERE {" +
                    "  ?communaute rdf:type ns:Communauté ." +
                    "  ?communaute ns:nom ?nomCommunaute ." +
                    "  ?communaute ns:description ?descriptionCommunaute ." +
                    "  ?communaute ns:organise ?evenement ." +
                    "  ?evenement rdf:type ns:Événement_de_Partage ." +
                    "  ?evenement ns:sujet ?sujetEvenement ." +
                    "  ?discussion rdf:type ns:Fil_de_Discussion ." +
                    "  ?discussion ns:sujet ?sujetDiscussion ." +
                    "  ?utilisateur ns:participeÀ ?discussion ." +
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

