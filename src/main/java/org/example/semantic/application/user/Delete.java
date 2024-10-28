package org.example.semantic.application.user;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.example.semantic.tools.JenaEngine;

public class Delete {
    public Delete() {
    }

    public static void main(String[] args) {
        // Read the existing RDF model from the OWL file
        Model model = JenaEngine.readModel("data/ontologie.owl");
        if (model != null) {
            String NS = model.getNsPrefixURI("");

            // Define the SPARQL DELETE query to remove Alice's data
            String deleteQuery =
                    "PREFIX ns: <" + NS + ">\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +

                            "DELETE { \n" +
                            "   ns:Utilisateur1 ?p1 ?o1 ; \n" +
                            "      ns:nom_utilisateur ?nom ; \n" +
                            "      ns:email_utilisateur ?email ; \n" +
                            "      ns:date_inscription ?date . \n" +
                            "}\n" +
                            "WHERE { \n" +
                            "   ns:Utilisateur1 rdf:type ns:Utilisateur . \n" +
                            "   ns:Utilisateur1 ns:nom_utilisateur ?nom ; \n" +
                            "      ns:email_utilisateur \"alice@example.com\" ; \n" +
                            "      ns:date_inscription ?date . \n" +
                            "}";

            // Create the update request and execute it
            UpdateRequest updateRequest = UpdateFactory.create(deleteQuery);
            UpdateAction.execute(updateRequest, model);

            // Output the updated model to the console
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
