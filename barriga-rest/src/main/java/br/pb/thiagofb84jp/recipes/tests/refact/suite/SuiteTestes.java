package br.pb.thiagofb84jp.recipes.tests.refact.suite;

import br.pb.thiagofb84jp.recipes.core.BaseTest;
import br.pb.thiagofb84jp.recipes.tests.refact.AuthTest;
import br.pb.thiagofb84jp.recipes.tests.refact.ContasTest;
import br.pb.thiagofb84jp.recipes.tests.refact.MovimentacaoTest;
import br.pb.thiagofb84jp.recipes.tests.refact.SaldoTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class SuiteTestes extends BaseTest {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "thiago.ferreira@gmail.com");
        login.put("senha", "abcd_123");

        String TOKEN = given()
                            .body(login)
                       .when()
                            .post("/signin")
                       .then()
                            .statusCode(200)
                            .extract().path("token")
                       ;

        requestSpecification.header("Authorization", "JWT " + TOKEN);
        get("/reset").then().statusCode(200);
    }

}
