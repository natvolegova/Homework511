package com.example.homework511;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 11; //пользовательская переменная, определяем права доступа на запись
    public static final String FILE_TASK = "task_list.txt";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private LinearLayout addLayout;
    private Button btnAddTask;
    private ListView mainContent;
    private FileHelper tFileHelper;
    private ItemListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //определяем путь к хранению файла
        String filePatch = getApplicationContext().getExternalFilesDir(null).toString();
        final String fileSrc = filePatch + "/" + FILE_TASK;
        //создаем обработчик для файла
        tFileHelper = new FileHelper(MainActivity.this, fileSrc);

        //Добавление файла
        int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            tFileHelper.createFile();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        myListAdapter = new ItemListAdapter(this, null, fileSrc);
        myListAdapter.prepareContent();
        mainContent.setAdapter(myListAdapter);

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
                tFileHelper.addToFile(result);
                myListAdapter.add_item(result);
                //закрываем окно
                addLayout.setVisibility(View.GONE);
                toolbar.setTitle(getResources().getString(R.string.app_name));
                fab.setVisibility(View.VISIBLE);
                mainContent.setVisibility(View.VISIBLE);
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
        mainContent = findViewById(R.id.main_content);
    }

    //открываем layout для добавления новой задачи
    @SuppressLint("RestrictedApi")
    private void initAddTask() {
        addLayout.setVisibility(View.VISIBLE);
        toolbar.setTitle(getResources().getString(R.string.new_task));
        fab.setVisibility(View.GONE);
        mainContent.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE: //пользовательская переменная, доступ на запись
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tFileHelper.createFile();
                } else {
                    //request denied
                    Toast.makeText(this, getResources().getString(R.string.msg_request_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
