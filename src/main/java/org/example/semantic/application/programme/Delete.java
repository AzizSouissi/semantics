package org.example.semantic.application.programme;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.example.semantic.tools.JenaEngine;

public class Delete {
    public Delete() {
    }

    public static void main(String[] args) {
        String NS = "";
        String resourceToDelete = "Programme_Complet_Santé"; // Specify the resource to delete

        // Read the existing RDF model from the OWL file
        Model model = JenaEngine.readModel("data/ontologie.owl");
        if (model != null) {
            NS = model.getNsPrefixURI("");

            // Define the delete operation for the specified resource
            String deleteOperation =
                    "PREFIX ns: <" + NS + ">\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +

                            "DELETE { \n" +
                            "   ns:" + resourceToDelete + " ?p ?o . \n" + // Delete all triples of this resource
                            "}\n" +
                            "WHERE { \n" +
                            "   ns:" + resourceToDelete + " ?p ?o . \n" + // Check that the resource exists
                            "}";

            // Create the update request and execute it
            UpdateRequest updateRequest = UpdateFactory.create(deleteOperation);
            UpdateAction.execute(updateRequest, model);

            // Output the updated model to the console
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
