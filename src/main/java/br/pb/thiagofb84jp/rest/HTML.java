package br.pb.thiagofb84jp.rest;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;

public class HTML {

    @Test
    public void deveFazerBuscasComHTML() {
        given()
             .log().all()
        .when()
             .get("http://restapi.wcaquino.me/v2/users")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .contentType(ContentType.HTML)
             .body("html.body.div.table.tbody.tr.size()", is(3))
             .body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
             .appendRootPath("html.body.div.table.tbody")
             .body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
        ;
    }

    @Test
    public void deveFazerBuscasComHTMLEXPath() {
        given()
             .log().all()
        .when()
             .get("http://restapi.wcaquino.me/v2/users?format=clean")
        .then()
             .log().all()
             .statusCode(200)
        .and()
             .contentType(ContentType.HTML)
             .body(hasXPath("count(//table/tr)", is("4")))
             .body(hasXPath("//td[text() = '2']/../td[2]", is("Maria Joaquina")))
        ;
    }

    @Test
    public void deveAcessarAplicacaoWeb() {
        String cookie = given()
                             .log().all()
                             .formParam("email", "thiago.ferreira@gmail.com")
                             .formParam("senha", "abcd_123")
                             .contentType(ContentType.URLENC.withCharset("UTF-8"))
                        .when()
                             .post("http://seubarriga.wcaquino.me/logar")
                        .then()
                             .log().all()
                             .statusCode(200)
                             .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        String body = given()
                           .log().all()
                           .cookie("connect.sid", cookie)
                      .when()
                           .get("http://seubarriga.wcaquino.me/contas")
                      .then()
                           .log().all()
                           .statusCode(200)
                      .and()
                           .body("html.body.table.tbody.tr[0].td[0]", is("Conta 1020"))
                           .extract().body().asString()
        ;

        System.out.println("|-----------------------|");
        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
    }
}