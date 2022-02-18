package core;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.windows.WindowsDriver;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestReporter {

    private ExtentReports extent;
    protected ExtentTest test;
    private WindowsDriver driver;
    List<Map<String,String>> allDataFromFile;

    public TestReporter() throws IOException {
        allDataFromFile = excelUserData.getDataFromExcelFile();
        extent = new ExtentReports("./src/main/test-output/TestReport.html", true);
        extent.addSystemInfo("Tests","UBO regressions")
                .addSystemInfo("Host Name",allDataFromFile.get(0).get("Host Name"))
                .addSystemInfo("Environment","Test Server")
                .addSystemInfo("User Name",allDataFromFile.get(0).get("Tester"));
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
        File file = new File("./src/main/test-output");
        if(!file.exists()){ file.mkdir(); }
    }

    public void startReporting(String testName){test = extent.startTest(testName, "#####Start reporting#####"); }

    public void endReporting(){ extent.endTest(test); }

    public void flushReport(){
        extent.flush();
        extent.close();
    }

    public void report(LogStatus status, String description){
        if(test!=null){
            test.log(status, description);
        }
    }

    public void report(LogStatus status,  Throwable description){
        if(test!=null){
            test.log(status, description);
        }
    }

}