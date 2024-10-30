package org.example.semantic.services;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.update.*;
import org.apache.jena.vocabulary.RDFS;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.example.semantic.entities.ProgrammeDeBienEtre;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammeDeBienEtreHandler implements HttpHandler {
    private static Model model;
    private static String NS;
    private final Gson gson = new Gson();

    public ProgrammeDeBienEtreHandler(Model model, String namespace) {
        ProgrammeDeBienEtreHandler.model = model;
        ProgrammeDeBienEtreHandler.NS = namespace;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        switch (exchange.getRequestMethod()) {
            case "POST":
                response = createProgramme(exchange);
                break;
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/programme")) {
                    response = getAllProgrammes();
                } else {
                    response = readProgramme(exchange);
                }
                break;
            case "PUT":
                response = updateProgramme(exchange);
                break;
            case "DELETE":
                response = deleteProgramme(exchange);
                break;
            default:
                response = "{\"error\": \"Unsupported method!\"}";
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String createProgramme(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        ProgrammeDeBienEtre programme = gson.fromJson(isr, ProgrammeDeBienEtre.class);
        Resource programmeResource = model.createResource(NS + programme.getNom());
        programmeResource.addProperty(RDFS.label, programme.getNom());
        programmeResource.addProperty(model.createProperty(NS + "frequence"), programme.getFrequence());
        programmeResource.addProperty(model.createProperty(NS + "description"), programme.getDescription());
        return "{\"message\": \"Programme created: " + programme.getNom() + "\"}";
    }

    private String readProgramme(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String nom = path.split("/")[2];
        List<ProgrammeDeBienEtre> programmes = new ArrayList<>();
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX ns: <" + NS + "> " +
                "SELECT ?s ?frequence ?description WHERE { " +
                "?s rdfs:label \"" + nom + "\" ." +
                "?s ns:frequence ?frequence ." +
                "?s ns:description ?description }";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                ProgrammeDeBienEtre programme = new ProgrammeDeBienEtre();
                programme.setNom(soln.getResource("s").getLocalName());
                programme.setFrequence(soln.getLiteral("frequence").getString());
                programme.setDescription(soln.getLiteral("description").getString());
                programmes.add(programme);
            }
        }
        return gson.toJson(programmes);
    }

    private String updateProgramme(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String oldName = path.split("/")[2];
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        ProgrammeDeBienEtre updatedProgramme = gson.fromJson(isr, ProgrammeDeBienEtre.class);

        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "DELETE { ?s rdfs:label \"" + oldName + "\" } " +
                "INSERT { ?s rdfs:label \"" + updatedProgramme.getNom() + "\" } " +
                "WHERE { ?s rdfs:label \"" + oldName + "\" }";
        executeUpdate(queryString);
        return "{\"message\": \"Programme updated from " + oldName + " to " + updatedProgramme.getNom() + "\"}";
    }

    private String deleteProgramme(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String nom = path.split("/")[2];
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "DELETE { ?s rdfs:label \"" + nom + "\" } " +
                "WHERE { ?s rdfs:label \"" + nom + "\" }";
        executeUpdate(queryString);
        return "{\"message\": \"Programme deleted: " + nom + "\"}";
    }

    private String getAllProgrammes() {
        List<ProgrammeDeBienEtre> programmes = new ArrayList<>();
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX ns: <" + NS + "> " +
                "SELECT ?s ?nom ?frequence ?description WHERE { " +
                "?s rdfs:label ?nom ." +
                "?s ns:frequence ?frequence ." +
                "?s ns:description ?description }";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                ProgrammeDeBienEtre programme = new ProgrammeDeBienEtre();
                programme.setNom(soln.getLiteral("nom").getString());
                programme.setFrequence(soln.getLiteral("frequence").getString());
                programme.setDescription(soln.getLiteral("description").getString());
                programmes.add(programme);
            }
        }
        return gson.toJson(programmes);
    }

    private void executeUpdate(String updateString) {
        Dataset dataset = DatasetFactory.create(model);
        UpdateRequest updateRequest = UpdateFactory.create(updateString);
        UpdateExecutionFactory.create(updateRequest, dataset).execute();
    }
}

