package com.vqn.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by vqnguyen on 7/14/2017.
 */
@Service
public class CrawlerService {
    String cookie;

    @Value("${webdriver.chrome.driver}")
    String webdriverPath;

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public String getCookieFromSesimi() throws InterruptedException {
  /*      ChromeOptions chromeOptions = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver",webdriverPath);
        chromeOptions.addArguments("headless");*/
        System.out.println("Start crawling ...");
        System.setProperty("webdriver.gecko.driver",webdriverPath);
        DesiredCapabilities capabilities=DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        FirefoxDriver driver = new FirefoxDriver(capabilities);
        driver.get("http://www.simsimi.com/FirebaseAuthPage?ref=ChatSettings");
        System.out.println("1. Open url");
        WebDriverWait wait = new WebDriverWait(driver, 100);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("firebaseui-list-item")));

        driver.findElement(By.tagName("button")).click();
        System.out.println("2. Login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type=email]")));
        driver.findElement(By.cssSelector("input[type=email]")).sendKeys("vqn.vanquocnguyen@gmail.com");
        driver.findElement(By.cssSelector("div[role=button]")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type=password]")));
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("input[type=password]")).sendKeys("vanlang12");
        driver.findElement(By.cssSelector("div[role=button]")).click();
        Thread.sleep(100);

        System.out.println("3. Get cookie");

        new WebDriverWait(driver, 60)
                .ignoring(NoAlertPresentException.class)
                .until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        driver.manage().getCookies().forEach(c -> {
            if ( c.getName().equals("dotcom_session_key")) {
                setCookie(c.toString());
            }
        });
        driver.close();
        System.out.println("Finish crawling!");
        return cookie;
    }

    @Scheduled(fixedRate = 500000)
    public void autoGetCookie() throws IOException, InterruptedException {
        System.out.println("Getting cooking by schedule");
        //noinspection deprecation
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://simsimi.com/getRealtimeReq?lc=en&ft=1&normalProb=3&reqText=test&status=W&talkCnt=0");
        httpGet.setHeader("Cookie", cookie);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() != 200) {
            getCookieFromSesimi();
        }
    }
}
