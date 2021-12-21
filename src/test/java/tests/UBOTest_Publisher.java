package tests;

import apps.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UBOTest_Publisher extends TestBase{

    Publisher p = null;
    //FileMaster fm = null;
    RFAS rfas = null;
    GroupAdmin ga = null;
    SignalAdmin sa = null;
    SystemView sv = null;
    LogCollector lc = null;
    WinMerge wm = null;
    CopyFiles copy = null;
    public String[] ArrayPublisherVersion = null;
    FilesActions files = null;
    Delete delete = null;
    String versionTPIBefore = null;
    String versionTPIAfter = null;


    //CLEANING ACTION with BOTH VERSIONS and UNZIP files
    @Test(priority=1, groups={"Publisher"}, description="Unzipping daily/common files into FileMaster folder")
    public void verifyRenameFileMasterAndUnzipFiles_OLD() throws Exception {
        files = new FilesActions();
            files.renameFileMasterFolder();  // DOESN'T RENAME FILE MASTER FOLDER if already exists!!
            System.out.println("#### Performed action renamed fileMaster folder...");
            setUpFM();
            files.createFoldersToUnzipCommonData();
            Thread.sleep(1000);
            System.out.println("#### Unzipping daily files!!");
            int daily = files.unzipDailyDataFiles();
            System.out.println("#### Unzipping common files!!");
            Thread.sleep(1000);
            int common = files.unzipCommonDataFiles();
        if(daily>0 && common>=0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    @Test(priority=2, groups={"Publisher"}, description="Delete TEST files from previous Test execution")
    public void deleteFilesOfPreviousTest() throws Exception {
        delete = new Delete();
        int numberFiles = delete.deleteFiles();
        if(numberFiles==0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    //LAST TEST TO PERFORM REMOVE FOLDER FileMaster_OLD at END regression Old and New
    @Test(priority=3, groups={"Publisher"}, description="Erase FileMaster OLD Folder")
    public void eraseOldFileMasterFolder_OLD(){
        files = new FilesActions();
        System.out.println(".......DELETING FileMaster_OLD folder........");
        int f = files.removeDirectory(new File("C:\\UNITAM\\FileMaster_OLD"));
        System.out.println("@@@ Deleted FileMaster_OLD folder @@@");
        Assert.assertEquals(f,1);
    }

    @Test(priority=4, groups={"Publisher"}, description="Opens RFAS and checks the Client Authorization with OLD Publisher version")
    public void checkIfClientIsAuthorized_OLD() throws Exception {
        setUpRFAS();
        rfas = new RFAS(getDriverRFAS());
        Thread.sleep(4000);
        switchToWindowRFAS(getDriverRFAS());
        int client_RFAS = 0;
        try {
            client_RFAS = rfas.setAuthorization();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(client_RFAS,1);
    }

    @Test(priority=5, groups={"Publisher"}, description="Opening OLD Publisher version")
    public void checkIfPublisherOpensCorrectly_OLD()  {
        setUpPublisher();
        p = new Publisher(getDriverPub());
        String title_Publisher = null;
        try {
            title_Publisher = p.openPublisherApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayPublisherVersion = p.getPublisherVersion().replace("."," ").split(" ");
        System.out.println("Publisher Version is... "+ArrayPublisherVersion[3]);
        Assert.assertEquals(title_Publisher,"Publisher");
    }

    @Test(priority=6, groups={"Publisher"}, description="Opening Signals Admin and Priority List Action executed with OLD Publisher version")
    public void performPriorityListAction_OLD() {
        String title_SignalsAdmin = null;
        try {
            setUpSignalsAdmin();
            sa = new SignalAdmin(getDriverSignalsAdmin());
            sa.openSignalsAdminApp();
            Thread.sleep(6000);
            switchToWindowSA(getDriverSignalsAdmin());
            title_SignalsAdmin = sa.processPriorityList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_SignalsAdmin,"SignalsAdmin");
        getDriverSignalsAdmin().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test(priority=7, groups={"Publisher"}, description="Publishing with OLD version the households number specify in the Excel Sheet ")
    public void checkIfPublishHouseHoldsProcessIsCorrect_OLD()  {
        String title_GroupAdmin = null;
        try {
            setUpGroupAdmin();
            ga = new GroupAdmin(getDriverGA(),getDriverPub());
            ga.openGroupAdminApp();
            Thread.sleep(6000);
            switchToWindowGA(getDriverGA());
            //title_GroupAdmin = ga.publishAll_HH();  1322
            //title_GroupAdmin = ga.publishOnlyActive_HH();  //  818
            title_GroupAdmin = ga.selectOnly_number_Active_HH();  //300
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_GroupAdmin,"GroupAdmin");
    }

    @Test(priority=8, groups={"Publisher"}, description="Opening System View to read the OLD version Publisher Logs")
    public void readLogsFromSystemView_OLD() {
        System.out.println("##########System View############");
        String title_sv = null;
        try {
            setUpSystemView();
            sv = new SystemView(getDriverSV());
            title_sv = sv.displaySystemViewLogs();
            lc = new LogCollector();
            lc.get_LC_Picture(getDriverLC());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_sv,"SystemView");
    }

    @Test(priority=9, groups={"Publisher"},timeOut = 50000, description="Copying files from ToPanel Setting to a Test folder for compare of old version")
    public void copyGeneratedFilesToOldTestVersionFolder() throws Exception {
        System.out.println("Inside copyGeneratedFiles() method");
        copy = new CopyFiles();
        boolean t = copy.copyFilesFromAppFolderToOldTestFolder(ArrayPublisherVersion[3]);
        System.out.println("files all copied to OLD TEST folder....");
        Assert.assertTrue(t);
    }

    @Test(priority=10, groups={"Publisher"}, description="Closing all applications")
    public void closeAllApps() throws InterruptedException {
        //fm = new FileMaster();
        //System.out.println("WinMerge closed...");
        switchToWindowSA(getDriverSignalsAdmin());
        tearDownSA();
        System.out.println("SystemAdmin closed...");
        switchToWindowSV(getDriverSV());
        tearDownSV();
        System.out.println("System View closed...");
        switchToWindowRFAS(getDriverRFAS());
        tearDownRFAS();
        System.out.println("RFAS closed...");
        switchToWindowGA(getDriverGA());
        tearDownGA();
        switchToWindowPublisher(getDriverPub());
        tearDownPub();
        System.out.println("Publisher closed...");
        switchToWindowFM(getDriverFM());
        tearDownFM();
        System.out.println("FM closed...");
        switchToWindowLC(getDriverLC());
        Thread.sleep(15000);  //pause to be sure FM is closed with all it's threads
        //tearDownLC();
        //tearDownLC_WD();
        System.out.println("App closed except LC!!!!");
    }

    @Test(priority=11, groups={"Publisher"}, description="Erase FileMaster OLD Folder to start with NEW Publisher Version")
    public void eraseOldFileMasterFolder_NEW(){
        files = new FilesActions();
        System.out.println(".......DELETING FileMaster_OLD folder to start with NEW Publisher Version........");
        int f = files.removeDirectory(new File("C:\\UNITAM\\FileMaster_OLD"));
        System.out.println("@@@ Deleted FileMaster_OLD folder @@@");
        Assert.assertEquals(f,1);
    }

    @Test(priority=12, groups={"Publisher"}, description="Unzipping daily/common files into FileMaster folder")
    public void verifyRenameFileMasterAndUnzipFiles_NEW() throws Exception {
        files = new FilesActions();
        files.renameFileMasterFolder();  // DOESN'T RENAME FILE MASTER FOLDER if already exists!!
        System.out.println("#### Renamed fileMaster folder to start test with NEW Publisher version...");
        setUpFM();
        files.createFoldersToUnzipCommonData();
        Thread.sleep(1000);
        System.out.println("#### Unzipping daily files to start test with NEW Publisher version...!!");
        int daily = files.unzipDailyDataFiles();
        System.out.println("#### Unzipping common files to start test with NEW Publisher version...!!");
        Thread.sleep(1000);
        int common = files.unzipCommonDataFiles();
        if(daily>0 && common>=0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    //FIRST ACTION with new VERSION (get TPI version of old Publisher)
    @Test(priority=13,groups={"Publisher"}, description="Store TPI Version before regression starts")
    public void getTPIVersionBeforeRegressionStarts() throws Exception {
        System.out.println("TPI Version with OLD Publisher version....");
        files = new FilesActions();
        versionTPIBefore = files.getTpiVersionFromPublisherINI();
        if(!versionTPIBefore.isEmpty()){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    //ALWAYS REMEMBER TO NAME:
    //C:\UNITAM SW\Publisher the old version and
    // C:\TEST\PublisherXXX the new version where XXX is the new version, ex 117
    //SECOND ACTION WITH NEW VERSION
    @Test(priority=14, groups={"Publisher"}, description="Renaming old and new Publisher versions in the UNITAM SW folder")
    public void verifyDownloadNewApp() throws Exception {
        System.out.println("Inside verifyDownloadNewApp() method");
        copy = new CopyFiles();
        //setUpPublisher();
        //copy.installNewApp(getDriverPub());
        boolean installation=copy.installNewApp();
        Assert.assertTrue(installation,"Renaming Publisher in UNITAM SW folder failed");

    }

    @Test(priority=15, groups={"Publisher"}, description="Delete files from FileMaster ToPanel Setting folder")
    public void emptyFilesInToPanelSetting() throws IOException {
        delete = new Delete();
        int numberFiles = delete.emptyFolderSetting();
        if(numberFiles==0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    @Test(priority=16, groups={"Publisher"}, description="Opens RFAS and checks the Client Authorization with NEW Publisher version")
    public void checkIfClientIsAuthorized_NEW() throws Exception {
        setUpRFAS();
        rfas = new RFAS(getDriverRFAS());
        Thread.sleep(4000);
        switchToWindowRFAS(getDriverRFAS());
        int client_RFAS = 0;
        try {
            client_RFAS = rfas.setAuthorization();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(client_RFAS,1);
    }

    @Test(priority=17, groups={"Publisher"}, description="Opening NEW Publisher version")
    public void checkIfPublisherOpensCorrectly_NEW()  {
        setUpPublisher();
        p = new Publisher(getDriverPub());
        String title_Publisher = null;
        try {
            title_Publisher = p.openPublisherApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayPublisherVersion = p.getPublisherVersion().replace("."," ").split(" ");
        System.out.println("Publisher Version is... "+ArrayPublisherVersion[3]);
        Assert.assertEquals(title_Publisher,"Publisher");
    }

    @Test(priority=18, groups={"Publisher"}, description="Opening Signals Admin and Priority List Action executed with NEW Publisher version")
    public void performPriorityListAction_NEW() {
        String title_SignalsAdmin = null;
        try {
            setUpSignalsAdmin();
            sa = new SignalAdmin(getDriverSignalsAdmin());
            sa.openSignalsAdminApp();
            Thread.sleep(6000);
            switchToWindowSA(getDriverSignalsAdmin());
            title_SignalsAdmin = sa.processPriorityList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_SignalsAdmin,"SignalsAdmin");
        getDriverSignalsAdmin().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test(priority=19, groups={"Publisher"}, description="Publishing with NEW version the households number specify in the Excel Sheet ")
    public void checkIfPublishHouseHoldsProcessIsCorrect_NEW()  {
        String title_GroupAdmin = null;
        try {
            setUpGroupAdmin();
            ga = new GroupAdmin(getDriverGA(),getDriverPub());
            ga.openGroupAdminApp();
            Thread.sleep(6000);
            switchToWindowGA(getDriverGA());
            //title_GroupAdmin = ga.publishAll_HH();  1322
            //title_GroupAdmin = ga.publishOnlyActive_HH();  //  818
            title_GroupAdmin = ga.selectOnly_number_Active_HH();  //300
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_GroupAdmin,"GroupAdmin");
    }

    @Test(priority=20, groups={"Publisher"}, description="Opening System View to read the NEW version Publisher Logs")
    public void readLogsFromSystemView_NEW() {
        System.out.println("##########System View############");
        String title_sv = null;
        try {
            setUpSystemView();
            sv = new SystemView(getDriverSV());
            title_sv = sv.displaySystemViewLogs();
            lc = new LogCollector();
            lc.get_LC_Picture(getDriverLC());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_sv,"SystemView");
    }

    @Test(priority=21, groups={"Publisher"},timeOut = 50000, description="Copying files from ToPanel Setting to a Test folder for comparation of new version")
    public void copyGeneratedFilesToNewTestVersionFolder() throws Exception {
        System.out.println("Inside copyGeneratedFiles() method");
        copy = new CopyFiles();
        boolean t = copy.copyFilesFromAppFolderToNewTestFolder();
        System.out.println("files all copied to NEW folder....");
        Assert.assertTrue(t);
    }

    //@Test(priority=22, groups={"Publisher"})
    public void removeLinePublisherINI_AndCheckTPIVersion() throws Exception {
        files = new FilesActions();
        files.eraseLineInFilePublisherINI();
        files.moveFile();
        setUpPublisher();
        Thread.sleep(4000);
        System.out.println("TPI Version after erasing the row in Publisher.ini file");
        versionTPIAfter = files.getTpiVersionFromPublisherINI();
        System.out.println("version tpi AFTER: "+versionTPIAfter);
        Assert.assertEquals(versionTPIAfter,versionTPIBefore);  // IF EQUAL DO COMPARE FILES WITH WINMERGE,
                                                                // IF NOT EQUAL REGRESSIONS AGAIN WITH NEW TPI VERSION
    }

    //THE LAST TEST AFTER BOTH VERS APPS PUBLISHED!!
    // dependsOnMethods = {"removeLinePublisherINI_AndCheckTPIVersion"},
    @Test(priority=23, groups={"Publisher"}, description="Opening WinMerge application and create the comparation report")
    public void verifyCompareFiles() throws Exception {
        wm = new WinMerge(getDriverWinMerge());
        int files = wm.generateReportToSend(ArrayPublisherVersion[3], versionTPIBefore);
        System.out.println("TPI VERSION FOR THIS REGRESSION TESTS IS: -------> " +versionTPIBefore);
        if(files>0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    @Test(priority=24, groups={"Publisher"}, description="Moves the old publisher version from UNITAM SW folder to other folder")  //
    public void movePublisherOldExeFile() throws IOException {
        files = new FilesActions();
        int file = files.cancelPublisherOldExeFileFromUnitamSWFolder();
        if(file==1){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }


    //DUMMY TEST!!
    //@Test(priority=11, groups={"Publisher_old"}, description="Launch mvn test after changing tag in TestRunner.xml")
    public void verifyLaunchScriptToStartAgainExecution() throws Exception {
        files = new FilesActions();
        int file = files.launchTestFromCommandLine("mvn test");
        if(file==1){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }



}
