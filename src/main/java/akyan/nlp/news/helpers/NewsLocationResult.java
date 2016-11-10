package akyan.nlp.news.helpers;

import java.util.ArrayList;

/**
 * Created by yusuf on 10/11/16.
 */
public class NewsLocationResult {
    private ArrayList<LocationModel> location;
    private String html;

    public ArrayList<LocationModel> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<LocationModel> location) {
        this.location = location;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
