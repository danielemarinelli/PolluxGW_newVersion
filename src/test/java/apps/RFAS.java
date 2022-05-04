package apps;

import core.excelUserData;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.TestBase;
import java.util.List;
import java.util.Map;

public class RFAS extends TestBase {

    WindowsDriver driverWinRFAS;
    List<Map<String,String>> allDataFromFile;

    public RFAS(WindowsDriver driverWinRFAS){this.driverWinRFAS = driverWinRFAS;}

    public int setAuthorization(WindowsDriver driverWinRFAS) throws Exception {
        System.out.println("Click on Log Client TAB of the RFAS app");
        driverWinRFAS.findElementByName("Log Clients").click();
        //List num_auth_cli = driverWinRFAS.findElementsByAccessibilityId("ListViewSubItem-0");
        Thread.sleep(10000);
        List num_auth_cli = driverWinRFAS.findElementsByAccessibilityId("LCAuthorizedClientSortableListView");
        List unAuthCli = driverWinRFAS.findElementsByAccessibilityId("LCUnauthorizedClientSortableListView");
        allDataFromFile = excelUserData.getDataFromExcelFile();
        String clientID = allDataFromFile.get(0).get("ID");
        WebElement rowClient = driverWinRFAS.findElementByName(clientID);
        if(rowClient.getLocation().getY()>400){
        //if(unAuthCli.size()==1){
            System.out.println("Client Authorization process started");
            rowClient.click();
            Actions actions = new Actions(driverWinRFAS);
            actions.moveToElement(rowClient).contextClick().build().perform();
            driverWinRFAS.findElementByName("Authorize Client(s)").click();
            driverWinRFAS.findElementByName("Yes").click();
        }else{System.out.println("Client is already authorized");}
        Thread.sleep(2000);
        //for (int i = 0; i < 2; i++) { driverWinRFAS.findElementByName("Yes").click(); }
        System.out.println("Authorized Client RFAS...");
        String[] title = driverWinRFAS.getTitle().split(" ");
        takeAppSnap(driverWinRFAS, title[0]);
        return num_auth_cli.size();
    }

    public void tearDownRFAS() {
        driverWinRFAS.close();
        for (int i = 0; i < 2; i++) { driverWinRFAS.findElementByName("Yes").click(); }
    }

    public int checkIfClientIsAuthorization(WindowsDriver driverWinRFAS) throws Exception {
        driverWinRFAS.findElementByName("Log Clients").click();
        System.out.println("Check Client Authorization");
        Thread.sleep(8000);
        List num_auth_cli = driverWinRFAS.findElementsByAccessibilityId("LCAuthorizedClientSortableListView");
        String[] title = driverWinRFAS.getTitle().split(" ");
        takeAppSnap(driverWinRFAS, title[0]);
        Thread.sleep(1000);
        return num_auth_cli.size();
    }
}
