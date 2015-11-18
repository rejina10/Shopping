package com.android.shopping.settings;

import android.content.Context;
import android.os.Environment;

import com.android.shopping.database.DatabaseQueryManager;

import java.io.File;

/**
 * Created by rejina on 11/15/2015.
 */
public class ApplicationSettings {

    private static String SDCARD_APP_PATH;
    public static String APP_DATABASE_PATH;
    public static final String DB_NAME = "db_shopping";

    static {
        SDCARD_APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/Data/com.android.shopping" + "/data";
        APP_DATABASE_PATH = SDCARD_APP_PATH + "/database/" + DB_NAME;
    }

    public static void CreateApplicationFolder() {
        try {
            Create_Folders(ApplicationSettings.SDCARD_APP_PATH, "database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Create_Folders(String baseDirPath, String folderName) throws Exception {
        File o_base_dir = new File(baseDirPath);
        if (!o_base_dir.exists()) {
            CreateDirs(o_base_dir);
        }
        File o_dir = new File(o_base_dir, folderName);
        CreateDirs(o_dir);
    }

    public static boolean CreateDirs(File directory) {
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }

    public static DatabaseQueryManager dbAccessor;

    public static int initDBAccessor(Context context) {
        dbAccessor = new DatabaseQueryManager(context);
        return 0;
    }
}
