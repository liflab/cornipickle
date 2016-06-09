package ca.uqac.lif.cornipickle;

import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
<<<<<<< HEAD:Source/ServerTest/src/ca/uqac/lif/cornipickle/WebDriverTest.java
@Ignore
public class WebDriverTest{
=======

@Ignore
public class WebDriverFirefox{
>>>>>>> 2a5ddbd979bcb54dfdc62a2f023b19fc4d201867:Source/ServerTest/src/ca/uqac/lif/cornipickle/WebDriverFirefox.java

    
    public static void main(String args[]) throws InterruptedException{
    	WebDriver driver = new FirefoxDriver();
        test1(driver);
        test2(driver);
        test3(driver);
        test5(driver);
        test8(driver);
        test9(driver);
        driver.close();
    }

    
    public static void test1(WebDriver driver) throws InterruptedException {

        try{

        driver.get("http://localhost:10101/examples/misaligned-elements.html");
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Thread.sleep(2000);


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
        Thread.sleep(2000);


        //When OK
        boolean marginLeftZero = listItem.getCssValue("margin-left").equals("0px");
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");

        boolean isOk = bpWitnessColor&&marginLeftZero;
        //System.out.println("When OK : "+isOk);


        //Toggle misalignment
        buttonToggleMisalignment.click();
        Thread.sleep(2000);


        //When not OK
        marginLeftZero = !listItem.getCssValue("margin-left").equals("0px");
        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");

        boolean isNotOk = bpWitnessColor&&marginLeftZero;
        //System.out.println("When not OK : "+isNotOk);

        //Assertion
        /*System.out.println("////////////////////////////////\n" +
                "TEST Example 1 -> " +
                (isOk&&isNotOk)+
                "\n////////////////////////////////\n" +
                "\n" +
                "\n");*/

        System.out.println("Test 1"+(isOk&&isNotOk));
        }
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }

    }


    
    public static void test2(WebDriver driver) throws InterruptedException {
        try{
        driver.get("http://localhost:10101/examples/overlapping-elements.html");
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));

        WebElement bpWitness = driver.findElement(By.id("bp_witness"));
        WebElement blueSquare = driver.findElement(By.id("blue"));

        //Add to cornipickle
        buttonAddToCornipickle.click();

        //Toggle misalignment
        blueSquare.click();
        Thread.sleep(2000);


        //When Overlapping
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");
        boolean isOk = bpWitnessColor;
        //System.out.println("When overlapping : "+isOk);



        //Move blue square such as squares don't overlap
        Actions action = new Actions(driver);
        action.clickAndHold(blueSquare).moveByOffset(50, 0).release().build().perform();
        Thread.sleep(2000);


        //When not OK

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");

        boolean isNotOk = bpWitnessColor;
        //System.out.println("When not overlapping : "+isNotOk);

        System.out.println("Test 2"+(isOk&&isNotOk));}


        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }

    }


    
    public static void test3(WebDriver driver) throws InterruptedException {

        try{
        driver.get("http://localhost:10101/examples/outside-window.html");
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));

        WebElement bpWitness = driver.findElement(By.id("bp_witness"));
        WebElement blueSquare = driver.findElement(By.id("blue"));

        //Add to cornipickle
        buttonAddToCornipickle.click();

        //Toggle misalignment
        blueSquare.click();
        Thread.sleep(2000);


        //When Overlapping
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");
        boolean isOk = bpWitnessColor;
        //System.out.println("When overlapping : "+isOk);



        //Move blue square such as squares don't overlap
        Actions action = new Actions(driver);
        action.clickAndHold(blueSquare).moveByOffset(0, -100).release().build().perform();
        Thread.sleep(2000);


        //When not OK

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");

        boolean isNotOk = bpWitnessColor;
