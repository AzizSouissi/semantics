package org.example.semantic.models;

import org.apache.jena.rdf.model.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserModel {

    private final Model model;
    private final String ns = "http://www.example.com/users#";
    private final Property passwordProp;
    private final Property roleProp;

    public UserModel() {
        // Initialize the RDF model
        model = ModelFactory.createDefaultModel();
        model.setNsPrefix("user", ns);

        // Define properties
        passwordProp = model.createProperty(ns, "password");
        roleProp = model.createProperty(ns, "role");
    }

    // Create a new user
    public void addUser(String username, String password, String role) {
        Resource user = model.createResource(ns + username);
        user.addProperty(passwordProp, password);
        user.addProperty(roleProp, role);
    }

    // Find user by username
    public Optional<Resource> findUser(String username) {
        Resource user = model.getResource(ns + username);
        return model.containsResource(user) ? Optional.of(user) : Optional.empty();
    }

    // Update user's role and password
    public boolean updateUser(String username, String newPassword, String newRole) {
        Optional<Resource> user = findUser(username);
        if (user.isPresent()) {
            Resource u = user.get();
            u.removeAll(passwordProp).addProperty(passwordProp, newPassword);
            u.removeAll(roleProp).addProperty(roleProp, newRole);
            return true;
        }
        return false;
    }

    // Delete a user
    public boolean deleteUser(String username) {
        Optional<Resource> user = findUser(username);
        if (user.isPresent()) {
            model.removeAll(user.get(), null, (RDFNode) null);
            return true;
        }
        return false;
    }

    // List all users
    public String listUsers() {
        StringBuilder users = new StringBuilder();
        ResIterator iter = model.listResourcesWithProperty(passwordProp);
        while (iter.hasNext()) {
            Resource user = iter.next();
            String username = user.getURI().replace(ns, "");
            String role = user.getProperty(roleProp).getString();
            users.append("User: ").append(username).append(", Role: ").append(role).append("\n");
        }
        return users.toString();
    }

    // Create a getter for the model
    public Model getModel() {
        return model;
    }
}
