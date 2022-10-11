package br.pb.thiagofb84jp.rest.tests;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import br.pb.thiagofb84jp.rest.dto.Recipe;
import com.github.javafaker.Faker;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class RecipesTest extends BaseTest {

    @Test
    public void createNewRecipe() {
        Recipe recipe = getValidRecipe();

        given()
             .body(recipe)
             .log().all()
        .when()
             .post("/api/recipes")
        .then()
             .statusCode(200)
             .log().all()
        ;
    }

    private static String getIdRecipe() {
        Recipe recipe = getValidRecipe();

         String _id = given()
                            .body(recipe)
                       .when()
                            .post("/api/recipes")
                       .then()
                            .statusCode(200)
                       .and()
                            .extract().path("_id")
        ;

         return _id;
    }

    @Test
    public void getRecipeById() {
        String RECIPE_ID = getIdRecipe();

        given()
             .pathParam("id", RECIPE_ID)
             .log().all()
        .when()
             .get("/api/recipes/{id}")
        .then()
             .statusCode(200)
        .and()
             .log().all()
        ;
    }

    @Test
    public void getAllRecipes() {
        given()
        .when()
             .get("/api/recipes/")
        .then()
             .statusCode(200)
        .and()
             .log().all()
        ;
    }

    @Test
    public void updateRecipe() {
        Recipe recipe = getValidRecipe();

        given()
             .body(recipe)
             .pathParam("id", "6337493f10bf6d0018b95df0")
        .when()
             .put("/api/recipes/{id}")
        .then()
             .statusCode(200)
        .and()
             .log().all()
        ;
    }

    @Test
    public void deleteRecipe() {
        given()
             .pathParam("id", "63374a9d10bf6d0018b95df2")
        .when()
             .put("/api/recipes/{id}")
        .then()
             .statusCode(200)
        .and()
             .log().all()
        ;
    }

    private static Recipe getValidRecipe() {
        Faker faker = new Faker();
        String title = "Recipe " + faker.number().numberBetween(1, 100);
        String body = "Here I have a important recipe to do";

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setBody(body);

        return recipe;
    }
}
