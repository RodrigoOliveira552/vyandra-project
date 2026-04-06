package br.com.vyandra.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginApiTest {

    private static Faker faker;
    private static String emailAdmin;
    private static String emailComum;
    private static String senhaPadrao = "senhaApi123";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://serverest.dev";
        faker = new Faker(new Locale("pt", "BR"));

        emailAdmin = faker.internet().emailAddress();
        String jsonAdmin = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + " (Admin)\",\n" +
                "  \"email\": \"" + emailAdmin + "\",\n" +
                "  \"password\": \"" + senhaPadrao + "\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(jsonAdmin)
                .post("/usuarios");

        emailComum = faker.internet().emailAddress();
        String jsonComum = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + " (Comum)\",\n" +
                "  \"email\": \"" + emailComum + "\",\n" +
                "  \"password\": \"" + senhaPadrao + "\",\n" +
                "  \"administrador\": \"false\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(jsonComum)
                .post("/usuarios");
    }

    @Test
    @DisplayName("Deve realizar login via API com usuário ADMINISTRADOR e capturar o Token")
    public void testLoginAdminViaApi() {
        String loginBody = "{\n" +
                "  \"email\": \"" + emailAdmin + "\",\n" +
                "  \"password\": \"" + senhaPadrao + "\"\n" +
                "}";

        System.out.println("Tentando logar Admin com: " + emailAdmin);

        given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("authorization", notNullValue());
    }

    @Test
    @DisplayName("Deve realizar login via API com usuário COMUM e capturar o Token")
    public void testLoginUsuarioComumViaApi() {
        String loginBody = "{\n" +
                "  \"email\": \"" + emailComum + "\",\n" +
                "  \"password\": \"" + senhaPadrao + "\"\n" +
                "}";

        System.out.println("Tentando logar Usuário Comum com: " + emailComum);

        given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("authorization", notNullValue());
    }
}