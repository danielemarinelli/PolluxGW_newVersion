package tests;

import apps.*;
import core.excelUserData;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class UBOTest_PolluxGW_NewVersion extends TestBase{

    FilesActions files = null;
    ReproductionAgent ra = null;
    CopyFiles cp = null;
    RFAS rfas = null;
    PolluxGW p = null;
    SystemView sv = null;
    LogCollector lc = null;
    Delete delete = null;
    WinMerge wm = null;
    PolluxSimulator ps =null;
    String title_Polluxgw;
    public String[] ArrayPolluxgwVersion = null;

    @Test(priority=1, description="Renaming old and new PolluxGateway versions in the UNITAM SW folder")
    public void verifyDownloadNewPolluxApp() throws Exception {
        cp = new CopyFiles();
        boolean installation=cp.installNewPolluxApp();
        Assert.assertTrue(installation,"Renaming PolluxGateway in UNITAM SW folder failed");
    }

    //START AGAIN ALL PROCESS WITH NEW VERSION, first cancel all folders with data
    @Test(priority=2, description="Unzipping daily/common files into FileMaster folder")
    public void verifyRenameFileMasterAndUnzipFiles_NEW() throws Exception {
        files = new FilesActions();
        files.renameFileMasterFolder();
        System.out.println("#### Renamed fileMaster folder to start regression with NEW Pollux Version");
        setUpFM();
        files.createFoldersToUnzipCommonData();
        Thread.sleep(1000);
        System.out.println("#### Closing FM!!");
        System.out.println("#### Unzipping daily files!!");
        tearDownFM();
        int daily = files.unzipDailyDataFiles();
        System.out.println("#### Unzipping common files!!");
        Thread.sleep(1000);
        int common = files.unzipCommonDataFiles();
        if(daily>0 && common>=0){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    @Test(priority=3, description="Erase FileMaster_OLD Folder and PolluxFromPanelTPI_OLD folder to clean env for new version run")
    public void eraseOldFolders_NEW()  {
        files = new FilesActions();
        System.out.println(".......DELETING FileMaster_OLD folder........");
        int f = files.removeDirectory(new File("C:\\UNITAM\\FileMaster_OLD"));
        System.out.println("@@@ Deleted FileMaster_OLD folder @@@");
        System.out.println(".......DELETING PolluxFromPanelTPI_OLD folder........");
        int p = files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData\\PolluxFromPanelTPI_OLD"));
        files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origFromPanelData"));
        int l = files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\ToPanelData\\LastPlxToPanel_OLD"));
        System.out.println(".......DELETING LastPlxToPanel_OLD folder .....");
        //int t = files.removeFilesFromTestDirectoryOld();
        Assert.assertEquals(f+p+l,3);
        //Assert.assertEquals(f,1);
    }

    @Test(priority=4, description="Setting files and folders to start Pollux regression")
    public void renamePolluxFolders() throws Exception {
        files = new FilesActions();
        files.renamePolluxGateWayFilesFolders();
        cp = new CopyFiles();
        cp.copyFilesFromChannelCodeFolderToTestFolder();
        cp.copyFilesFromSkyExceptionsFolderToTestFolder();
        cp.copyFilesFromNTACodeFolderToTestFolder();
        files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origChannelCode"));
        files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origSkyExceptions"));
        files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origNTACode"));
        files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_orig2021"));
        //files.removeDirectory(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origFromPanelData"));
        Assert.assertTrue(true);
    }

    @Test(priority=5, description="Open File Master application in ADMIN MODE")
    public void startFM_AdminMode_NEW() throws Exception {
        lc = new LogCollector();
        lc.launchFMinAdminMode(getDriverLC());
        attachDriverToFM();
        String fmTitle = lc.appTitleFM(getDriverFM());
        Assert.assertEquals(fmTitle,"FileMaster");
    }

    @Test(priority=6, description="Opens RFAS and checks the Client Authorization with New Pollux version")
    public void waitClientAuthorized_NEW() throws Exception {
        rfas = new RFAS(getDriverRFAS());
        lc = new LogCollector();
        int client_RFAS = 0;
        switchToWindowLC(getDriverLC());
        lc.launchRFASInAdminMode(getDriverLC());
        attachDriverToRFAS();
        client_RFAS = rfas.setAuthorization(getDriverRFAS());  //Check if Authorization is needed
        //client_RFAS = rfas.checkIfClientIsAuthorization(getDriverRFAS());
        Assert.assertEquals(client_RFAS,1);
    }

    @Test(priority=7, description="Renaming PolluxFromPanelTPI folder")
    public void renamePolluxFromPanelTPIFolder() throws Exception {
        files = new FilesActions();
        files.renamePolluxFromPanelTPIFolder();
        Assert.assertTrue(true);
    }

    @Test(priority=8, description="Open PolluxGateway application")
    public void openPolluxgw_AdminMode_NEW() throws Exception {
        lc = new LogCollector();
        p = new PolluxGW();
        switchToWindowLC(getDriverLC());
        lc.launchPolluxGatewayInAdminMode(getDriverLC());
        attachDriverToNewPolluxGatewayVersion();
        title_Polluxgw = lc.appTitlePG(getDriverPolluxGW());
        ArrayPolluxgwVersion = p.getNewVersionPolluxGW(getDriverPolluxGW()).replace("."," ").split(" ");
        System.out.println("New Pollux Version is  >>>> "+ArrayPolluxgwVersion[3]);
        Assert.assertEquals(title_Polluxgw,"PolluxGateway");
    }

    @Test(priority=9, description="Get ReproductionAgent application Title")
    public void VerifyReproductionAgentTitle_NEW() {
        setUpReproductionAgent();
        ra = new ReproductionAgent(getDriverRA());
        String title_ReproAgent;
        title_ReproAgent=ra.getTitleRA();
        Assert.assertEquals(title_ReproAgent,"ReproductionAgent");
    }

    @Test(priority=10, description="TPI files to send to FileMaster")
    public void NumberOfTPIToSendToFM_NEW() throws IOException {
        ra = new ReproductionAgent(getDriverRA());
        System.out.println("Total TPI files on mixer output  " + ra.getNumberTotalTPIFilesFromMixerOutputFolder());
        Assert.assertTrue(ra.getNumberTotalTPIFilesFromMixerOutputFolder()>0);
    }

    @Test(priority=11, description="Sending Jobs to FileMaster with New Pollux Version",timeOut = 800000)  //12 mins timeout
    public void SendingJobsToFileMasterFromReproductionAgent_NEW() throws Exception {
        ra = new ReproductionAgent(getDriverRA());
        p = new PolluxGW();
        String reproAgentMessage = ra.setJobRepAgent();
        p.screenPolluxGW(getDriverPolluxGW());
        //takeAppSnap(getDriverPolluxGW(),title_Polluxgw);
        //Assert.assertEquals(reproAgentMessage,"Reproduction Wizard - Process Complete", "Repro Agent process didn't end correctly");
    }

    @Test(priority=12, description="Open System View app")
    public void readLogsFromSystemView_NEW() {
        System.out.println("##########System View############");
        String title_sv = null;
        try {
            setUpSystemView();
            sv = new SystemView(getDriverSV());
            sv.displaySystemViewLogs();
            title_sv = sv.mixerSnapShotWithNewVersion();
            lc = new LogCollector();
            lc.get_LC_Picture(getDriverLC());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_sv,"SystemView");
    }

    @Test(priority=13, description="Open Publisher application in ADMIN MODE")
    public void startPublisher_AdminMode_NEW() throws Exception {
        lc = new LogCollector();
        lc.launchPublisherInAdminMode(getDriverLC());
        attachDriverToPublisher();
        String pubTitle = lc.appTitlePublisher(getDriverPublisher());
        Assert.assertEquals(pubTitle,"Publisher");
    }

    //insert polluxsimulator
    @Test(priority=14, description="Open PolluxSimulator application in ADMIN MODE")
    public void startPolluxSimulator_AdminMode_NEW()  {
        System.out.println("##########Pollux Simulator############");
        String title_ps = null;
        try {
            setUpPolluxSimulator();
            ps = new PolluxSimulator();
            title_ps = ps.get_PS_Picture(getDriverPS());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_ps,"PolluxSimulator");
    }

    @Test(priority=15, description="Copying files from MixerOutput to a Test folder for compare of new version")
    public void generatingToPanelFilesWithPublisherAndPolluxSimulatorApps_NEW() throws Exception {
        System.out.println("Generating ToPanel Files with OLD version");
        files = new FilesActions();
        //files.renameToPanelSettingsAndLastPlxToPanelFolders();
        ps = new PolluxSimulator();
        ps.polluxSendProcess(getDriverPS());
    }

    @Test(priority=16, description="Copying files from ToPanelSettings to a Test folder for compare of new version")
    public void copyPanelSettingsFilesToOldTestVersionFolder() throws Exception {
        System.out.println("Copy generated ToPanel\\Settings to Test folder");
        cp = new CopyFiles();
        boolean s = cp.copyFilesFromToPanelSettingsFolderToNewTestFolder();
        System.out.println("ToPanel Files copied to C:\\TEST\\newAppVersion, ready to be compared....");
        Assert.assertTrue(s);
    }

    @Test(priority=17, description="Copying files from MixerOutput to a Test folder for compare of new version")
    public void copyTPIFilesToNewTestVersionFolder() throws Exception {
        System.out.println("Copy generated TPI to Test folder");
        cp = new CopyFiles();
        boolean t = cp.copyFilesFromPolluxOutputFolderToNewTestFolder();
        System.out.println("TPI copied to C:\\TEST\\newAppVersion folder, ready to be compared....");
        Assert.assertTrue(t);
    }

    @Test(priority=18, description="Open PolluxGW logs from System View app")
    public void readPolluxGWLogsFromSystemView_OLD() {
        System.out.println("##########System View for Pollux gw logs############");
        String title_sv = null;
        try {
            //setUpSystemView();
            switchToWindowSV(getDriverSV());
            sv = new SystemView(getDriverSV());
            title_sv = sv.displayPolluxGWLogs();
            //title_sv = sv.mixerSnapShotWithOldVersion();
            //lc = new LogCollector();
            //lc.get_LC_Picture(getDriverLC());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(title_sv,"SystemView");
    }


    @Test(priority=19, description="Opening Signals Admin and Priority List Action executed with NEW Publisher versionn")
    public void verifyCompareFilesAndSendReport() throws Exception {
        openWinMergeApp();
        wm = new WinMerge(getDriverWinMerge());
        //int files = wm.generateReportToSendWithTPIComparation("TEST0000");
        int files = wm.generateReportToSendWithTPIComparation(ArrayPolluxgwVersion[3]);
        System.out.println("REPORT GENERATED via Email");
        if(files==1){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

    @Test(priority=20, description="Moves the old PolluxGW version from UNITAM SW folder to other folder")  //
    public void movePolluxOldExeFile() throws IOException {
        files = new FilesActions();
        int file = files.cancelPolluxOldExeFileFromUnitamSWFolder();
        if(file==1){
            Assert.assertTrue(true);
        }else{
            Assert.fail();}
    }

}
