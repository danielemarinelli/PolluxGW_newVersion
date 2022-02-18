package apps;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.TestBase;

import java.io.IOException;
import java.util.List;

public class SystemView extends TestBase {

    WindowsDriver driverWinSV;
    public SystemView(WindowsDriver driverWinSV){ this.driverWinSV = driverWinSV; }

    public void displaySystemViewLogs() throws Exception {
        driverWinSV.findElementByName("Log type").click();
        Thread.sleep(1000);
        driverWinSV.findElementByAccessibilityId("DropDown").click();
        scrollDownTillElementIsVisible(driverWinSV.findElementByName("MIXER"));
        Actions a = new Actions(driverWinSV);
        WebElement mixer= driverWinSV.findElementByName("MIXER");
        a.moveToElement(mixer).click().build().perform();
        //driverWinSV.findElementByAccessibilityId("1002").click();  //untick the ‘show notification'
        driverWinSV.findElementByName("Read").click();
        System.out.println("DISPLAY MIXER LOGS....");
        String[] title = driverWinSV.getTitle().split(" ");
        Thread.sleep(3000);
    }

    public String mixerSnapShotWithOldVersion() throws Exception {
        String[] title = driverWinSV.getTitle().split(" ");
        WebElement logType = driverWinSV.findElementByName("Log type");
        Actions a = new Actions(driverWinSV);
        a.moveToElement(logType,100,170).click().build().perform();
        Thread.sleep(3000);
        takeAppSnap(driverWinSV, title[0]);
        return title[0];
    }


    public String mixerSnapShotWithNewVersion() throws Exception {
        String[] title = driverWinSV.getTitle().split(" ");
        WebElement logType = driverWinSV.findElementByName("Log type");
        //System.out.println("y "+logType.getLocation().getY());
        //System.out.println("x "+logType.getLocation().getX());
        Actions a = new Actions(driverWinSV);
        a.moveToElement(logType,100,140).click().build().perform();
        a.moveToElement(logType,100,140).click().build().perform();
        Thread.sleep(500);
        a.moveToElement(logType,100,170).click().build().perform();
        Thread.sleep(1000);
        takeAppSnap(driverWinSV, title[0]);
        return title[0];
    }
}
