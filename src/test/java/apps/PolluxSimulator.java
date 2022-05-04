package apps;

import core.excelUserData;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.TestBase;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PolluxSimulator extends TestBase {

    List<Map<String,String>> pickDateToReproduceFromFile;

    public String get_PS_Picture(WindowsDriver driverPS) throws IOException {
        String title[] = driverPS.getTitle().split(" ");
        System.out.println("PolluxSimulator screen");
        WebElement setup = driverPS.findElementByName("Setup");
        setup.click();
        takeAppSnap(driverPS, title[0]);
        return title[0];
    }

    public void polluxSendProcess(WindowsDriver driverPS) throws Exception {
        pickDateToReproduceFromFile = excelUserData.getPolluxGWDataFromFile();
        String[] title = driverPS.getTitle().split(" ");
        WebElement polluxSend = driverPS.findElementByName("Pollux Send");
        polluxSend.click();
        //WebElement path = driverPS.findElementByName("Path:");
        WebElement browse = driverPS.findElementByAccessibilityId("1007");
        browse.click();
        driverPS.findElementByName("OK").click();
        WebElement selectAll = driverPS.findElementByAccessibilityId("1009");  //select all button
        selectAll.click();
        WebElement sendToGateway = driverPS.findElementByAccessibilityId("1041");
        Thread.sleep(500);
        if(!sendToGateway.isSelected()){sendToGateway.click();}
        //getElementCoordinates(driverPS.findElementByName("Date:"));
        Actions a = new Actions(driverPS);
        System.out.println("Selecting daily date");
        a.moveToElement(driverPS.findElementByName("Date:"),70,0).click().
                sendKeys(pickDateToReproduceFromFile.get(0).get("StartMonth")).build().perform();
        a.moveToElement(driverPS.findElementByName("Date:"),100,0).click().
                sendKeys(pickDateToReproduceFromFile.get(0).get("StartDay")).build().perform();
        a.moveToElement(driverPS.findElementByName("Date:"),120,0).click().
                sendKeys(pickDateToReproduceFromFile.get(0).get("StartYear")).build().perform();
        WebElement reload = driverPS.findElementByName("Reload");
        reload.click();
        selectAll.click();
        WebElement send = driverPS.findElementByAccessibilityId("1012");
        send.click();
        WebElement yesOption = driverPS.findElementByName("Yes");
        yesOption.click();
        System.out.println("Be patient and wait till the process ends (might take up to 15 minutes)");
        takeAppSnap(driverPS,title[0]);
        Thread.sleep(900000); //calculate 15 mins

    }

}
