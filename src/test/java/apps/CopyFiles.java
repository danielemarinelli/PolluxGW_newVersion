package apps;

import core.excelUserData;
import tests.TestBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;


public class CopyFiles extends TestBase {

    public static List<Map<String,String>> app_new_vers;

    public boolean copyFilesFromPolluxOutputFolderToOldTestFolder() throws Exception {
            app_new_vers = excelUserData.getPolluxGWDataFromFile();
            File theDir = new File(app_new_vers.get(0).get("OldFolderPolluxApp"));
            if (!theDir.exists()){theDir.mkdirs();}
            ProcessBuilder ps = new ProcessBuilder("xcopy", "C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData\\PolluxFromPanelTPI\\"+app_new_vers.get(0).get("StartYear").replace(".0","")+"\\"+app_new_vers.get(0).get("StartMonth").replace(".0","")+"\\"+app_new_vers.get(0).get("StartDay").replace(".0",""), app_new_vers.get(0).get("OldFolderPolluxApp"));
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine())  != null) {
                System.out.println(line);}
            pr.waitFor();
            System.out.println("ok! Files copied under C:\\TEST Old Folder");
            in.close();
        return true;
    }

    public boolean copyFilesFromPolluxOutputFolderToNewTestFolder() throws Exception {
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        File theDir = new File(app_new_vers.get(0).get("OldFolderPolluxApp"));
        if (!theDir.exists()){theDir.mkdirs();}
        ProcessBuilder ps = new ProcessBuilder("xcopy", "C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData\\PolluxFromPanelTPI\\"+app_new_vers.get(0).get("StartYear").replace(".0","")+"\\"+app_new_vers.get(0).get("StartMonth").replace(".0","")+"\\"+app_new_vers.get(0).get("StartDay").replace(".0",""), app_new_vers.get(0).get("NewFolderPolluxApp"));
        ps.redirectErrorStream(true);
        Process pr = ps.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine())  != null) {
            System.out.println(line);}
        pr.waitFor();
        System.out.println("ok! Files copied under C:\\TEST New Folder");
        in.close();
        return true;
    }

    public boolean installNewPolluxApp() throws Exception {
        System.out.println("rename of Pollux in unitamsw");
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        File oldPollux = new File("C:\\UNITAM SW\\PolluxGateway.exe");
        File oldPollux1 = new File(app_new_vers.get(0).get("RenamePolluxVersion"));
        // Renames the file
        boolean renamed = oldPollux.renameTo(oldPollux1);
        if (renamed) { System.out.println("File renamed to " + oldPollux1.getPath()); }
        else { System.out.println("Error renaming file " + oldPollux.getPath()); }
        File newPollux1 = new File(app_new_vers.get(0).get("NewPolluxVersion"));
        File newPollux = new File("C:\\UNITAM SW\\PolluxGateway.exe");
        boolean renamed1 = newPollux1.renameTo(newPollux);
        if (renamed1) { System.out.println("File renamed to " + newPollux.getPath()); }
        else { System.out.println("Error renaming file " + newPollux1.getPath()); }
        System.out.println("############################");
        System.out.println("STARTING TEST WITH NEW POLLUXGW VERSION");
        System.out.println("############################");
        return (renamed && renamed1);
    }

    public boolean copyFilesFromChannelCodeFolderToTestFolder() throws Exception {
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        File ChannelCode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\ChannelCode");
        if (!ChannelCode.exists()){
            ChannelCode.mkdir();
        }
        ProcessBuilder ps = new ProcessBuilder("xcopy",app_new_vers.get(0).get("ChannelCode"), "C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\ChannelCode");
        ps.redirectErrorStream(true);
        Process pr = ps.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine())  != null) {
            System.out.println(line);}
        pr.waitFor();
        System.out.println("ok! Files copied under Panel_0\\Files\\ChannelCode Folder");
        in.close();
        return true;
    }

    public boolean copyFilesFromSkyExceptionsFolderToTestFolder() throws Exception {
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        File SkyExceptions = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\SkyExceptions");
        if (!SkyExceptions.exists()){SkyExceptions.mkdir();}
        ProcessBuilder ps = new ProcessBuilder("xcopy",app_new_vers.get(0).get("SkyExceptions"), "C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\SkyExceptions");
        ps.redirectErrorStream(true);
        Process pr = ps.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine())  != null) {
            System.out.println(line);}
        pr.waitFor();
        System.out.println("ok! Files copied under Panel_0\\Files\\SkyExceptions Folder");
        in.close();
        return true;
    }

    public boolean copyFilesFromNTACodeFolderToTestFolder() throws Exception {
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        File NTACode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\NTACode");
        if (!NTACode.exists()){NTACode.mkdir();}
        ProcessBuilder ps = new ProcessBuilder("xcopy",app_new_vers.get(0).get("NTACode"), "C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\NTACode");
        ps.redirectErrorStream(true);
        Process pr = ps.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine())  != null) {
            System.out.println(line);}
        pr.waitFor();
        System.out.println("ok! Files copied under Panel_0\\Files\\NTACode Folder");
        in.close();
        return true;
    }


}
