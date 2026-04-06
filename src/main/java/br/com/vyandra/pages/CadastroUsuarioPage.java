package br.com.vyandra.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CadastroUsuarioPage {
    private WebDriver driver;

    @FindBy(id = "nome")
    private WebElement campoNome;

    @FindBy(id = "email")
    private WebElement campoEmail;

    @FindBy(id = "password")
    private WebElement campoSenha;

    @FindBy(id = "administrador")
    private WebElement checkboxAdmin;

    @FindBy(css = "button[data-testid='cadastrar']")
    private WebElement botaoCadastrar;

    public CadastroUsuarioPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void acessarPagina() {
        driver.get("https://front.serverest.dev/cadastrarusuarios");
    }

    public void preencherDados(String nome, String email, String senha) {
        campoNome.sendKeys(nome);
        campoEmail.sendKeys(email);
        campoSenha.sendKeys(senha);
    }

    public void marcarComoAdmin() {
        if (!checkboxAdmin.isSelected()) {
            checkboxAdmin.click();
        }
    }

    public void submeterCadastro() {
        botaoCadastrar.click();
    }
}