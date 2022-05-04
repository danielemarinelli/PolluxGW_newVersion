package tests;

import com.relevantcodes.extentreports.LogStatus;
import core.TestReporter;
import core.email;
import core.excelUserData;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TestBase {

    private WindowsDriver driverWinPolluxGW=null;
    private WindowsDriver driverWinMerge=null;
    private WindowsDriver driverWinLC=null;
    private WindowsDriver driverWinFM=null;
    private WindowsDriver driverWinRFAS=null;
    protected WindowsDriver driverWinPub=null;
    public WindowsDriver driverWinGA=null;
    private WindowsDriver driverWinSA=null;
    private WindowsDriver driverWinSV=null;
    private WindowsDriver driverUnitamSW=null;
    private WindowsDriver driverWinReproductionAgent;
    private WindowsDriver driverWinPublisher=null;
    private WindowsDriver driverWinPS=null;
    private TestReporter reporter;
    String date = null;
    String RAWinHandleHex = null;
    String FMWinHandleHex = null;
    String LCWinHandleHex = null;
    String UnitamSWHandleHex = null;
    List<Map<String,String>> appTitlesFromFile;


    //@BeforeSuite
    public final void openWinMergeApp() {
        DesiredCapabilities WM = new DesiredCapabilities();
        WM.setCapability("app", "C:\\Program Files\\WinMerge\\WinMergeU.exe");
        WM.setCapability("platformName", "Windows_WM");
        WM.setCapability("deviceName", "WindowsPC_WM");
        try {
        driverWinMerge = new WindowsDriver(new URL("http://127.0.0.1:4723/"), WM);
        driverWinMerge.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Assert.assertNotNull(driverWinMerge,"WinMerge App didn't open");
            Set<String> windowsWinMerge = driverWinMerge.getWindowHandles();
            System.out.println("WinMERGE -----> "+windowsWinMerge);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite  //(groups={"new_PolluxGateway","old_PolluxGateway"})
    public final void searchLC()  {
        try{
            appTitlesFromFile = excelUserData.getAppFoldersFromFile();
            DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
            desktopCapabilities.setCapability("platformName", "Windows");
            desktopCapabilities.setCapability("deviceName", "WindowsPC");
            desktopCapabilities.setCapability("app", "Root");
            WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
            DesiredCapabilities LCCapabilities = new DesiredCapabilities();
            LCCapabilities.setCapability("platformName", "Windows");
            LCCapabilities.setCapability("deviceName", "WindowsPC");
            WebElement lc = desktopSession.findElementByName(appTitlesFromFile.get(0).get("LogCollectorTitle"));
            //List<WebElement> lc = desktopSession.findElementsByName("LogCollector - Ver. 5.23.2247.45 Copyright (C) Media Instruments SA 2010-2021. All Rights Reserved.");
            //if (lc.size() == 1) {
                //String LCWinHandleStr = lc.get(0).getAttribute("NativeWindowHandle");
                String LCWinHandleStr = lc.getAttribute("NativeWindowHandle");
                int LCWinHandleInt = Integer.parseInt(LCWinHandleStr);
                String LCWinHandleHex = Integer.toHexString(LCWinHandleInt);
                LCCapabilities.setCapability("appTopLevelWindow", LCWinHandleHex);
                System.out.println("LC Handle is: " + LCWinHandleHex);
            //}else{System.out.println("Something went wrong opening LC in Admin mode....");}
            driverWinLC = new WindowsDriver(new URL("http://127.0.0.1:4723/"), LCCapabilities);
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong opening LC in Admin mode....");
        }
    }

    public final void setUpRFAS()  {
        try {
        DesiredCapabilities RFAS = new DesiredCapabilities();
        RFAS.setCapability("app","C:\\UNITAM SW\\RFAS.exe");
        RFAS.setCapability("platformName","Windows_RFAS");
        RFAS.setCapability("deviceName","WindowsPC_RFAS");
        driverWinRFAS = new WindowsDriver(new URL("http://127.0.0.1:4723/"),RFAS);
        Assert.assertNotNull(driverWinRFAS,"RFAS didn't open");
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    Set<String> windowsRFAS = driverWinRFAS.getWindowHandles();
        System.out.println(windowsRFAS);
        driverWinRFAS.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public final void setUpFM()  {
        DesiredCapabilities FM = new DesiredCapabilities();
        FM.setCapability("app","C:\\UNITAM SW\\FileMaster.exe");
        FM.setCapability("platformName","Windows_FM");
        FM.setCapability("deviceName","WindowsPC_FM");
        try {
            driverWinFM = new WindowsDriver(new URL("http://127.0.0.1:4723/"),FM);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Set<String> windowsFM = driverWinFM.getWindowHandles();
        System.out.println(windowsFM);
        driverWinFM.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Assert.assertNotNull(driverWinFM,"File Master didn't open");

    }

    public final void attachDriverToFM()  {
        try{
            appTitlesFromFile = excelUserData.getAppFoldersFromFile();
            DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
            desktopCapabilities.setCapability("platformName", "Windows");
            desktopCapabilities.setCapability("deviceName", "WindowsPC");
            desktopCapabilities.setCapability("app", "Root");
            WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
            DesiredCapabilities FMCapabilities = new DesiredCapabilities();
            FMCapabilities.setCapability("platformName", "Windows");
            FMCapabilities.setCapability("deviceName", "WindowsPC");
            WebElement fm = desktopSession.findElementByName(appTitlesFromFile.get(0).get("FileMasterTitle"));
            //List<WebElement> fm = desktopSession.findElementsByName("FileMaster - Ver. 5.23.2245.49 Copyright (C) Media Instruments SA 2007. All rights reserved.");
            //if (fm.size() == 1) {
                //String FMWinHandleStr = fm.get(0).getAttribute("NativeWindowHandle");
                String FMWinHandleStr = fm.getAttribute("NativeWindowHandle");
                int FMWinHandleInt = Integer.parseInt(FMWinHandleStr);
                String FMWinHandleHex = Integer.toHexString(FMWinHandleInt);
                FMCapabilities.setCapability("appTopLevelWindow", FMWinHandleHex);
                System.out.println("FM Handle is: " + FMWinHandleHex);
            //}else{System.out.println("Something went wrong opening FM in Admin mode....");}

    driverWinFM = new WindowsDriver(new URL("http://127.0.0.1:4723/"), FMCapabilities);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Something went wrong opening FM in Admin mode....");
        }
    }

    public final void attachDriverToOldPolluxGatewayVersion() {
        try{
        appTitlesFromFile = excelUserData.getAppFoldersFromFile();
        DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
        desktopCapabilities.setCapability("platformName", "Windows");
        desktopCapabilities.setCapability("deviceName", "WindowsPC");
        desktopCapabilities.setCapability("app", "Root");
        WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
        DesiredCapabilities PGCapabilities = new DesiredCapabilities();
        PGCapabilities.setCapability("platformName", "Windows");
        PGCapabilities.setCapability("deviceName", "WindowsPC");
        WebElement pg = desktopSession.findElementByName(appTitlesFromFile.get(0).get("OldPolluxGatewayTitle"));
            String PGWinHandleStr = pg.getAttribute("NativeWindowHandle");
            int PGWinHandleInt = Integer.parseInt(PGWinHandleStr);
            String PGWinHandleHex = Integer.toHexString(PGWinHandleInt);
            PGCapabilities.setCapability("appTopLevelWindow", PGWinHandleHex);
            System.out.println("PG Handle is: " + PGWinHandleHex);
        driverWinPolluxGW = new WindowsDriver(new URL("http://127.0.0.1:4723/"), PGCapabilities);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Something went wrong opening PolluxGW old version in Admin mode....");
        }
    }

    public final void attachDriverToNewPolluxGatewayVersion() {
        try{
            appTitlesFromFile = excelUserData.getAppFoldersFromFile();
            DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
            desktopCapabilities.setCapability("platformName", "Windows");
            desktopCapabilities.setCapability("deviceName", "WindowsPC");
            desktopCapabilities.setCapability("app", "Root");
            WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
            DesiredCapabilities PGCapabilities = new DesiredCapabilities();
            PGCapabilities.setCapability("platformName", "Windows");
            PGCapabilities.setCapability("deviceName", "WindowsPC");
            WebElement pg = desktopSession.findElementByName(appTitlesFromFile.get(0).get("NewPolluxGatewayTitle"));
            String PGWinHandleStr = pg.getAttribute("NativeWindowHandle");
            int PGWinHandleInt = Integer.parseInt(PGWinHandleStr);
            String PGWinHandleHex = Integer.toHexString(PGWinHandleInt);
            PGCapabilities.setCapability("appTopLevelWindow", PGWinHandleHex);
            System.out.println("PG Handle is: " + PGWinHandleHex);
            driverWinPolluxGW = new WindowsDriver(new URL("http://127.0.0.1:4723/"), PGCapabilities);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong opening PolluxGW new version in Admin mode....");
        }
    }

    public final void setUpPolluxSimulator() {
        DesiredCapabilities PS = new DesiredCapabilities();
        PS.setCapability("app", "C:\\UNITAM SW\\TOOLS\\PolluxSimulator.exe");
        PS.setCapability("platformName", "Windows_PSimulator");
        PS.setCapability("deviceName", "WindowsPC_PSimulator");
        try {
            driverWinPS = new WindowsDriver(new URL("http://127.0.0.1:4723/"), PS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Set<String> windowsPS = driverWinPS.getWindowHandles();
        Assert.assertNotNull(driverWinPS,"PolluxSimulator didn't open");
        System.out.println("PS handler: "+windowsPS);
        driverWinPS.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public final void attachDriverToPublisher() {
        try{
            appTitlesFromFile = excelUserData.getAppFoldersFromFile();
            DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
            desktopCapabilities.setCapability("platformName", "Windows");
            desktopCapabilities.setCapability("deviceName", "WindowsPC");
            desktopCapabilities.setCapability("app", "Root");
            WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
            DesiredCapabilities PubCapabilities = new DesiredCapabilities();
            PubCapabilities.setCapability("platformName", "Windows");
            PubCapabilities.setCapability("deviceName", "WindowsPC");
            WebElement pub = desktopSession.findElementByName(appTitlesFromFile.get(0).get("PublisherTitle"));
            String PGWinHandleStr = pub.getAttribute("NativeWindowHandle");
            int PubWinHandleInt = Integer.parseInt(PGWinHandleStr);
            String PubWinHandleHex = Integer.toHexString(PubWinHandleInt);
            PubCapabilities.setCapability("appTopLevelWindow", PubWinHandleHex);
            System.out.println("Publisher Handle is: " + PubWinHandleHex);
            driverWinPublisher = new WindowsDriver(new URL("http://127.0.0.1:4723/"), PubCapabilities);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong opening Publisher in Admin mode....");
        }
    }


    public final void attachDriverToRFAS() {
        try{
            appTitlesFromFile = excelUserData.getAppFoldersFromFile();
        DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
        desktopCapabilities.setCapability("platformName", "Windows");
        desktopCapabilities.setCapability("deviceName", "WindowsPC");
        desktopCapabilities.setCapability("app", "Root");
        WindowsDriver desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desktopCapabilities);
        DesiredCapabilities RFASCapabilities = new DesiredCapabilities();
        RFASCapabilities.setCapability("platformName", "Windows");
        RFASCapabilities.setCapability("deviceName", "WindowsPC");
        WebElement RFAS = desktopSession.findElementByName(appTitlesFromFile.get(0).get("RFAS_Title"));
        //List<WebElement> RFAS = desktopSession.findElementsByName("RFAS - Ver. 5.23.2247.50 Copyright (C) Media Instruments SA 2010-2021. All Rights Reserved.");
        //if (RFAS.size() == 1) {
            //String RFASWinHandleStr = RFAS.get(0).getAttribute("NativeWindowHandle");
            String RFASWinHandleStr = RFAS.getAttribute("NativeWindowHandle");
            int RFASWinHandleInt = Integer.parseInt(RFASWinHandleStr);
            String RFASWinHandleHex = Integer.toHexString(RFASWinHandleInt);
            RFASCapabilities.setCapability("appTopLevelWindow", RFASWinHandleHex);
            System.out.println("RFAS Handle is: " + RFASWinHandleHex);
        //}else{System.out.println("Something went wrong opening RFAS in Admin mode....");}
        driverWinRFAS = new WindowsDriver(new URL("http://127.0.0.1:4723/"), RFASCapabilities);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong opening RFAS in Admin mode....");
        }
    }

    public final void setUpSystemView() {
        DesiredCapabilities SystemView = new DesiredCapabilities();
        SystemView.setCapability("app", "C:\\UNITAM SW\\TOOLS\\SystemView.exe");
        SystemView.setCapability("platformName", "Windows_SystemView");
        SystemView.setCapability("deviceName", "WindowsPC_SystemView");
        try {
            driverWinSV = new WindowsDriver(new URL("http://127.0.0.1:4723/"), SystemView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Set<String> windowsSV = driverWinSV.getWindowHandles();
        Assert.assertNotNull(driverWinSV,"SystemView didn't open");
        System.out.println("SV handler: "+windowsSV);
        driverWinSV.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public final void setUpReproductionAgent()  {
        DesiredCapabilities RA = new DesiredCapabilities();
        RA.setCapability("ms:waitForAppLaunch",15);
        RA.setCapability("app", "C:\\UNITAM SW\\ReproductionAgent.exe");
        RA.setCapability("platformName", "Windows_ReproductionAgent");
        RA.setCapability("deviceName", "WindowsPC_ReproductionAgent");
        try {
        driverWinReproductionAgent = new WindowsDriver(new URL("http://127.0.0.1:4723/"), RA);
        } catch (Exception e) {
        }
        //pressButtonF1();
        Set<String> windowsRA = getDriverRA().getWindowHandles();
        System.out.println("ReproductionAgent handler: "+windowsRA);
        Assert.assertNotNull(getDriverRA(),"ReproductionAgent didn't open");

    }

    public WindowsDriver getDriverPolluxGW() { return driverWinPolluxGW;}
    public WindowsDriver getDriverLC() { return driverWinLC;}
    public WindowsDriver getDriverFM() { return driverWinFM;}
    public WindowsDriver getDriverRFAS() { return driverWinRFAS;}
    public WindowsDriver getDriverWinMerge() { return driverWinMerge;}
    public WindowsDriver getDriverSV() { return driverWinSV;}
    public WindowsDriver getDriverRA() { return driverWinReproductionAgent;}
    public WindowsDriver getDriverPS(){ return driverWinPS;}
    public WindowsDriver getDriverPublisher() { return driverWinPublisher;}

        public void tearDownSA() {
        driverWinSA.close();
        driverWinSA.findElementByName("Yes").click();
    }


        public void tearDownFM() throws Exception {
        driverWinFM.close();
            for (int i = 0; i < 2; i++) { driverWinFM.findElementByName("Yes").click(); }
            Thread.sleep(5000);
        }

    @AfterMethod(groups={"new_PolluxGateway","old_PolluxGateway"})
    public void testDone(){System.out.println("##########Test is over, proceed with next one...");}

    @BeforeMethod(groups={"new_PolluxGateway","old_PolluxGateway"})
    public void startTest(){System.out.println("#########Test is starting...");}

    public void tearDownSV() { driverWinSV.findElementByAccessibilityId("Close").click(); }

    public void tearDownWinMerge() { driverWinMerge.close(); }


    //@AfterSuite
    public void tearDownRFAS() {
            driverWinRFAS.close();
            for (int i = 0; i < 2; i++) { driverWinRFAS.findElementByName("Yes").click(); }
    }

    public void tearDownReproAgent() throws InterruptedException {
        Thread.sleep(1000);
        driverWinReproductionAgent.close();
        driverWinReproductionAgent.findElementByName("Yes").click();
    }

    public void tearDownPolluxGW() {
        driverWinPolluxGW.close();
        for (int i = 0; i < 2; i++) { driverWinPolluxGW.findElementByName("Yes").click(); }
        driverWinPolluxGW.quit();
    }

    public void tearDownLC_WD(){
        System.out.println("---> closing LC & WatchDog");
        //getElementCoordinates(driverWinLC.findElementByName("File"));
        driverWinLC.findElementByName("File").click();
        Actions a = new Actions(driverWinLC);
        a.moveToElement(driverWinLC.findElementByName("File"),10,75).click().build().perform();
            for (int i = 0; i < 3; i++) { driverWinLC.findElementByName("Yes").click(); }
        System.out.println();
    }

    public WebElement waitTillButtonIsDisplayed(WindowsDriver wd, WebElement ele) {
            WebDriverWait wait = new WebDriverWait(wd, 40);
            return wait.until(ExpectedConditions.elementToBeClickable(ele));
          }

    public void rightMouseClick(WebElement ele,WindowsDriver driver){
        Actions actions = new Actions(driver);
        actions.contextClick(ele).perform();
    }

    public void switchToWindowRA() {
        Set<String> windows = driverWinReproductionAgent.getWindowHandles();
        for(String w : windows) {
            driverWinReproductionAgent.switchTo().window(w);
            //System.out.println("---> ReproductionAgent: "+w);
        }
    }

    public void switchToWindowPolluxGW() {
        Set<String> windows = driverWinPolluxGW.getWindowHandles();
        for(String w : windows) {
            driverWinPolluxGW.switchTo().window(w);
            //System.out.println("---> PolluxGW: "+w);
        }
    }

    public void switchToWindowGA(WindowsDriver driverWinGA) {
        Set<String> windows = driverWinGA.getWindowHandles();
        for(String w : windows) {
            driverWinGA.switchTo().window(w);
            System.out.println("---> GA: "+w);
        }
    }

    public void switchToWindowWinMerge(WindowsDriver driverWinMerge) {
        Set<String> windows = driverWinMerge.getWindowHandles();
        for(String w : windows) {
            driverWinMerge.switchTo().window(w);
            System.out.println("---> DriverWinMerge: "+w);
        }
    }

    public void switchToWindowSA(WindowsDriver driverWinSA) {
        Set<String> windows = driverWinSA.getWindowHandles();
        for(String w : windows) {
            driverWinSA.switchTo().window(w);
            System.out.println("---> SA window: "+w);
        }
    }

    public void switchToWindowPublisher(WindowsDriver driverWinPub) {
        Set<String> windows = driverWinPub.getWindowHandles();
        for(String w : windows) {
            driverWinPub.switchTo().window(w);
            System.out.println("---> Publisher window: "+w);
        }
    }

    public void switchToWindowSV(WindowsDriver driverWinSV) {
        Set<String> windows = driverWinSV.getWindowHandles();
        for(String w : windows) {
            driverWinSV.switchTo().window(w);
            System.out.println("---> SystemView window: "+w);
        }
    }

    public void switchToWindowRFAS(WindowsDriver driverWinRFAS) {
        Set<String> windows = driverWinRFAS.getWindowHandles();
        for(String w : windows) {
            driverWinRFAS.switchTo().window(w);
            System.out.println("---> RFAS window: "+w);
        }
    }

    public void switchToWindowFM(WindowsDriver driverWinFM) {
        Set<String> windows = driverWinFM.getWindowHandles();
        for(String w : windows) {
            driverWinFM.switchTo().window(w);
            System.out.println("---> FM window: "+w);
        }
    }

    public void switchToWindowLC(WindowsDriver driverWinLC) {
        Set<String> windows = driverWinLC.getWindowHandles();
        for(String w : windows) {
            driverWinLC.switchTo().window(w);
            System.out.println("---> LC window: "+w);
        }
        //driverWinLC.switchTo().window(LCWinHandleHex);
        //System.out.println("---> LogCollector ID is: "+LCWinHandleHex);
    }

    //@BeforeMethod
    public void initTestReport(Method method){reporter.startReporting(method.getName()); }

    public TestReporter reporter(){
        if(reporter!=null){
            return reporter;
        }
        return null;
    }

    //@AfterMethod
    public void closeReport(){ reporter.endReporting(); }

    //@AfterSuite
    public void clearReport(){
        reporter.flushReport();
    }

    //@AfterMethod
    public void testStatusInExtentReport(ITestResult result) {
        if(ITestResult.FAILURE == result.getStatus()){
            reporter().report(LogStatus.FAIL,"Failed test is: "+result.getName());
            reporter().report(LogStatus.FAIL,result.getThrowable());
        }else if(ITestResult.SUCCESS == result.getStatus()){
            reporter().report(LogStatus.PASS,"Test passed: "+result.getName());
        }else if(ITestResult.SKIP == result.getStatus()){
            reporter().report(LogStatus.SKIP,"Test skipped: "+result.getName());
        }
    }

    @AfterMethod  //(groups={"new_PolluxGateway","old_PolluxGateway"})
    public void takeScreenShotIfTestsFails(ITestResult result) throws Exception {
        if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("checkIfClientIsAuthorized_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinRFAS);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("checkIfClientIsAuthorized_OLD");
        } else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("verifyRenameFileMasterAndUnzipFiles_OLD")) {
            email.sendEmailForPolluxFailure("verifyRenameFileMasterAndUnzipFiles_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("startFM_AdminMode_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinGA);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("startFM_AdminMode_OLD");
        } else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("waitClientAuthorized_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinSA);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("waitClientAuthorized_OLD");
        } else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("renamePolluxFromPanelTPIFolder_OLD")) {
            email.sendEmailForPolluxFailure("renamePolluxFromPanelTPIFolder_OLD");
        } else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("openPolluxgw_AdminMode_OLD")) {
            TakesScreenshot camera =((TakesScreenshot)driverWinPub);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot,DestFile);
            email.sendEmailForPolluxFailure("openPolluxgw_AdminMode_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("VerifyReproductionAgentTitle_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinPub);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("VerifyReproductionAgentTitle_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("NumberOfTPIToSendToFM_OLD")) {
            email.sendEmailForPolluxFailure("NumberOfTPIToSendToFM_OLD");
        } else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("SendingJobsToFileMasterFromReproductionAgent_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinLC);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            System.out.println("##### THE REPRODUCTION AGENT PROCESS DIDN'T DISPLAY THE COMPLETE JOBS MESSAGE #####");
            email.sendEmailForPolluxFailure("SendingJobsToFileMasterFromReproductionAgent_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("readLogsFromSystemView_OLD")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinSV);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("readLogsFromSystemView_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("copyTPIFilesToOldTestVersionFolder")) {
            email.sendEmailForPolluxFailure("copyTPIFilesToOldTestVersionFolder");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("closeAllApp")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinRFAS);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("closeAllApp");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("eraseOldFolders_OLD")) {
            email.sendEmailForPolluxFailure("eraseOldFolders_OLD");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("verifyDownloadNewPolluxApp")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinGA);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("verifyDownloadNewPolluxApp");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("verifyRenameFileMasterAndUnzipFiles_NEW")) {
            email.sendEmailForPublisherFailure("verifyRenameFileMasterAndUnzipFiles_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("startFM_AdminMode_NEW")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinSA);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("startFM_AdminMode_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("waitClientAuthorized_NEW")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinRFAS);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("waitClientAuthorized_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("renamePolluxFromPanelTPIFolder_NEW")) {
            email.sendEmailForPolluxFailure("renamePolluxFromPanelTPIFolder_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("openPolluxgw_AdminMode_NEW")) {
            TakesScreenshot camera =((TakesScreenshot)driverWinPub);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot,DestFile);
            email.sendEmailForPolluxFailure("openPolluxgw_AdminMode_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("VerifyReproductionAgentTitle_NEW")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinRFAS);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPolluxFailure("VerifyReproductionAgentTitle_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("NumberOfTPIToSendToFM_NEW")) {
            email.sendEmailForPolluxFailure("NumberOfTPIToSendToFM_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("SendingJobsToFileMasterFromReproductionAgent_NEW")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinGA);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPublisherFailure("SendingJobsToFileMasterFromReproductionAgent_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("readLogsFromSystemView_NEW")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinSV);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPublisherFailure("readLogsFromSystemView_NEW");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("copyTPIFilesToNewTestVersionFolder")) {
           email.sendEmailForPublisherFailure("copyTPIFilesToNewTestVersionFoldert");
        }else if (ITestResult.FAILURE == result.getStatus() && result.getName().equals("verifyCompareFilesAndSendReport")) {
            TakesScreenshot camera = ((TakesScreenshot) driverWinSV);
            File screenShot = camera.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("C:\\TEST\\screenShots\\FAIL__" + result.getName() + "__" + formatDate() + ".png");
            FileHandler.copy(screenShot, DestFile);
            email.sendEmailForPublisherFailure("verifyCompareFilesAndSendReport");
        }
    }


    public String formatDate(){
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    date = dtf.format(now);
    date = date.replaceAll("/","").replaceAll(":","").replaceAll(" ","");
    return date;
    }

    public void takeAppSnap(WindowsDriver driver, String app) throws IOException {
           TakesScreenshot ts = (TakesScreenshot) driver;
           File file = ts.getScreenshotAs(OutputType.FILE);
           FileUtils.copyFile(file,new File("C:\\TEST\\screenShots\\"+app+"__"+formatDate()+".png"));
           //FileUtils.copyFile(file,new File(System.getProperty("user.dir")+"\\screenShots\\"+app+"__"+formatDate()+".png"));
           //FileUtils.copyFile(file,new File("./src/main/screenShots/"+app+"__"+formatDate()+".png"));
           if(!file.exists()){ file.mkdir(); }
    }

    public void takeFileDiffSnap(WindowsDriver driver, String fileName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File file = ts.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,new File("C:\\TEST\\screenShots\\"+fileName+".png"));
        //FileUtils.copyFile(file,new File(System.getProperty("user.dir")+"\\screenShots\\"+fileName+".png"));
        //FileUtils.copyFile(file,new File("./src/main/screenShots/"+fileName+".png"));
        if(!file.exists()){ file.mkdir(); }
    }

       public String takeScreenForReport(WindowsDriver driver, String app) throws IOException {
           TakesScreenshot ts = (TakesScreenshot) driver;
           File file = ts.getScreenshotAs(OutputType.FILE);
           Path srcPath = file.toPath();
           File DestFile=new File("./src/main/screenShots/"+app+"__"+formatDate()+".png");
           Path destPath = DestFile.toPath();
           Files.copy(srcPath,destPath);
           return destPath.toString();
       }

       public void scrollDownTillElementIsVisible(WebElement ele){ ele.sendKeys(Keys.PAGE_DOWN); }

       public void getElementCoordinates(WebElement ele) {
        Point point = ele.getLocation();
        int xcord = point.getX();
        int ycord = point.getY();
        System.out.println("Coordinates are: X-> "+xcord+" and Y -> "+ycord);
    }


    public void copyPathFiles(String s) throws Exception {
        // copying File path to Clipboard
        StringSelection str = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
        Robot rb = new Robot();
        // press Control+V for pasting
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);
        // release Control+V for pasting
        rb.keyRelease(KeyEvent.VK_CONTROL);
        rb.keyRelease(KeyEvent.VK_V);
    }

    private void pressButtonF1() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F1);
        robot.keyRelease(KeyEvent.VK_F1);
    }

}