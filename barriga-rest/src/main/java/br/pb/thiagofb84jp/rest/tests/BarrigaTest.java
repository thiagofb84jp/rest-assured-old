package br.pb.thiagofb84jp.rest.tests;

import br.pb.thiagofb84jp.rest.core.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest {

    private String TOKEN;

    @Before
    public void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "thiago.ferreira@gmail.com");
        login.put("senha", "abcd_123");

        TOKEN = given()
                     .body(login)
                .when()
                     .post("/signin")
                .then()
                     .statusCode(200)
                     .extract().path("token")
                ;
    }

    @Test
    public void naoDeveAcessarAPISemToken() {
        given()
        .when()
             .get("/contas")
        .then()
             .statusCode(401)
        ;
    }

    @Test
    public void deveIncluirContaComSucesso() {
        Faker faker = new Faker();
        int number = faker.number().numberBetween(1, 500);

        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \"Conta " +  number +"\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \"Conta alterada\"}")
        .when()
             .put("/contas/1381328")
        .then()
             .statusCode(200)
        .and()
             .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{\"nome\": \"Conta alterada\"}")
        .when()
             .post("/contas")
        .then()
             .statusCode(400)
        .and()
             .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        given()
             .header("Authorization", "JWT " + TOKEN)
             .body(mov)
        .when()
             .post("/transacoes")
        .then()
             .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
        given()
             .header("Authorization", "JWT " + TOKEN)
             .body("{}")
        .when()
             .post("/transacoes")
        .then()
             .statusCode(400)
        .and()
             .body("$", hasSize(8))
             .body("msg", hasItems(
                     "Data da Movimentação é obrigatório",
                     "Data do pagamento é obrigatório",
                     "Descrição é obrigatório",
                     "Interessado é obrigatório",
                     "Valor é obrigatório",
                     "Valor deve ser um número",
                     "Conta é obrigatório",
                     "Situação é obrigatório"
             ))
        ;
    }

    @Test
    public void deveInserirMovimentacaoComDataFutura() {
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(getFutureDate());

        given()
             .header("Authorization", "JWT " + TOKEN)
             .body(mov)
        .when()
             .post("/transacoes")
        .then()
             .statusCode(400)
        .and()
             .body("$", hasSize(1))
             .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    /*
    *   Generating mass of data
     */
    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();

        mov.setConta_id(1381328);
//        mov.setUsuarioId();
        mov.setDescricao("Descrição da movimentação");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao(getPreviousDate());
        mov.setData_pagamento(getCurrentDate());
        mov.setValor(100F);
        mov.setStatus(true);

        return mov;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());

        return currentDate;
    }

    private String getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String futureDate = dateFormat.format(new Date((calendar.getTimeInMillis())));

        return futureDate;
    }

    private String getFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String futureDate = dateFormat.format(new Date((calendar.getTimeInMillis())));

        return futureDate;
    }

}