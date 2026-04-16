package br.com.vyandra.ui;

import br.com.vyandra.core.BaseTest;
import br.com.vyandra.pages.LoginPage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    private final Faker faker = new Faker(new Locale("pt", "BR"));
    private final String senhaPadrao = "senhaForte123";

    private String injetarUsuarioViaApi(boolean ehAdmin) {
        String emailDinamico = faker.internet().emailAddress();

        String jsonBody = "{\n" +
                "  \"nome\": \"" + faker.name().fullName() + "\",\n" +
                "  \"email\": \"" + emailDinamico + "\",\n" +
                "  \"password\": \"" + senhaPadrao + "\",\n" +
                "  \"administrador\": \"" + ehAdmin + "\"\n" +
                "}";

        given()
                .baseUri("https://serverest.dev")
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201);

        System.out.println(" Usuário injetado via API para o teste. Admin: " + ehAdmin);
        return emailDinamico;
    }

    @Test
    @DisplayName("Deve realizar login com sucesso usando perfil ADMINISTRADOR")
    public void testLoginAdminComSucesso() {
        String emailAdmin = injetarUsuarioViaApi(true);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.acessarPagina();
        loginPage.realizarLogin(emailAdmin, senhaPadrao);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean deslogarVisivel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[data-testid='logout']")
        )).isDisplayed();

        assertTrue(deslogarVisivel, "O botão de Logout não apareceu para o Administrador, o login falhou!");
    }

    @Test
    @DisplayName("Deve realizar login com sucesso usando perfil COMUM")
    public void testLoginUsuarioComumComSucesso() {
        String emailComum = injetarUsuarioViaApi(false);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.acessarPagina();
        loginPage.realizarLogin(emailComum, senhaPadrao);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean deslogarVisivel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button[data-testid='logout']")
        )).isDisplayed();

        assertTrue(deslogarVisivel, "O botão de Logout não apareceu para o Usuário Comum, o login falhou!");
    }

    @Test
    @DisplayName("Deve exibir erro ao tentar logar com usuário INEXISTENTE/INVÁLIDO")
    public void testLoginUsuarioInvalido() {
        String emailFalso = faker.internet().emailAddress();
        String senhaFalsa = "senhaErrada123";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.acessarPagina();
        loginPage.realizarLogin(emailFalso, senhaFalsa);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement alertaErro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".alert")
        ));

        String textoAlerta = alertaErro.getText();

        assertTrue(textoAlerta.contains("Email e/ou senha inválidos"),
                "A mensagem de erro esperada não apareceu! O que retornou foi: " + textoAlerta);

        System.out.println("Erro ao tentar logar com usuário INEXISTENTE/INVÁLIDO");

    }
}