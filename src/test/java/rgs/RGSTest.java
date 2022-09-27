package rgs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class RGSTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "http://www.rgs.ru";
        driver.get(baseUrl);
    }

    @Test
    public void test() {
        WebElement companies = driver.findElement(By.xpath("//a[text()='Компаниям' and contains (@href,'companies')]"));
        companies.click();
        boolean iframeFlag = isElement((By.xpath("//iframe[@id='fl-616371']")));
        if(iframeFlag) {
            WebElement iframe = driver.findElement(By.xpath("//iframe[@id='fl-616371']"));
            driver.switchTo().frame(iframe);
            driver.findElement(By.xpath(".//div[contains(@class,'close js-collapse-login')]")).click();
            driver.switchTo().parentFrame();
        }
        WebElement health = driver.findElement(By.xpath("//span[text()='Здоровье' and contains(@class,'padding')]"));
        health.click();
        WebElement healthInsurance = driver.findElement(By.xpath("//a[text()='Добровольное медицинское страхование' and contains (@href,'dobrovolnoe-meditsinskoe-strakhovanie')]"));
        healthInsurance.click();
        WebElement titleHealthInsurance = driver.findElement(By.xpath("//h1[contains (@class,'title word-breaking title')]"));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Добровольное медицинское страхование", titleHealthInsurance.getText());
        WebElement submitApplication = driver.findElement(By.xpath("//span[text()='Отправить заявку']"));
        submitApplication.click();
        WebElement titleToIssuePolicy = driver.findElement(By.xpath("//div[@class='mediator-wrapper sectionForm']//h2[contains(@class,'section-basic')]"));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Оперативно перезвоним\n" + "для оформления полиса", titleToIssuePolicy.getText());
        WebElement fieldUserName = driver.findElement(By.xpath("//input[@name='userName']"));
        fieldUserName.sendKeys("Иванов Иван Иванович");
        WebElement fieldUserTel = driver.findElement(By.xpath("//input[@name='userTel']"));
        fieldUserTel.sendKeys("9034567895");
        WebElement fieldUserEmail = driver.findElement(By.xpath("//input[@name='userEmail']"));
        fieldUserEmail.sendKeys("qwertyqwerty");
        WebElement fieldUserAddress = driver.findElement(By.xpath("//input[@placeholder='Введите' and @class='vue-dadata__input']"));
        fieldUserAddress.sendKeys("qwertyqwerty");
        Assert.assertEquals("Поле было заполнено некорректно",fieldUserName.getAttribute("value"),"Иванов Иван Иванович");
        Assert.assertEquals("Поле было заполнено некорректно",fieldUserTel.getAttribute("value"),"+7 (903) 456-7895");
        Assert.assertEquals("Поле было заполнено некорректно",fieldUserEmail.getAttribute("value"),"qwertyqwerty");
        Assert.assertEquals("Поле было заполнено некорректно",fieldUserAddress.getAttribute("value"),"qwertyqwerty");
        WebElement checkboxPersonalData = driver.findElement(By.xpath("//div[@class='form__checkbox']//input"));
        scrollToElementJs(checkboxPersonalData);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",checkboxPersonalData);
        WebElement buttonContactMe = driver.findElement(By.xpath("//button[text()='Свяжитесь со мной']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",buttonContactMe);
        WebElement textYourMail =  driver.findElement(By.xpath("//input[@name='userEmail']//..//..//span[@class='input__error text--small']"));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Введите корректный адрес электронной почты", textYourMail.getText());
    }

    /**
     * Скрол до элемента на js коде
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Проверяет наличие элемента на странице
     * @author Алехнович Александр
     */
    public boolean isElement(By element) {
        try {
            driver.findElement(element);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @After
    public void after() {
        driver.quit();
    }
}
