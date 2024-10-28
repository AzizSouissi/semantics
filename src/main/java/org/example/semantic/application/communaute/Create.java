package org.example.semantic.application.communaute;

import org.example.semantic.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class Create {
    public static void main(String[] args) {
        String NS = "http://example.org/ontologies/Gestion_des_Communautés#";

        // Read the model from an ontology file (if needed)
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            // Create classes
            Resource communauté = model.createResource(NS + "Communauté");
            Resource groupeDeSoutien = model.createResource(NS + "Groupe_de_Soutien");
            Resource groupeInteret = model.createResource(NS + "Groupe_Intérêt");
            Resource discussion = model.createResource(NS + "Discussion");
            Resource filDeDiscussion = model.createResource(NS + "Fil_de_Discussion");
            Resource evenementDePartage = model.createResource(NS + "Événement_de_Partage");
            Resource utilisateur = model.createResource(NS + "Utilisateur");

            // Create properties
            Property nom = model.createProperty(NS + "nom");
            Property description = model.createProperty(NS + "description");
            Property sujet = model.createProperty(NS + "sujet");
            Property participeA = model.createProperty(NS + "participeÀ");
            Property organise = model.createProperty(NS + "organise");

            // Create instances
            Resource communauteBienEtre = model.createResource(NS + "CommunauteBienEtre")
                    .addProperty(nom, "Bien-Être et Santé")
                    .addProperty(description, "Groupe dédié au soutien mental et physique.")
                    .addProperty(organise, model.createResource(NS + "Evenement1"));

            Resource groupeYoga = model.createResource(NS + "GroupeYoga")
                    .addProperty(nom, "Passion Yoga")
                    .addProperty(description, "Groupe pour les amateurs de yoga.");

            Resource discussion1 = model.createResource(NS + "Discussion1")
                    .addProperty(sujet, "Conseils pour une meilleure santé mentale.");

            Resource evenement1 = model.createResource(NS + "Evenement1")
                    .addProperty(nom, "Atelier de yoga pour débutants.");

            Resource evenement2 = model.createResource(NS + "Evenement2") // Adding new event
                    .addProperty(nom, "Séance de méditation avancée.")
                    .addProperty(description, "Atelier pour approfondir la méditation et le bien-être mental.");

            Resource utilisateur1 = model.createResource(NS + "Utilisateur1")
                    .addProperty(participeA, discussion1);

            // Create SPARQL update query for adding the above instances and properties
            String insertData = "PREFIX ns: <" + NS + ">\n" +
                    "INSERT DATA {\n" +
                    "  ns:CommunauteBienEtre a ns:Communauté ;\n" +
                    "                        ns:nom \"Bien-Être et Santé\" ;\n" +
                    "                        ns:description \"Groupe dédié au soutien mental et physique.\" ;\n" +
                    "                        ns:organise ns:Evenement1 .\n" +
                    "  ns:GroupeYoga a ns:Groupe_Intérêt ;\n" +
                    "               ns:nom \"Passion Yoga\" ;\n" +
                    "               ns:description \"Groupe pour les amateurs de yoga.\" .\n" +
                    "  ns:Discussion1 a ns:Fil_de_Discussion ;\n" +
                    "               ns:sujet \"Conseils pour une meilleure santé mentale.\" .\n" +
                    "  ns:Evenement1 a ns:Événement_de_Partage ;\n" +
                    "               ns:nom \"Atelier de yoga pour débutants.\" .\n" +
                    "  ns:Evenement2 a ns:Événement_de_Partage ;\n" +  // Add new event to query
                    "               ns:nom \"Séance de méditation avancée.\" ;\n" +
                    "               ns:description \"Atelier pour approfondir la méditation et le bien-être mental.\" .\n" +
                    "  ns:Utilisateur1 a ns:Utilisateur ;\n" +
                    "                 ns:participeÀ ns:Discussion1 .\n" +
                    "}";

            // Execute the SPARQL update query on the model
            UpdateRequest updateRequest = UpdateFactory.create(insertData);
            UpdateAction.execute(updateRequest, model);

            // Print the updated model in RDF/XML format
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}