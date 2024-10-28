package org.example.semantic.tools;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import java.io.*;
import java.util.List;

public class JenaEngine {

    static public Model readModel(String inputDataFile) {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputDataFile);
        if (in == null) {
            System.out.println("Ontology file: " + inputDataFile + " not found");
            return null;
        }
        try {
            model.read(in, "");
        } catch (Exception e) {
            System.out.println("Error reading model: " + e.getMessage());
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    static public Model readInferencedModelFromRuleFile(Model model, String inputRuleFile) {
        InputStream in = FileManager.get().open(inputRuleFile);
        if (in == null) {
            System.out.println("Rule file: " + inputRuleFile + " not found");
            return null;
        } else {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<Rule> rules = Rule.rulesFromURL(inputRuleFile);
        GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
        InfModel infModel = ModelFactory.createInfModel(reasoner, model);
        return infModel;
    }

    static public String executeQuery(Model model, String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        return ResultSetFormatter.asText(results);
    }

    static public String executeQueryFile(Model model, String filepath) {
        File queryFile = new File(filepath);
        InputStream in = FileManager.get().open(filepath);
        if (in == null) {
            System.out.println("Query file: " + filepath + " not found");
            return null;
        }
        StringBuilder queryString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queryString.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading query file: " + e.getMessage());
            return null;
        }
        return executeQuery(model, queryString.toString());
    }



}
