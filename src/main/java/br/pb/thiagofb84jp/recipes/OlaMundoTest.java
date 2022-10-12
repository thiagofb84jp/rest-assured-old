package br.pb.thiagofb84jp.recipes;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OlaMundoTest {

    @Test
    public void testOlaMundo() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        assertTrue(response.statusCode() == 200);
        assertTrue("O status code deveria ser 200", response.statusCode() == 200);
        assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
             //Coloca-se aqui pré-condições
        .when()
             //Ação do que vai ser testado
             .get("http://restapi.wcaquino.me/ola")
        .then()
             //Coloca-se as assertivas
             .assertThat()
             .statusCode(200);
    }

    @Test
    public void devoConhecerMatchersHamcrest() {
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class));
        assertThat(128D, isA(Double.class));
        assertThat(128D, greaterThan(120D));
        assertThat(128D, lessThan(130D));

        List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1, 3, 5, 7, 9));
        assertThat(impares, containsInAnyOrder(1, 3, 5, 9, 7));
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1, 5));

        assertThat("Maria", is(not("João")));
        assertThat("Maria", is("Maria"));
        assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
    }

    @Test
    public void devoValidarBody() {
        given()
        .when()
              .get("http://restapi.wcaquino.me/ola")
        .then()
              .statusCode(200)
              .body(is("Ola Mundo!"))
              .body(containsString("Mundo"))
              .body(is(not(nullValue())));
    }
}