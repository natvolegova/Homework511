package com.example.homework511;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ItemListAdapter extends BaseAdapter {
    private List<ItemData> items;
    private LayoutInflater layoutInflater;
    private Context context;
    private String fileTask;
    //параметр для картинок
    private int[] imageSrc = {R.drawable.ic_laptop_24px, R.drawable.ic_phone_android_24px, R.drawable.ic_tablet_android_24px};


    // Конструктор, в который передается контекст
    ItemListAdapter(Context context, List<ItemData> items, String fileTask) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.fileTask = fileTask;
    }

    public void add_item(String result) {
        String[] resultArr = result.split(";");
        items.add(new ItemData(resultArr[0], resultArr[1], resultArr[2], Integer.parseInt(resultArr[3].trim())));
        notifyDataSetChanged();
    }

    // удаление элемента списка
    private void removeItem(int position) {
        FileHelper fh = new FileHelper(context, fileTask);
        File file = new File(fileTask);
        fh.deleteFromFile(position, file);
        items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ItemData getItem(int i) {
        if (i < items.size()) {
            return items.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        //если нет данных о view, то создаем новую вьюху из item_list_view, но не добавляем ее на экран, а только считываем параметры
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_list_view, viewGroup, false);
        }
        ItemData item = items.get(i);
        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());
        TextView subtitle = view.findViewById(R.id.subtitle);
        subtitle.setText(item.getSubtitle());
        TextView responsible = view.findViewById(R.id.responsible);
        responsible.setText(item.getResponsible());
        ImageView img = view.findViewById(R.id.image);
        img.setImageResource(imageSrc[item.getImg_src()]);
        ImageButton btn_delete = view.findViewById(R.id.btn_delete);
        btn_delete.setFocusable(false); //без этого свойсва обработчик на элементе списка не работает
        btn_delete.setOnClickListener(delete_item); //добавляем обработчик нажатия кнопки удаления
        btn_delete.setTag(i); //без этого кнопка удаления не работает
        return view;
    }

    //обработчик кнопки, удялем элемент списка
    private ImageButton.OnClickListener delete_item = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            removeItem((Integer) view.getTag());
        }
    };
    // заполняем данными из файла адаптер
    public void prepareContent() {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(fileTask);
            Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();
            for (String line : lines) {
                String[] item = line.split(";");
                items.add(new ItemData(item[0], item[1], item[2], Integer.parseInt(item[3].trim())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }
}
