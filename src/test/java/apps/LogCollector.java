package apps;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.TestBase;

import java.io.IOException;
import java.util.List;

public class LogCollector extends TestBase {

    //WindowsDriver driverWinLC;
    //public LogCollector(WindowsDriver driverWinLC){ this.driverWinLC = driverWinLC; }

    public void get_LC_Picture(WindowsDriver driverWinLC) throws IOException {
        String title[] = driverWinLC.getTitle().split(" ");
        System.out.println("LogCollector screen");
        takeAppSnap(driverWinLC, title[0]);
    }

    public void launchFMinAdminMode(WindowsDriver driverLC) throws Exception {
        List<WebElement> elements=driverLC.findElementsByAccessibilityId("ListViewSubItem-0");
        System.out.println("List contains "+elements.size()+" applications below:");
        for (WebElement e:elements) {System.out.println(e.getText());}
        WebElement fm = driverLC.findElementByName("FileMaster");
        Actions actions = new Actions(driverLC);
        actions.contextClick(fm).perform();
        System.out.println("right click on FM");
        actions.moveToElement(driverLC.findElementByName("Start")).click().build().perform();
        driverLC.findElementByName("Yes").click();
        System.out.println("started FM in Admin mode...");
        Thread.sleep(3000);
    }

    public String appTitleFM(WindowsDriver driverFM) throws IOException {
        String title[] = driverFM.getTitle().split(" ");
        System.out.println(driverFM.getTitle());
        System.out.println("FileMaster screen");
        takeAppSnap(driverFM, title[0]);
        return title[0];
    }

    public void launchPolluxGatewayInAdminMode(WindowsDriver driverLC) throws Exception {
        WebElement pg = driverLC.findElementByName("PolluxGateway");
        Actions actions = new Actions(driverLC);
        if(pg.isDisplayed()) {
            actions.contextClick(pg).perform();
            System.out.println("right click on PolluxGateway");
            actions.moveToElement(driverLC.findElementByName("Start")).click().build().perform();
            driverLC.findElementByName("Yes").click();
            System.out.println("started PolluxGateway in Admin mode...");
        }else {  // ==>> NOT all apps are visible inside the LC window
            for (int i = 0; i < 30; i++) {
                driverLC.findElementByName("Line down").click();
                if(pg.isDisplayed()){
                    driverLC.findElementByName("Line down").click();
                    break;}
            }
            //driverLC.findElementByAccessibilityId("DownPageButton").click();
            actions.contextClick(pg).perform();
            System.out.println("right click on PolluxGateway");
            actions.moveToElement(driverLC.findElementByName("Start")).click().build().perform();
            driverLC.findElementByName("Yes").click();
            System.out.println("started PolluxGateway in Admin mode...");
        }
        Thread.sleep(2000);
    }

    public String appTitlePG(WindowsDriver driverPolluxGW) throws Exception {
        String title[] = driverPolluxGW.getTitle().split(" ");
        System.out.println(driverPolluxGW.getTitle());
        System.out.println("PolluxGW screen");
        driverPolluxGW.findElementByName("1 & OnDemand").click();
        takeAppSnap(driverPolluxGW, title[0]);
        return title[0];
    }

    public void launchRFASInAdminMode(WindowsDriver driverLC) throws Exception {
        WebElement RFAS = driverLC.findElementByName("RFAS");
        Actions actions = new Actions(driverLC);
        if(RFAS.isDisplayed()){     // ==>> all apps are visible inside the LC window
            actions.contextClick(RFAS).perform();
            System.out.println("right click on RFAS");
            actions.moveToElement(driverLC.findElementByName("Start")).click().build().perform();
            driverLC.findElementByName("Yes").click();
            System.out.println("started RFAS in Admin mode...");
        }else {  // ==>> NOT all apps are visible inside the LC window
            for (int i = 0; i < 30; i++) {
                driverLC.findElementByName("Line down").click();
                if(RFAS.isDisplayed()){
                    driverLC.findElementByName("Line down").click();
                    break;}
            }
            //driverLC.findElementByAccessibilityId("DownPageButton").click();
            actions.contextClick(RFAS).perform();
            System.out.println("right click on RFAS");
            actions.moveToElement(driverLC.findElementByName("Start")).click().build().perform();
            driverLC.findElementByName("Yes").click();
            System.out.println("started RFAS in Admin mode...");
        }
        Thread.sleep(2000);
    }

}
