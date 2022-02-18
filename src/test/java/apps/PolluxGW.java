package apps;

import io.appium.java_client.windows.WindowsDriver;
import tests.TestBase;


public class PolluxGW extends TestBase {


    public String getNewVersionPolluxGW(WindowsDriver driverWinPolluxGW) throws Exception {
        System.out.println("...New Version PolluxGW...");
        String[] title = driverWinPolluxGW.getTitle().split(" ");
        Thread.sleep(2000);

        return title[3];
    }

}
