package br.pb.thiagofb84jp.rest.dummyJson.tests;

import br.pb.thiagofb84jp.rest.dummyJson.core.BaseTest;
import br.pb.thiagofb84jp.rest.dummyJson.dto.Product;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsTest extends BaseTest {

    @Test
    public void createNewProduct() {
        Product product = getValidProduct();

        given()
             .body(product)
        .when()
             .post("/products/add")
        .then()
             .statusCode(200)
        .and()
             .body("id", is(notNullValue()))
             .body("title", is(product.getTitle()))
        ;
    }

    @Test
    public void updateProduct() {
        Product product = getValidProduct();

        given()
             .pathParam("id", 1)
             .body(product)
        .when()
             .put("/products/{id}")
        .then()
             .statusCode(200)
        .and()
             .body("id", is("1"))
             .body("title", is(product.getTitle()))
             .body("images", hasSize(5))
             .body("images", hasItems("https://dummyjson.com/image/i/products/1/1.jpg",
                                         "https://dummyjson.com/image/i/products/1/2.jpg",
                                         "https://dummyjson.com/image/i/products/1/3.jpg",
                                         "https://dummyjson.com/image/i/products/1/4.jpg",
                                         "https://dummyjson.com/image/i/products/1/thumbnail.jpg"))
        ;
    }

    private static Product getValidProduct() {
        Faker faker = new Faker();
        Product product = new Product();
        product.setTitle(faker.commerce().productName());
        return product;
    }

}
