package com.example.mohamedabdelaziz.moslem;

/**
 * Created by Mohamed Abd ELaziz on 7/7/2017.
 */

public class attachments {
   private String order ,title,duration,size,url;

    public attachments(String order, String title, String duration, String size, String url) {
        this.order = order;
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.url = url;
    }

    public String getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }
}
