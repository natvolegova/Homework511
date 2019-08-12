package com.example.homework511;

public class ItemData {
    private String title;
    private String subtitle;
    private String responsible;
    private int img_src;

    public ItemData(String title, String subtitle, String responsible, int img_src) {
        this.title = title;
        this.subtitle = subtitle;
        this.responsible = responsible;
        this.img_src = img_src;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getResponsible() {
        return responsible;
    }

    public int getImg_src() {
        return img_src;
    }
}
