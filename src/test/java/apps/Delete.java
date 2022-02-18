package apps;

import core.excelUserData;
import org.apache.commons.io.FileUtils;
import tests.TestBase;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Delete extends TestBase {

    public static List<Map<String,String>> app_folders;

    public int deleteFiles() throws Exception {
        System.out.println("@@@ DELETING FOLDERS BEFORE STARTING THE NEW REGRESSION @@@");
        app_folders = excelUserData.getPolluxGWDataFromFile();
        File oldDir = new File(app_folders.get(0).get("OldFolderPolluxApp"));
        if (!oldDir.exists()){oldDir.mkdirs();}
        File newDir = new File(app_folders.get(0).get("NewFolderPolluxApp"));
        if (!newDir.exists()){newDir.mkdirs();}
        FileUtils.cleanDirectory(new File(app_folders.get(0).get("OldFolderPolluxApp")));
        FileUtils.cleanDirectory(new File(app_folders.get(0).get("NewFolderPolluxApp")));
        File oldExe = new File(app_folders.get(0).get("oldExe"));
        if (!oldExe.exists()){oldExe.mkdirs();}
        FileUtils.cleanDirectory(oldExe);
        File screen = new File(app_folders.get(0).get("screenShots"));
        if (!screen.exists()){screen.mkdirs();}
        FileUtils.cleanDirectory(screen);
        File report = new File(app_folders.get(0).get("Report"));
        if (!report.exists()){report.mkdirs();}
        FileUtils.cleanDirectory(report);
        File FromPanelData = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData");
        if (!FromPanelData.exists()){FromPanelData.mkdirs();}
        FileUtils.cleanDirectory(FromPanelData);
        File oldFromPanelData = new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\_origFromPanelData");
        if (!oldFromPanelData.exists()){oldFromPanelData.mkdirs();}
        FileUtils.cleanDirectory(oldFromPanelData);
        System.out.println("All folders are empty");
        int n = Objects.requireNonNull(new File(app_folders.get(0).get("NewFolderPolluxApp")).list()).length;
        int o = Objects.requireNonNull(new File(app_folders.get(0).get("OldFolderPolluxApp")).list()).length;
        int r = Objects.requireNonNull(new File(app_folders.get(0).get("Report")).list()).length;
        int e = Objects.requireNonNull(new File(app_folders.get(0).get("oldExe")).list()).length;
        int p = Objects.requireNonNull(new File(app_folders.get(0).get("screenShots")).list()).length;
        int s = Objects.requireNonNull(new File("C:\\UNITAM\\PolluxGateway\\Panel_0\\Files\\FromPanelData").list()).length;
        return n+o+s+r+e+p;
    }


}
