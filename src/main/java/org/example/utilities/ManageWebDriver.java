package org.example.utilities;

//import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ManageWebDriver {

    private WebDriver driver;
    private Properties configProperties;

    public void initializeDriver(){
        configProperties=PropertiesFileReader.readPropertiesFile(ResourcePaths.configFilePath);
        //loadConfigProperties();
        String browserName=configProperties.getProperty("browser");
        switch (browserName){
            case "chrome"-> {
                //WebDriverManager.chromedriver().clearDriverCache().setup();
                ChromeOptions chromeOptions=new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver=new ChromeDriver(chromeOptions);
                //driver.manage().window().maximize();
            }
            case "firefox"->{
                //WebDriverManager.firefoxdriver().setup();
                driver=new FirefoxDriver();
                driver.manage().window().maximize();
            }
            default -> System.out.println("Invalid browser name : ".concat(browserName));
        }
    }

    public WebDriver getDriver(){
        return driver;
    }

    public void openUrl(String url){
        System.out.println(configProperties.getProperty(url));
        driver.get(configProperties.getProperty(url));
    }

    public void quit(){
        getDriver().quit();
    }

    private void loadConfigProperties(){
        try(FileInputStream configFIS=new FileInputStream(ResourcePaths.configFilePath)) {
            configProperties=new Properties();
            configProperties.load(configFIS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
