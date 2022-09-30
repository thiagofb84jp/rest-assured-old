package br.pb.thiagofb84jp.rest.tests;

import com.github.javafaker.Faker;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class RecipesTest {

    @Test
    public void createNewRecipe() {
        given()
        .when()
             .post("https://usman-recipes.herokuapp.com/api/recipes")
        .then()
             .statusCode(200)
        ;
    }

    @Test
    public void getRecipeById() {
        given()
             .pathParam("id", "6335e150f086aa00189afb5b")
        .when()
             .get("https://usman-recipes.herokuapp.com/api/recipes/{id}")
        .then()
             .statusCode(200)
        .and()
             .log().all()
        ;
    }

    private Recipe getValidRecipe() {
        Faker faker = new Faker();
        String title = faker.company().name();
        String body = faker.company().buzzword();

        Recipe recipe = new Recipe();

        recipe.setTitle(title);
        recipe.setBody(body);

        return recipe;
    }
}
