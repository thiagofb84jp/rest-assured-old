package br.pb.thiagofb84jp.recipes.tests.refact;

import br.pb.thiagofb84jp.recipes.core.BaseTest;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class AuthTest extends BaseTest {

    @Test
    public void naoDeveAcessarAPISemToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) requestSpecification;
        req.removeHeader("Authorization");

        given()
        .when()
             .get("/contas")
        .then()
             .statusCode(401)
        ;
    }

}
