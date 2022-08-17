package br.pb.thiagofb84jp.rest;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbosTest {

    Faker faker = new Faker(new Locale("pt-BR"));
    String name = faker.name().fullName();
    int age = faker.number().numberBetween(1, 99);

    @Test
    public void deveSalvarUsuario() {
        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body("{\"name\": \"" + name + "\", \"age\": "+ age +"}")
        .when()
             .post("https://restapi.wcaquino.me/users")
        .then()
             .log().all()
             .statusCode(201)
        .and()
             .body("id", is(notNullValue()))
             .body("name", is(name))
             .body("age", is(age))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("age", age);

        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body(params)
        .when()
             .post("https://restapi.wcaquino.me/users")
        .then()
             .log().all()
             .statusCode(201)
        .and()
             .body("id", is(notNullValue()))
             .body("name", is(name))
             .body("age", is(age))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto() {
        User user = new User(name, age);

        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body(user)
        .when()
             .post("https://restapi.wcaquino.me/users")
        .then()
             .log().all()
             .statusCode(201)
        .and()
             .body("id", is(notNullValue()))
             .body("name", is(user.getName()))
             .body("age", is(user.getAge()))
        ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuario() {
        User user = new User(name, age);

        User insertedUser  = given()
                                  .log().all()
                                  .contentType(ContentType.JSON)
                                  .body(user)
                             .when()
                                  .post("https://restapi.wcaquino.me/users")
                             .then()
                                  .log().all()
                                  .statusCode(201)
                             .and()
                                  .extract().body().as(User.class)
                             ;

        System.out.println(insertedUser);

        Assert.assertThat(insertedUser.getId(), notNullValue());
        Assert.assertEquals(name, insertedUser.getName());
        Assert.assertThat(insertedUser.getAge(), is(user.getAge()));
    }

    @Test
    public void deveDesearializarObjetoAoSalvarUsuario() {
        User user = new User(name, age);

        User usuarioInserido = given()
                                    .log().all()
                                    .contentType(ContentType.JSON)
                                    .body(user)
                               .when()
                                    .post("https://restapi.wcaquino.me/users")
                               .then()
                                    .log().all()
                                    .statusCode(201)
                               .and()
                                    .extract().body().as(User.class)
                               ;

        System.out.println(usuarioInserido);

        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals(name, usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(age));
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body("{\"age\": "+ age +"}")
        .when()
             .post("https://restapi.wcaquino.me/users")
        .then()
             .log().all()
             .statusCode(400)
        .and()
             .body("id", is(nullValue()))
             .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXML() {
        given()
             .log().all()
             .contentType(ContentType.XML)
             .body("<user><name>"+ name +"</name><age>"+ age +"</age></user>")
        .when()
             .post("https://restapi.wcaquino.me/usersXML")
        .then()
             .log().all()
             .statusCode(201)
        .and()
             .body("user.@id", is(notNullValue()))
             .body("user.name", is(name))
             .body("user.age", is(Integer.toString(age)))
        ;
    }

//    @Test
//    public void deveSalvarUsuarioViaXMLUsandoObjeto() {
//        User user = new User(name, age);
//
//        given()
//             .log().all()
//             .contentType(ContentType.XML)
//             .body(user)
//        .when()
//             .post("https://restapi.wcaquino.me/usersXML")
//        .then()
//             .log().all()
//             .statusCode(201)
//        .and()
//             .body("user.@id", is(notNullValue()))
//             .body("user.name", is(name))
//             .body("user.age", is(Integer.toString(age)))
//        ;
//    }

    @Test
    public void deveAlterarUsuario() {
        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body("{\"name\": \"" + name + "\", \"age\": "+ age +"}")
        .when()
             .put("https://restapi.wcaquino.me/users/1")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("id", is(1))
             .body("name", is(name))
             .body("age", is(age))
             .body("salary", is(1234.5678F))
        ;
    }

    @Test
    public void devoCustomizarURLParte1() {
        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body("{\"name\": \"" + name + "\", \"age\": "+ age +"}")
        .when()
             .put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("id", is(1))
             .body("name", is(name))
             .body("age", is(age))
             .body("salary", is(1234.5678F))
        ;
    }

    @Test
    public void devoCustomizarURLParte2() {
        given()
             .log().all()
             .contentType(ContentType.JSON)
             .body("{\"name\": \"" + name + "\", \"age\": "+ age +"}")
             .pathParam("entidade", "users")
             .pathParam("userID", "1")
        .when()
             .put("https://restapi.wcaquino.me/{entidade}/{userID}")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .body("id", is(1))
             .body("name", is(name))
             .body("age", is(age))
             .body("salary", is(1234.5678F))
        ;
    }

    @Test
    public void deveRemoverUsuario() {
        given()
             .log().all()
        .when()
             .delete("https://restapi.wcaquino.me/users/1")
        .then()
             .log().all()
             .statusCode(204)
        ;
    }

    @Test
    public void deveRemoverUsuarioInexistente() {
        given()
             .log().all()
        .when()
             .delete("https://restapi.wcaquino.me/users/1000")
        .then()
             .log().all()
             .statusCode(400)
             .body("error", is("Registro inexistente"))
        ;
    }

}