//        System.out.println("When not overlapping : "+isNotOk);

        //Assertion
        System.out.println("Test 3"+(isOk&&isNotOk));}
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }
    }





    @Ignore
    
    public void test4(WebDriver driver) throws InterruptedException {
        try{

        driver.get("http://localhost:10101/examples/mojibake.html");
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));
        WebElement buttonToggleMojibake = driver.findElement(By.id("misalign-toggle"));

        WebElement bpWitness = driver.findElement(By.id("bp_witness"));


        //Add to cornipickle
        buttonAddToCornipickle.click();

        //Toggle misalignment
        buttonToggleMojibake.click();
        Thread.sleep(2000);


        //When OK
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");
        boolean isOk = bpWitnessColor;
        //System.out.println("When OK : "+isOk);


        //Toggle misalignment
        buttonToggleMojibake.click();
        Thread.sleep(2000);


        //When not OK

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");

        boolean isNotOk = bpWitnessColor;
        //System.out.println("When not OK : "+isNotOk);

        //Assertion
        System.out.println("Test 4"+(isOk&&isNotOk));}
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }

    }




    
    public static void test5(WebDriver driver) throws InterruptedException {
        try{

        driver.get("http://localhost:10101/examples/wrong-text.html");
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));
        WebElement buttonToggleName = driver.findElement(By.id("misalign-toggle"));
        WebElement content = driver.findElement(By.id("contents"));
        WebElement bpWitness = driver.findElement(By.id("bp_witness"));


        //Add to cornipickle
        buttonAddToCornipickle.click();
        content.click();

        //Toggle misalignment
        buttonToggleName.click();
        Thread.sleep(2000);


        //When OK
        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");
        boolean isOk = bpWitnessColor;
        //System.out.println("When OK : "+isOk);


        //Toggle name
        buttonToggleName.click();
        Thread.sleep(2000);


        //When not OK
        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");

        boolean isNotOk = bpWitnessColor;
        //System.out.println("When not OK : "+isNotOk);

        //Assertion
        System.out.println("Test 5"+(isOk&&isNotOk));}
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }


    }


    
    public static void test8(WebDriver driver) throws InterruptedException {
        try{
        driver.get("http://localhost:10101/examples/square-order.html");
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));

        WebElement bpWitness = driver.findElement(By.id("bp_witness"));
        WebElement content = driver.findElement(By.id("contents"));
        WebElement blueSquare = driver.findElement(By.id("blue"));
        WebElement redSquare = driver.findElement(By.id("red"));


        content.click();
        Thread.sleep(2000);


        //Before

        blueSquare.click();
        redSquare.click();

        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");
        boolean before = bpWitnessColor;
        //System.out.println("Before : "+ before);


        //Add to cornipickle
        buttonAddToCornipickle.click();


        //After
        blueSquare.click();
        redSquare.click();

        Thread.sleep(2000);

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");
        boolean after = bpWitnessColor;
        //System.out.println("After : "+ after);


        //Assertion
        System.out.println("Test 8"+(before&&after));}
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }

    }


    
    public static void test9(WebDriver driver) throws InterruptedException {
        try{


        driver.get("http://localhost:10101/examples/square-order-next.html");
        Thread.sleep(2000);


        //WebElements
        WebElement buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));

        WebElement bpWitness = driver.findElement(By.id("bp_witness"));
        WebElement content = driver.findElement(By.id("contents"));
        WebElement blueSquare = driver.findElement(By.id("blue"));
        WebElement redSquare = driver.findElement(By.id("red"));
        WebElement yellowSquare = driver.findElement(By.id("yellow"));


        content.click();
        Thread.sleep(2000);


        //Before

        blueSquare.click();
        Thread.sleep(2000);
        yellowSquare.click();
        Thread.sleep(2000);
        redSquare.click();
        Thread.sleep(2000);

        boolean bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(0, 128, 0, 1)");
        boolean before = bpWitnessColor;
        //System.out.println("Before : "+ before);


        //Add to cornipickle
        buttonAddToCornipickle.click();


        //After
        blueSquare.click();
        Thread.sleep(2000);
        yellowSquare.click();
        Thread.sleep(2000);
        redSquare.click();

        Thread.sleep(2000);

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 0, 0, 1)");
        boolean after = bpWitnessColor;
        //System.out.println("After : "+ after);




        driver.get("http://localhost:10101/examples/square-order-next.html");

        Thread.sleep(2000);


         buttonAddToCornipickle = driver.findElement(By.id("to-cornipickle"));

         bpWitness = driver.findElement(By.id("bp_witness"));
         content = driver.findElement(By.id("contents"));
         blueSquare = driver.findElement(By.id("blue"));
         redSquare = driver.findElement(By.id("red"));
         yellowSquare = driver.findElement(By.id("yellow"));


        buttonAddToCornipickle.click();
        Thread.sleep(2000);

        //After2
        blueSquare.click();
        Thread.sleep(2000);
        redSquare.click();
        Thread.sleep(2000);
        yellowSquare.click();

        Thread.sleep(2000);

        bpWitnessColor = bpWitness.getCssValue("background-color").equals("rgba(255, 255, 255, 1)");
        boolean after2 = bpWitnessColor;
        //System.out.println("After2 : "+ after2);

        //Assertion
        System.out.println(before);
        System.out.println(after);
        System.out.println(after2);
        System.out.println("Test 9"+(before&&after&&after2));}
        catch (NoSuchElementException nsee){
            System.out.println("Element non trouvé");
            assert(false);
        }
    }
}

