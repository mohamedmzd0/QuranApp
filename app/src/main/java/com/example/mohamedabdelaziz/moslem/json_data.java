package com.example.mohamedabdelaziz.moslem;

/**
 * Created by Mohamed Abd ELaziz on 7/7/2017.
 */

public class json_data {

private String title , api_url ;
prepared_by prepared ;
    public json_data(String title, String api_url,prepared_by prepared) {
        this.title = title;
        this.api_url = api_url;
        this.prepared=prepared ;
    }

    public String getTitle() {
        return title;
    }

    public String getApi_url() {
        return api_url;
    }

}
class prepared_by {
    private String title ;
    public prepared_by(String title)
    {
        this.title=title ;
    }

    public String getTitle() {
        return title;
    }
}
