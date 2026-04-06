package br.com.vyandra.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "email")
    private WebElement campoEmail;

    @FindBy(id = "password")
    private WebElement campoSenha;

    @FindBy(css = "button[data-testid='entrar']")
    private WebElement botaoEntrar;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void acessarPagina() {
        driver.get("https://front.serverest.dev/login");
    }

    public void realizarLogin(String email, String senha) {
        campoEmail.sendKeys(email);
        campoSenha.sendKeys(senha);
        botaoEntrar.click();
    }
}