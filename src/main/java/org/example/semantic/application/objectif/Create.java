package org.example.semantic.application.objectif;


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
                            "   ns:Suivi_Emotionnel rdf:type owl:Class . \n" +
                            "   ns:Gestion_Des_Progres rdf:type owl:Class . \n" +
                            "   ns:Suivi_Emotionnel_Individuel rdf:type owl:Class ; \n" +
                            "      rdfs:subClassOf ns:Suivi_Emotionnel . \n" +
                            "   ns:Suivi_Emotionnel_Collectif rdf:type owl:Class ; \n" +
                            "      rdfs:subClassOf ns:Suivi_Emotionnel . \n" +

                            // Inserting new datatype properties
                            "   ns:niveau_de_satisfaction rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Suivi_Emotionnel ; \n" +
                            "      rdfs:range xsd:float . \n" +
                            "   ns:date_de_suivi rdf:type owl:DatatypeProperty ; \n" +
                            "      rdfs:domain ns:Suivi_Emotionnel ; \n" +
                            "      rdfs:range xsd:date . \n" +

                            // Inserting new object properties
                            "   ns:a_pour_suivi_emo rdf:type owl:ObjectProperty ; \n" +
                            "      rdfs:domain ns:Utilisateur ; \n" +
                            "      rdfs:range ns:Suivi_Emotionnel . \n" +
                            "   ns:est_suivi_emo_par rdf:type owl:ObjectProperty ; \n" +
                            "      rdfs:domain ns:Suivi_Emotionnel ; \n" +
                            "      rdfs:range ns:Coach . \n" +

                            // Inserting new instances
                            "   ns:Suivi_Emotionnel_Janvier rdf:type ns:Suivi_Emotionnel_Individuel ; \n" +
                            "      ns:niveau_de_satisfaction 80 ; \n" +
                            "      ns:date_de_suivi \"2024-01-15\"^^xsd:date . \n" +
                            "   ns:Suivi_Emotionnel_Février rdf:type ns:Suivi_Emotionnel_Collectif ; \n" +
                            "      ns:niveau_de_satisfaction 70 ; \n" +
                            "      ns:date_de_suivi \"2024-02-15\"^^xsd:date . \n" +
                            "   ns:Utilisateur_Marie rdf:type ns:Utilisateur ; \n" +
                            "      ns:a_pour_suivi_emo ns:Suivi_Emotionnel_Janvier . \n" +
                            "   ns:Coach_Sophie rdf:type ns:Coach ; \n" +
                            "      ns:specialite \"Gestion émotionnelle\" . \n" +
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
