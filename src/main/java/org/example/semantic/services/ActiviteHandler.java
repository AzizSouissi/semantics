package org.example.semantic.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ActiviteHandler implements HttpHandler {
    private String fusekiEndpoint;
    private Gson gson = new Gson();
    private static final String ONTOLOGY_NAMESPACE = "http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#";

    public ActiviteHandler(String fusekiEndpoint) {
        this.fusekiEndpoint = fusekiEndpoint;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> this.handleGet(exchange);
            case "POST" -> this.handlePost(exchange);
            case "PUT" -> this.handlePut(exchange);
            case "DELETE" -> this.handleDelete(exchange);
            default -> this.sendResponse(exchange, this.createResponse("Unsupported method."), 405);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String queryString = "PREFIX ns: <" + ONTOLOGY_NAMESPACE + "> SELECT ?activite ?duree ?description WHERE { ?activite a ns:Activite ; ns:duree ?duree ; ns:description ?description .}";
        String result = this.executeSparqlQuery(queryString);
        this.sendResponse(exchange, result, 200);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

        double duree = jsonObject.get("duree").getAsDouble();
        String description = jsonObject.get("description").getAsString().trim();

        String insertQuery = String.format(
                "PREFIX ns: <%s> INSERT DATA { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> a ns:Activite . " +
                        "<http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:nom \"%s\" . " +
                        "<http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:duree \"%f\" . " +
                        "<http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:description \"%s\" . }",
                ONTOLOGY_NAMESPACE, duree , description
        );

        try {
            this.executeUpdate(insertQuery);
            this.sendResponse(exchange, this.createResponse("Activity created: "), 201);
        } catch (IOException e) {
            this.sendResponse(exchange, this.createResponse("Failed to create activity: " + e.getMessage()), 500);
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

        String nom = jsonObject.get("nom").getAsString().trim();
        double duree = jsonObject.get("duree").getAsDouble();
        String description = jsonObject.get("description").getAsString().trim();

        String updateQuery = String.format(
                "PREFIX ns: <%s> DELETE { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:duree ?duree; ns:description ?description. } " +
                        "INSERT { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:duree \"%f\"; ns:description \"%s\". } " +
                        "WHERE { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite%s> ns:duree ?duree; ns:description ?description; }",
                ONTOLOGY_NAMESPACE, nom, nom, duree, description, nom
        );

        try {
            this.executeUpdate(updateQuery);
            this.sendResponse(exchange, this.createResponse("Activity updated: " + nom), 200);
        } catch (IOException e) {
            this.sendResponse(exchange, this.createResponse("Failed to update activity: " + e.getMessage()), 500);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String activiteId = path.substring(path.lastIndexOf('/') + 1);
        String deleteQuery = "PREFIX ns: <" + ONTOLOGY_NAMESPACE + "> DELETE { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite" + activiteId + "> a ns:Activite . } WHERE { <http://www.semanticweb.org/hafsi/ontologies/2024/9/untitled-ontology-24#Activite" + activiteId + "> a ns:Activite . }";

        try {
            this.executeUpdate(deleteQuery);
            this.sendResponse(exchange, this.createResponse("Activity deleted"), 200);
        } catch (IOException e) {
            this.sendResponse(exchange, this.createResponse("Failed to delete activity: " + e.getMessage()), 500);
        }
    }

    private String executeSparqlQuery(String query) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) (new URL(this.fusekiEndpoint)).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/sparql-query");
        conn.getOutputStream().write(query.getBytes(StandardCharsets.UTF_8));

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private void executeUpdate(String update) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) (new URL(this.fusekiEndpoint)).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/sparql-update");
        conn.getOutputStream().write(update.getBytes(StandardCharsets.UTF_8));
        conn.getResponseCode(); // Trigger the request
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String createResponse(String message) {
        return this.gson.toJson(Map.of("message", message));
    }
}