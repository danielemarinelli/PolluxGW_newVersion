package apps;

import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.RemoteWebElement;
import tests.TestBase;
import core.excelUserData;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesActions extends TestBase {

    public static List<Map<String, String>> app_folders;
    WindowsDriver driverWinMerge = null;
    public static List<Map<String,String>> app_new_vers;
    public static List<Map<String,String>> allCommonFoldersFromFile;
    public static List<Map<String, String>> foldersPollux;

    public void moveFile() throws Exception {
        System.out.println("inside method MOVE-FILES Files.move");
        Path temp = Files.move
                (Paths.get("C:\\Windows\\Publisher.ini"), Paths.get("C:\\TEST\\old_Exe\\Publisher.ini")); //original
        if (temp != null) {
            System.out.println("File Publisher.ini moved successfully");
        } else {System.out.println("Failed to move the file Publisher.ini");}
        Path temp1 = Files.move(Paths.get("C:\\Windows\\Publisher_copy.txt"), Paths.get("C:\\TEST\\Publisher.ini"));
        if (temp1 != null) {
            System.out.println("File Publisher_copy.txt moved successfully");
        } else {System.out.println("Failed to move the file Publisher_copy.txt");}

        emptyOriginalINIFile();

        Path temp3 = Files.move(Paths.get("C:\\TEST\\old_Exe\\Publisher.ini"), Paths.get("C:\\Windows\\Publisher.ini"));

        if (temp3 != null) {
            System.out.println("File Publisher.ini moved successfully into C:\\Windows folder");
        } else {
            System.out.println("Failed to move the file Publisher.ini into C:\\Windows folder");
        }
    }

    public void emptyOriginalINIFile() throws Exception {
        FileWriter fw = new FileWriter("C:\\TEST\\old_Exe\\Publisher.ini");  //empty original
        PrintWriter pw = new PrintWriter(fw);
        pw.write("");
        pw.flush();
        pw.close();
        System.out.println("Empty original Publisher.ini file");
        File inputFile = new File("C:\\TEST\\Publisher.ini");
        File copy = new File("C:\\TEST\\old_Exe\\Publisher.ini");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(copy));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {writer.write(currentLine + System.getProperty("line.separator"));}

        writer.close();
        reader.close();
    }

    public int moveFiles() throws IOException {
        System.out.println("inside method MOVE-FILES Files.move");
        app_folders = excelUserData.getFoldersNamesFromExcelSheet();
        ArrayList l_All_HH = new ArrayList();  //contains all HH
        List files = driverWinMerge.findElementsByAccessibilityId("ListViewSubItem-0");
        for (Object file : files) {
            l_All_HH.add(((RemoteWebElement) file).getText());
        }
        System.out.println("HH size: -> " + l_All_HH.size());
        int newFiles = 0;
        int oldFiles = 0;
        for (int j = 0; j < l_All_HH.size(); j++) {
            Path oldMove = Files.move
                    (Paths.get(app_folders.get(0).get("OldFolderApp") + "\\" + l_All_HH.get(j).toString()),
                            Paths.get(app_folders.get(0).get("OldFolderTest") + "\\" + l_All_HH.get(j).toString()));
            if (oldMove != null) {
                System.out.println("File " + l_All_HH.get(j).toString() + " renamed and moved successfully");
                oldFiles++;
            } else {
                System.out.println("Failed to move the file " + l_All_HH.get(j).toString());
            }
            Path newMove = Files.move
                    (Paths.get(app_folders.get(0).get("NewFolderApp") + "\\" + l_All_HH.get(j).toString()),
                            Paths.get(app_folders.get(0).get("NewFolderTest") + "\\" + l_All_HH.get(j).toString()));
            if (newMove != null) {
                System.out.println("File " + l_All_HH.get(j).toString() + " renamed and moved successfully");
                newFiles++;
            } else {
                System.out.println("Failed to move the file " + l_All_HH.get(j).toString());
            }

        }
        return newFiles + oldFiles;
    }

    public int unzipCommonDataFiles() throws IOException {
        allCommonFoldersFromFile = excelUserData.getFoldersNamesFromExcelSheet();
        String zipFilePath = allCommonFoldersFromFile.get(0).get("Common data");
        String destinationDir = "C:\\UNITAM\\FileMaster\\Files";
        File destDir = new File(destinationDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destinationDir + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        int common = Objects.requireNonNull(new File(destinationDir).list()).length;
        return common;
    }

    public int unzipDailyDataFiles() throws IOException {
        allCommonFoldersFromFile = excelUserData.getFoldersNamesFromExcelSheet();
        String zipFilePath = allCommonFoldersFromFile.get(0).get("Daily data");
        String destinationDir = allCommonFoldersFromFile.get(0).get("Daily data destination");
        File destDir = new File(destinationDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destinationDir + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        int daily = Objects.requireNonNull(new File(destinationDir).list()).length;
        return daily;
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        final int BUFFER_SIZE = 4096;  //Size of the buffer to read/write data
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public void renameFileMasterFolder() throws Exception {
        Thread.sleep(5000);
        File sourceFile = new File("C:\\UNITAM\\FileMaster");
        File destFile = new File("C:\\UNITAM\\FileMaster_OLD");
        if (sourceFile.renameTo(destFile)) {
            System.out.println("Directory FileMaster renamed successfully in FileMaster_OLD");
        } else {
            System.out.println("########## FAILED to rename directory in FileMaster_OLD because folder is already present #########");
        }
        Thread.sleep(12000);
    }

    public void removeDirectory() {
        String dirPath = "C:\\UNITAM\\FileMaster";
        File dir = new File(dirPath);
        try {
            boolean deleted = dir.delete();
            if (deleted) {
                System.out.println("Directory removed");
            } else {
                System.out.println("Directory could not be removed");
            }
        } catch (SecurityException ex) {
            System.out.println("Delete is denied.");
        }
    }

    public int removeDirectory(File dir) {
        if (!dir.exists()) { dir.mkdir();}
        if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {removeDirectory(aFile);}
            }
            dir.delete();
        } else {
            dir.delete();
        }
        return 1;
    }

    public void createFoldersToUnzipCommonData() throws IOException {
        //To create single directory/folder
        allCommonFoldersFromFile = excelUserData.getFoldersNamesFromExcelSheet();
        for (int i = 1; i <= allCommonFoldersFromFile.get(0).size()-11; i++) { //size is 31, 20 are the total folders where unzipped files will be placed
            File file = new File("C:\\UNITAM\\FileMaster\\Files\\"+allCommonFoldersFromFile.get(0).get("CommonFolder"+i));
            if (!file.exists()) {
                if (file.mkdir()) {System.out.println("Directory "+allCommonFoldersFromFile.get(0).get("CommonFolder"+i)+" is created!");
                } else {System.out.println("Failed to create directory "+allCommonFoldersFromFile.get(0).get("CommonFolder"+i)+"!");}
            }else{
                System.out.println("Directory "+allCommonFoldersFromFile.get(0).get("CommonFolder"+i)+" was already created when File Master started");}
        }
    }

    public void renamePolluxGateWayFilesFolders() throws Exception {
        Thread.sleep(3000);
        File ChannelCode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\ChannelCode");
        File _origChannelCode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origChannelCode");
        if(!ChannelCode.exists()){ChannelCode.mkdir();}
        if (ChannelCode.renameTo(_origChannelCode)) {
            System.out.println("Directory ChannelCode renamed successfully in _origChannelCode");
        } else {System.out.println("########## FAILED to rename directory in _origChannelCode because folder is already present #########");}
        File year = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\2021");
        File _origYear = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_orig2021");
        if(!year.exists()){year.mkdir();}
        if (year.renameTo(_origYear)) {
            System.out.println("Directory 2021 renamed successfully in _orig2021");
        } else {System.out.println("########## FAILED to rename directory in _orig2021 because folder is already present #########");}
        File NTACode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\NTACode");
        File _origNTACode = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origNTACode");
        if(!NTACode.exists()){NTACode.mkdir();}
        if (NTACode.renameTo(_origNTACode)) {
            System.out.println("Directory NTACode renamed successfully in _origNTACode");
        } else {System.out.println("########## FAILED to rename directory in _origNTACode because folder is already present #########");}
        File SkyExceptions = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\SkyExceptions");
        File _origSkyExceptions = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origSkyExceptions");
        if(!SkyExceptions.exists()){SkyExceptions.mkdir();}
        if (SkyExceptions.renameTo(_origSkyExceptions)) {
            System.out.println("Directory SkyExceptions renamed successfully in _origSkyExceptions");
        } else {System.out.println("########## FAILED to rename directory in _origSkyExceptions because folder is already present #########");}
        File FromPanelData = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData");
        File _origFromPanelData = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origFromPanelData");
        if(!FromPanelData.exists()){FromPanelData.mkdir();}
        if (FromPanelData.renameTo(_origFromPanelData)) {
            System.out.println("Directory FromPanelData renamed successfully in _origFromPanelData");
        } else {System.out.println("########## FAILED to rename directory in _origFromPanelData because folder is already present #########");}
    }

    public void renamePolluxFromPanelTPIFolder() throws Exception {
        Thread.sleep(3000);
        File sourceFile = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData\\PolluxFromPanelTPI");
        File destFile = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData\\PolluxFromPanelTPI_OLD");
        if(destFile.exists()){
            FileUtils.cleanDirectory(new File(String.valueOf(destFile)));
        }
        if (sourceFile.renameTo(destFile)) {
            System.out.println("Directory PolluxFromPanelTPI renamed successfully in PolluxFromPanelTPI_OLD");
        } else {
            System.out.println("########## FAILED to rename directory in PolluxFromPanelTPI_OLD because folder is already present #########");
        }
        Thread.sleep(2000);
    }

    public int removeFilesFromTestDirectoryOld() throws Exception {
        //FILE TO COMPARE wth old app ---> C:\TEST\REGRESSION POLLUXGW\oldAppVersion
        foldersPollux = excelUserData.getPolluxGWDataFromFile();
        removeDirectory(new File(foldersPollux.get(0).get("OldFolderApp")));
        return 1;
    }

    public int cancelPolluxOldExeFileFromUnitamSWFolder() throws IOException {
        app_new_vers = excelUserData.getPolluxGWDataFromFile();
        Path temp = null;
        try {
            temp = Files.move
                    (Paths.get(app_new_vers.get(0).get("RenamePolluxVersion")),
                            Paths.get(app_new_vers.get(0).get("oldExe")+"\\PolluxGateway_OLD_VERS.exe"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (temp != null) {
            System.out.println("File PolluxGateway_OLD_VERS.exe renamed and moved successfully to C:\\TEST\\old_Exe");
        } else {
            System.out.println("Failed to move the file PolluxGateway_OLD_VERS.exe");
        }
        int e = Objects.requireNonNull(new File(app_new_vers.get(0).get("oldExe")).list()).length;
        return e;
    }
}
