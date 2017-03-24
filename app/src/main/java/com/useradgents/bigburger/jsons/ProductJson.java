package com.useradgents.bigburger.jsons;

/**
 * Created by AUBERT on 12/03/2017.
 */

public class ProductJson {
    private int ref;
    private int price;
    private String title;
    private String thumbnail;
    private String description;

    public int getRef() {
        return ref;
    }

    public int getPrice() {
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setRef(final int ref) {
        this.ref = ref;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setThumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
