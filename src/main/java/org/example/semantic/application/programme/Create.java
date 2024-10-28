package org.example.semantic.application.programme;

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

            // Define new RDF properties and classes to insert
            String newProp =
                    "PREFIX ns: <" + NS + ">\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +

                            // Inserting new classes
                            "INSERT DATA { \n" +
                            "   ns:Programme_Spécialisé_Yoga rdf:type owl:Class . \n" +
                            "   ns:Programme_Complet_Santé rdf:type owl:Class . \n" +
                            "   ns:Activité_Méditation rdf:type ns:Activité_Mentale ; \n" +
                            "      ns:durée 1.5 ; \n" +
                            "      ns:description \"Séance de méditation guidée\" . \n" +
                            "   ns:Activité_Course rdf:type ns:Activité_Physique ; \n" +
                            "      ns:durée 2.0 ; \n" +
                            "      ns:description \"Course à pied de 5 km\" . \n" +

                            // Inserting new datatype properties
                            "   ns:durée rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Activité ; \n" +
                            "      rdfs:range xsd:float . \n" +
                            "   ns:fréquence rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Programme_de_Bien-Être ; \n" +
                            "      rdfs:range xsd:string . \n" +
                            "   ns:description rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Activité ; \n" +
                            "      rdfs:range xsd:string . \n" +

                            // Inserting new object properties
                            "   ns:comprend rdf:type owl:ObjectProperty ; \n" +
                            "      rdfs:domain ns:Programme_de_Bien-Être ; \n" +
                            "      rdfs:range ns:Activité . \n" +
                            "   ns:est_type rdf:type owl:ObjectProperty ; \n" +
                            "      rdfs:domain ns:Activité_Mentale ; \n" +
                            "      rdfs:range ns:Programme_de_Bien-Être . \n" +

                            // Inserting new instances
                            "   ns:Programme_Complet_Santé rdf:type ns:Programme_de_Bien-Être ; \n" +
                            "      ns:fréquence \"Hebdomadaire\" ; \n" +
                            "      ns:description \"Programme de bien-être complet\" ; \n" +
                            "      ns:comprend ns:Activité_Méditation, ns:Activité_Course . \n" +
                            "   ns:Programme_Spécialisé_Yoga rdf:type ns:Programme_de_Bien-Être ; \n" +
                            "      ns:fréquence \"Mensuel\" ; \n" +
                            "      ns:description \"Programme de yoga spécialisé\" ; \n" +
                            "      ns:comprend ns:Activité_Méditation . \n" +
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
