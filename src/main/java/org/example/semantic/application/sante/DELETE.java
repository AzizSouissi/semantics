package org.example.semantic.application.sante;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.example.semantic.tools.JenaEngine;

public class DELETE {
    public DELETE() {
    }

    public static void main(String[] args) {
        String NS = "";
        String resourceToDelete = "DavidBrown"; // Spécifiez ici la ressource à supprimer

        // Lire le modèle RDF existant à partir du fichier OWL
        Model model = JenaEngine.readModel("data/ontologie.owl");
        if (model != null) {
            NS = model.getNsPrefixURI("");

            // Définir l'opération de suppression pour la ressource spécifiée
            String deleteOperation =
                    "PREFIX ns: <" + NS + ">\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +

                            "DELETE { \n" +
                            "   ns:" + resourceToDelete + " ?p ?o . \n" + // Supprimer toutes les triplets de cette ressource
                            "}\n" +
                            "WHERE { \n" +
                            "   ns:" + resourceToDelete + " ?p ?o . \n" + // Vérifie que la ressource existe
                            "}" ;

            // Créer la requête de mise à jour et l'exécuter
            UpdateRequest updateRequest = UpdateFactory.create(deleteOperation);
            UpdateAction.execute(updateRequest, model);

            // Sortir le modèle mis à jour dans la console
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}