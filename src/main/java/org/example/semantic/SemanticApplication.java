package org.example.semantic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

@SpringBootApplication
public class SemanticApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemanticApplication.class, args);
		Model model = ModelFactory.createDefaultModel();
	}

}
