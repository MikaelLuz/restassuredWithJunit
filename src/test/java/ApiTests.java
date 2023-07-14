import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class ApiTests {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/users/";
    }

    @Test
    public void getUsersTest() {
        given()
                .when()
                .get()
                .then();
        Assertions.assertEquals(202, HttpStatus.SC_ACCEPTED);
        System.out.println("O status code retornado foi: " + HttpStatus.SC_ACCEPTED);
    }

    @Test
    public void createUserTest() {
        User user = new User("John", "Doe");

        given().
                contentType(ContentType.JSON).
                body(user).
                when().
                post().
                then().
                statusCode(HttpStatus.SC_CREATED).
                body(
                        "name", is(user.getName()),
                        "job", is(user.getJob()),
                        "id", notNullValue(),
                        "createdAt", notNullValue()
                );
        Assertions.assertEquals(201, HttpStatus.SC_CREATED);
        System.out.println("Usuário criado com sucesso! : " + HttpStatus.SC_ACCEPTED);
    }

    @Test
    public void validateSchema() {
        given()
                .when()
                .get()
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/users-schema.json"));
    }

    @Test
    public void validateRequireField() {
        given()
                .get()
                .then()
                .assertThat()
                .body("page", notNullValue(),
                        "per_page", notNullValue(),
                        "total", notNullValue(),
                        "total_pages", notNullValue(),
                        "data[]", notNullValue());
    }
}
