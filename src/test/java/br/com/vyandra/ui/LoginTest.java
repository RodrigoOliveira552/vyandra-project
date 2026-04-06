package br.com.vyandra.ui;

import br.com.vyandra.core.BaseTest;
import br.com.vyandra.pages.LoginPage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    @DisplayName("Deve realizar login com sucesso usando usuário injetado via API")
    public void testLoginComSucesso() {
        LoginPage loginPage = new LoginPage(driver);
        Faker faker = new Faker(new Locale("pt", "BR"));

        // Gerando os dados falsos
        String emailDinamico = faker.internet().emailAddress();
        String senhaDinamica = "senhaForte123";

        // 1. Arrange (PREPARAÇÃO SÊNIOR): Criar o usuário via API silenciosamente antes de abrir a tela
        String jsonBody = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + "\",\n" +
                "  \"email\": \"" + emailDinamico + "\",\n" +
                "  \"password\": \"" + senhaDinamica + "\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        given()
                .baseUri("https://serverest.dev")
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201); // Garante que o banco salvou

        // 2. Act: Agora sim, com o usuário no banco, o robô vai pra tela de login
        loginPage.acessarPagina();
        loginPage.realizarLogin(emailDinamico, senhaDinamica);

        // 3. Assert: Valida se entrou no sistema
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean deslogarVisivel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[data-testid='logout']")
        )).isDisplayed();

        assertTrue(deslogarVisivel, "O botão de Logout não apareceu, o login falhou!");
    }
}