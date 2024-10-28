package org.example.semantic.application.user;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.example.semantic.tools.JenaEngine;

public class Create {
    public Create() {
    }

    public static void main(String[] args) {
        String NS = "";
        // Read the existing RDF model from the OWL file
        Model model = JenaEngine.readModel("data/ontologie.owl");
        if (model != null) {
            NS = model.getNsPrefixURI("");

            // Define new RDF properties and classes to insert for Gestion_des_Utilisateurs
            String newProp =
                    "PREFIX ns: <" + NS + ">\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +

                            // Inserting new classes
                            "INSERT DATA { \n" +
                            "   ns:Gestion_des_Utilisateurs rdf:type owl:Class . \n" +
                            "   ns:Utilisateur rdf:type owl:Class ; \n" +
                            "      rdfs:subClassOf ns:Gestion_des_Utilisateurs . \n" +

                            // Inserting new datatype properties
                            "   ns:nom_utilisateur rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Utilisateur ; \n" +
                            "      rdfs:range xsd:string . \n" +
                            "   ns:email_utilisateur rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Utilisateur ; \n" +
                            "      rdfs:range xsd:string . \n" +
                            "   ns:date_inscription rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Utilisateur ; \n" +
                            "      rdfs:range xsd:date . \n" +

                            // Inserting new object properties
                            "   ns:gère_utilisateur rdf:type owl:ObjectProperty ; \n" +
                            "      rdfs:domain ns:Admin ; \n" +
                            "      rdfs:range ns:Utilisateur . \n" +

                            // Inserting new instances
                            "   ns:Utilisateur1 rdf:type ns:Utilisateur ; \n" +
                            "      ns:nom_utilisateur \"Alice\" ; \n" +
                            "      ns:email_utilisateur \"alice@example.com\" ; \n" +
                            "      ns:date_inscription \"2024-01-01\"^^xsd:date . \n" +
                            "   ns:Utilisateur2 rdf:type ns:Utilisateur ; \n" +
                            "      ns:nom_utilisateur \"Bob\" ; \n" +
                            "      ns:email_utilisateur \"bob@example.com\" ; \n" +
                            "      ns:date_inscription \"2024-01-15\"^^xsd:date . \n" +
                            "   ns:Admin_John rdf:type ns:Admin ; \n" +
                            "      ns:gère_utilisateur ns:Utilisateur1 ; \n" +
                            "      ns:gère_utilisateur ns:Utilisateur2 . \n" +
                            "}";

            // Create the update request and execute it
            UpdateRequest updateRequest = UpdateFactory.create(newProp);
            UpdateAction.execute(updateRequest, model);

            // Output the updated model to the console
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
