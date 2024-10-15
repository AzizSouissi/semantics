package org.example.semantic.model;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class CreateRDF {
    public void creat() {
        try {
            String adressURI = "http://www.exemple.com/espritadresse";
            String ArianaURI = "http://www.monsite.com/Ariana";
            String NameAdresse = "18 Rue de usine";
            String CP = "2035";
//la création d'espace de nom
            String EspaceNom = "http://www.exemple.com/vocabulaire#";
//la création d'un modéle
            Model model = ModelFactory.createDefaultModel();
// association de l'URI EspaceNom au prifixe en
            model.setNsPrefix("en", EspaceNom);
//La creation d'une resource
            Resource adresse = model.createResource(adressURI);
// creation d'une propriété (nameadresse)
            Property name = model.createProperty(EspaceNom, "name");
            adresse.addProperty(name, NameAdresse);
// creation d'une propriété (codepos)
            Property codepos = model.createProperty(EspaceNom, "codepos");
            adresse.addProperty(codepos, CP);
            Resource ville = model.createResource(ArianaURI);
            Property commeville = model.createProperty(EspaceNom, "commeville");
            adresse.addProperty(commeville, ville);
            model.write(System.out, "RDF/XML");
        } catch (Exception e) {
            System.out.println("le fichier n'existe pas");
        }
    }
}
