package br.pb.thiagofb84jp.rest;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserXMLTest {

    @Test
    public void devoTrabalharComXML() {
        given()
        .when()
             .get("https://restapi.wcaquino.me/usersXML/3")
        .then()
             .statusCode(200)
        .and()
            .body("user.name", is("Ana Julia"))
            .body("user.@id", is("3"))
            .body("user.filhos.name.size()", is(2))
            .body("user.filhos.name[0]", is("Zezinho"))
            .body("user.filhos.name[1]", is("Luizinho"))
            .body("user.filhos.name", hasItem("Luizinho"))
            .body("user.filhos.name", hasItems("Luizinho", "Zezinho"))
        ;
    }
}
