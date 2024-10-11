package com.srbenicio.slist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.srbenicio.slist.creators.DatabaseCreator;

import java.io.*;
import java.nio.channels.FileChannel;

public class DatabaseExporter {

    public static boolean exportDatabase(Context context) {
        // Try grant that is closes
        DatabaseCreator dbHelper = DatabaseCreator.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();

        File currentDB = context.getDatabasePath(DatabaseCreator.DATABASE_NAME);
        File exportDir = context.getExternalFilesDir(null);

        if (!currentDB.exists()) {return false;}

        try {
            File backupDB = new File(exportDir, "SList_backup.db");
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            // Transfer bytes from in to out
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    public static void importDatabase(Context context) {
        // Get the path to the backup file in external files directory
        File importFile = new File(context.getExternalFilesDir(null), "SList_backup.db");

        if (importFile.exists()) {
            File dbFile = context.getDatabasePath("SList.db");

            try {
                copyFile(importFile, dbFile);
                Toast.makeText(context, "Database imported successfully", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to import database:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Backup file not found", Toast.LENGTH_LONG).show();
        }
    }

}
