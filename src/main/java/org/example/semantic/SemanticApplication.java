package org.example.semantic;


import org.example.semantic.model.CreateRDF;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SemanticApplication {

	public static void main(String[] args) {
		CreateRDF C = new CreateRDF();
		C.creat();
	}


}
