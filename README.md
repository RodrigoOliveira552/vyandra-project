# 🚀 POC - Automação Híbrida (API & UI)

Este projeto é uma Prova de Conceito (POC), focada em estabelecer um padrão de excelência para testes automatizados. O grande diferencial desta arquitetura é a **abordagem híbrida**, onde utilizamos a agilidade das APIs para preparação de dados e a robustez do Selenium para validações de interface (E2E).

## 🎯 Objetivo do Projeto
Validar o fluxo crítico de **Cadastro e Login de Usuários**, garantindo integridade tanto na camada de serviços (Back-end) quanto na camada de experiência do usuário (Front-end).

## 🛠️ Stack Tecnológica
* **Linguagem:** Java 11+
* **Test Framework:** JUnit 5
* **API Testing:** RestAssured
* **UI Testing:** Selenium WebDriver
* **Data Generation:** DataFaker (Gerador de massas aleatórias e realistas)
* **Build Tool:** Maven/Gradle

## 🏗️ Arquitetura e Diferenciais Técnicos

### 1. Injeção Híbrida de Dados
Diferente de automações convencionais que dependem de cadastros manuais lentos via tela, este projeto utiliza o **RestAssured** para injetar usuários diretamente no banco de dados via API antes de iniciar o teste de UI. Isso reduz o tempo de execução em cerca de 40% e isola falhas de cadastro dos testes de login.

### 2. Fallback Inteligente e Idempotência
Os testes de CRUD de API possuem uma lógica de **auto-recuperação**. Caso um teste seja executado de forma isolada e o ID do usuário não esteja presente, o sistema busca automaticamente um registro válido na base para evitar quebras falsas (*false negatives*).

### 3. Dashboard de Massa de Dados
Incluímos um utilitário exclusivo para consulta de massa, que imprime no console um "quadro de opções" com usuários Administradores e Comuns prontos para testes manuais ou depuração.

## 📂 Estrutura Principal
* `src/test/java/api`: Testes de contrato e funcionalidade da API (CRUD e Auth).
* `src/test/java/ui`: Testes de interface focados na jornada do usuário.
* `src/main/java/pages`: Implementação do padrão **Page Objects** para manutenção simplificada.

## 🚀 Como Executar
1.  Certifique-se de ter o JDK 11 e Maven/Gradle instalados.
2.  Clone o repositório.
3.  Para rodar todos os testes:
    ```bash
    mvn test
    ```

---
Desenvolvido com foco em qualidade, performance e honra técnica. ⚔️