package ca.uqac.lif.cornipickle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by paull on 26/05/2016.
 */
public class WebDriverTestExample1 {
    public static void main(String args[]) throws InterruptedException {

        //Open Firefox
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:10101/examples/misaligned-elements.html");

        Thread.sleep(500);


        //WebElements

        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));
        WebElement buttonToggleMisalignment = driver.findElement(By.id("misalign-toggle"));
        WebElement listItem = driver.findElement(By.id("second-menu"));
        WebElement bpWitness = driver.findElement(By.id("bp_witness"));


        //Add to cornipickle
        buttonAddToCornipickle.click();
        listItem.click();

        //Toggle misalignment
        buttonToggleMisalignment.click();
        Thread.sleep(500);


        //When OK
        boolean marginLeftZero = listItem.getCssValue("margin-left").equals("0px");
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");

        boolean isOk = bpWitnessColor&&marginLeftZero;
        System.out.println("When OK : "+isOk);


        //Toggle misalignment
        buttonToggleMisalignment.click();
        Thread.sleep(500);


        //When not OK
        marginLeftZero = !listItem.getCssValue("margin-left").equals("0px");
        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");

        boolean isNotOk = bpWitnessColor&&marginLeftZero;
        System.out.println("When not OK : "+isNotOk);

        //Assertion
        System.out.println("\n\n////////////////////////////////\n" +
                "TEST Example 1 -> " +
                (isOk&&isNotOk)+
                "\n////////////////////////////////");


        //Close Firefox
        driver.close();
    }
}
