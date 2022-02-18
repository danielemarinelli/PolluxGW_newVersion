package apps;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import tests.TestBase;
import core.excelUserData;
import core.email;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WinMerge extends TestBase {

    public static List<Map<String,String>> folders_names;

    WindowsDriver driverWinMerge=null;
    public WinMerge(WindowsDriver driverWinMerge){ this.driverWinMerge = driverWinMerge; }

    public int selectFilesToCompare() throws Exception {
        int count=0;
        folders_names = excelUserData.getFoldersNamesFromExcelSheet();
        switchToWindowWinMerge(driverWinMerge);
        Thread.sleep(1000);
        WebElement title = driverWinMerge.findElementByAccessibilityId("TitleBar");
        WebElement menuBar = driverWinMerge.findElementByAccessibilityId("MenuBar");
        //WebElement restore = driverWinMerge.findElementByName("Restore");
        WebElement diffButton = driverWinMerge.findElementByName("Next Difference (Alt+Down)");
        Actions a = new Actions(driverWinMerge);
        a.doubleClick(title);
        getElementCoordinates(menuBar);
        getElementCoordinates(diffButton);
        //a.moveToElement(diffButton,0,25).click().build().perform();
        a.moveToElement(menuBar,-450,25).click().build().perform();
        driverWinMerge.findElementByAccessibilityId("57601").click();  //click on open icon
        copyPathFiles(folders_names.get(0).get("NewFolderApp"));
        driverWinMerge.findElementByAccessibilityId("1005").click();
        copyPathFiles(folders_names.get(0).get("OldFolderApp"));
        driverWinMerge.findElementByAccessibilityId("1006").click();
        driverWinMerge.findElementByAccessibilityId("1").click(); //click compare button
        System.out.println("Files Number to compare: "+driverWinMerge.findElementsByAccessibilityId("ListViewSubItem-0").size());
        //a.doubleClick(title);
        List diffs = driverWinMerge.findElementsByAccessibilityId("ListViewSubItem-4");
        ArrayList l = new ArrayList();   //contains position of only HH with differences
        ArrayList<Integer> numberOfDifferencesInFiles = new ArrayList<>();
        int position = 0;
        //int totalHH = 0;
            for (Object diff:diffs) {
                if(!(((RemoteWebElement) diff).getText().equals("0"))){
                    count++;
                    Thread.sleep(500);
                    l.add(position);  // l contains positions of files that are different
                    numberOfDifferencesInFiles.add(Integer.parseInt(((RemoteWebElement) diff).getText()));  //
                }
                position++; //at the end of foreach loop position is equal to the total HH to publish
                //totalHH++;
            }
            if(l.size()==0){System.out.println("ALL FILES ARE EQUAL!!!!!");
            }else {System.out.println("Positions of files that are different listed here --> "+l);}
        System.out.println("Comparing all the files, there are "+count+" with differences");
        List files = driverWinMerge.findElementsByAccessibilityId("ListViewSubItem-0");
        Actions action = new Actions(driverWinMerge);
        int i = 0;
        int j = 0;
        ArrayList<String> listOfDifferentFiles = new ArrayList<>();
        if(l.size()>0) {
            System.out.println("########## OPENING ALL FILES CONTAINING DIFFERENCES ##########");
            for (Object file : files) {
                if (l.contains(i)) {
                    action.moveToElement((RemoteWebElement) file).doubleClick().build().perform();
                    listOfDifferentFiles.add(((RemoteWebElement) file).getText());
                    a.doubleClick(title).build().perform();  //Maximize window
                    for (int k = 0; k < numberOfDifferencesInFiles.get(j); k++) {
                        diffButton.click();
                        Thread.sleep(500);
                        takeFileDiffSnap(driverWinMerge, (((RemoteWebElement) file).getText().replace(".", "")+"___"+(k+1)));
                    }
                    System.out.println("File " + ((RemoteWebElement) file).getText()+" contains "+numberOfDifferencesInFiles.get(j)+" differences");
                    //a.doubleClick(title).build().perform();  //Window back to normal size
                    //a.moveToElement(menuBar, 300, 95).click().build().perform();
                    a.moveToElement(diffButton, -30, 40).click().build().perform();
                    j++;
                }
                //20 hh in winmerge don't show the element "Line down"
                if(position>20){driverWinMerge.findElementByName("Line down").click();}
                i++;
                a.doubleClick(title).build().perform();  //Window back to normal size
            }
            //email.attachFilesInEmail(listOfDifferentFiles);
        }else{
            System.out.println("The files are all equal, so no email with attached files was sent");
            takeAppSnap(driverWinMerge,"WinMerge");
        }
        //a.doubleClick(title).build().perform();
        //a.doubleClick(restore).build().perform();
        return files.size();
    }


    public int generateReportToSendWithTPIComparation(String version) throws Exception {
        folders_names = excelUserData.getPolluxGWDataFromFile();
        switchToWindowWinMerge(driverWinMerge);
        List diffs = driverWinMerge.findElementsByAccessibilityId("ListViewSubItem-4");
        WebElement menuBar = driverWinMerge.findElementByAccessibilityId("MenuBar");
        WebElement title = driverWinMerge.findElementByAccessibilityId("TitleBar");
        WebElement diffButton = driverWinMerge.findElementByName("Next Difference (Alt+Down)");
        Actions a = new Actions(driverWinMerge);
        a.doubleClick(title);
        a.moveToElement(menuBar,-450,25).click().build().perform();
        driverWinMerge.findElementByAccessibilityId("57601").click();  //click on open icon
        copyPathFiles(folders_names.get(0).get("NewFolderPolluxApp"));
        driverWinMerge.findElementByAccessibilityId("1005").click();
        copyPathFiles(folders_names.get(0).get("OldFolderPolluxApp"));
        driverWinMerge.findElementByAccessibilityId("1006").click();
        driverWinMerge.findElementByAccessibilityId("1").click(); //click compare button
        //getElementCoordinates(diffButton);
        a.moveToElement(diffButton,-50,-25).click().build().perform();  //click on Tools
        driverWinMerge.findElementByAccessibilityId("32868").click();   //click generate report
        //driverWinMerge.findElementByAccessibilityId("1001").clear();
        String filePathForReport = folders_names.get(0).get("Report")+"\\Regression_Report";
        copyPathFiles(filePathForReport);
        //copyPathFiles(folders_names.get(0).get("Report"));
        driverWinMerge.findElementByAccessibilityId("1").click();
        //System.out.println("....Generating REPORT with TPI version "+versionTPI+" ....");
        email.sendReportAfterCompare(version);
        Thread.sleep(5000);
        driverWinMerge.findElementByAccessibilityId("1").click();
        a.moveToElement(diffButton,1500,-40).click().build().perform();  //click in restore window button
        return 1;
    }

}
