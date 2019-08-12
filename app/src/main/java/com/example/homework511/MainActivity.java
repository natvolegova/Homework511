package com.example.homework511;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 10; //пользовательская переменная, определяем права доступа на чтение
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11; //пользовательская переменная, определяем права доступа на запись
    public static final String FILE_TASK = "task_list.txt";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private LinearLayout addLayout;
    private Button btnAddTask;
    private ListView main_content;
    private File fileTask;
    private ItemListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        myListAdapter = new ItemListAdapter(this, null);
        main_content.setAdapter(myListAdapter);

        //открывем окно для добавления новой задачи
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAddTask();
            }
        });
        //добавляем новую задачу
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                EditText etTitle = findViewById(R.id.et_title);
                EditText etSubTitle = findViewById(R.id.et_subtitle);
                EditText etResponsible = findViewById(R.id.et_responsible);
                Spinner spListTask = findViewById(R.id.sp_list_task);
                //формируем итоговую строку для добавления данных и записываем в файл
                String result = etTitle.getText().toString() + ";" + etSubTitle.getText().toString() + ";" + etResponsible.getText().toString() + ";" + spListTask.getSelectedItemId() + "\n";
                appendTask(result);


                //закрываем окно
                addLayout.setVisibility(View.GONE);
                toolbar.setTitle(getResources().getString(R.string.app_name));
                fab.setVisibility(View.VISIBLE);
                main_content.setVisibility(View.VISIBLE);
                etTitle.setText("");
                etSubTitle.setText("");
                etResponsible.setText("");
                spListTask.setSelection(0);

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        addLayout = findViewById(R.id.add_layout);
        btnAddTask = findViewById(R.id.btn_add_task);
        main_content = findViewById(R.id.main_content);
        //проверяем доступ для записи во внешнее хранилище и создаем файл для записи задач
        int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //доступ разрешен, создаем файл
            createTaskFile();
        } else {
            //еше не было проверки, запрашиваем доступ
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }
    }

    //создаем файл для хранения данных
    private void createTaskFile() {
        if (isExternalStorageWritable()) {
            fileTask = new File(getApplicationContext().getExternalFilesDir(null), FILE_TASK);
            if (!fileTask.exists()) {
                try {
                    fileTask.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(this, fileTask.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "File Error", Toast.LENGTH_LONG).show();
        }
    }

    //открываем layout для добавления новой задачи
    @SuppressLint("RestrictedApi")
    private void initAddTask() {
        addLayout.setVisibility(View.VISIBLE);
        toolbar.setTitle(getResources().getString(R.string.new_task));
        fab.setVisibility(View.GONE);
        main_content.setVisibility(View.GONE);
    }

    //добавление задачи в файл и в список элементов
    private void appendTask(String result) {
        try {
            FileWriter logWriter = new FileWriter(fileTask, true);
            logWriter.append(result);
            logWriter.close();
            myListAdapter.add_item(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //методы для проверки доступа
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE: //пользовательская переменная, доступ на запись
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createTaskFile();
                } else {
                    //request denied
                    Toast.makeText(this, getResources().getString(R.string.msg_request_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
