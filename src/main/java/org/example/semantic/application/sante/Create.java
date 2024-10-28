package org.example.semantic.application.sante;



import org.example.semantic.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class Create {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String NS = "";

        // Read the model from a health ontology
        Model model = JenaEngine.readModel("data/ontologie.owl");

        if (model != null) {
            // Read the namespace of the ontology
            NS = model.getNsPrefixURI("");

            // Apply our rules on the inferred model
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Create a SPARQL update query to add a new patient and health information
            String newHealthData = "PREFIX ns: <" + NS + ">\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "INSERT DATA {" +
                    "  ns:JohnDoe rdf:type owl:NamedIndividual, ns:Patient ;" +
                    "              ns:hasName \"John Doe\" ;" +
                    "              ns:hasAge 40 ;" +
                    "              ns:hasCondition ns:Hypertension." +
                    "  ns:Hypertension rdf:type ns:HealthCondition ;" +
                    "                  rdfs:label \"Hypertension\" ." +
                    "}";

            // Execute the SPARQL update query on the inferred model
            UpdateRequest updateRequest = UpdateFactory.create(newHealthData);
            UpdateAction.execute(updateRequest, inferredModel);

            // Print the updated model or do further processing
            inferredModel.write(System.out, "RDF/XML");
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}

