package br.com.vyandra.ui;

import br.com.vyandra.core.BaseTest;
import br.com.vyandra.pages.CadastroUsuarioPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CadastroUsuarioTest extends BaseTest {

    @Test
    @DisplayName("Deve cadastrar um usuário COMUM com sucesso")
    public void testCadastrarUsuarioComum() {
        CadastroUsuarioPage cadastroPage = new CadastroUsuarioPage(driver);
        Faker faker = new Faker(new Locale("pt", "BR"));

        cadastroPage.acessarPagina();
        cadastroPage.preencherDados(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "senha123"
        );
        cadastroPage.submeterCadastro();

        validarMensagemDeSucesso();
    }

    @Test
    @DisplayName("Deve cadastrar um usuário ADMINISTRADOR com sucesso")
    public void testCadastrarUsuarioAdmin() {
        CadastroUsuarioPage cadastroPage = new CadastroUsuarioPage(driver);
        Faker faker = new Faker(new Locale("pt", "BR"));

        cadastroPage.acessarPagina();
        cadastroPage.preencherDados(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "admin123"
        );
        cadastroPage.marcarComoAdmin();
        cadastroPage.submeterCadastro();

        validarMensagemDeSucesso();
    }

    private void validarMensagemDeSucesso() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean mensagemVisivel = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".alert"), "Cadastro realizado com sucesso"
        ));

        assertTrue(mensagemVisivel, "A mensagem de sucesso não apareceu a tempo!");
    }
}