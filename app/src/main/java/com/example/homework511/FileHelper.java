package com.example.homework511;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHelper {
    Context context;
    private String fileName;
    private File fileTask;


    public FileHelper(Context context, String file) {
        this.context = context;
        this.fileName = file;
    }

    //создаем файл для хранения данных
    public void createFile() {
        if (isExternalStorageWritable()) {
            fileTask = new File(fileName);
            if (!fileTask.exists()) {
                try {
                    fileTask.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, fileTask.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "File Error", Toast.LENGTH_LONG).show();
        }
    }

    public void addToFile(String line) {
        try {
            FileWriter textFileWriter = new FileWriter(fileTask, true);
            textFileWriter.write(line);
            textFileWriter.flush();
            textFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromFile(int num, File fileTask) {
        List<String> lines = new ArrayList<>();

        try {
            Scanner reader = new Scanner(new FileInputStream(fileTask), "UTF-8");
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();
            lines.remove(num);
            FileWriter textFileWriter = new FileWriter(fileTask, false);
            for (String line : lines) {
                textFileWriter.write(line + "\n");
            }
            textFileWriter.flush();
            textFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //методы для проверки доступа
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}
