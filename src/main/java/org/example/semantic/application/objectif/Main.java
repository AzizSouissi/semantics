package org.example.semantic.application.objectif;

import org.apache.jena.rdf.model.Model;
import org.example.semantic.tools.JenaEngine;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String NS = "";
        Model model = JenaEngine.readModel("data/ontologie.owl");
        if (model != null) {
            NS = model.getNsPrefixURI("");
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
        } else {
            System.out.println("Error when reading model from ontology");
        }

    }
}