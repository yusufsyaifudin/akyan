package akyan.nlp.news.helpers.locationmodel;

/**
 * Created by yusuf on 07/11/16.
 */
public class DistrictCounter {
    private String id;
    private String regency_id;
    private String name;
    private String alt_name;
    private Double latitude;
    private Double longitude;
    private Integer count = 0;

    public DistrictCounter() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegency_id() {
        return this.regency_id;
    }

    public void setRegency_id(String id) {
        this.regency_id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt_name() {
        return this.alt_name;
    }

    public void setAlt_name(String alt_name) {
        this.alt_name = alt_name;
    }

    public Double getLatitude() {
        return (latitude == null) ? 0 : latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return (longitude == null) ? 0 : longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getCount() {
        return (count == null) ? 0 : count;
    }

    public void setCount(Integer count) {
        if(this.count > 0) {
            this.count = this.count + count;
        } else {
            this.count = count;
        }
    }

    public String toString() {
        return "{id: " + this.getId().toString() +
                ", regency_id: " + this.getRegency_id().toString() +
                ", name: " + this.getName() + ", " +
                ", alt_name: " + this.getAlt_name() +
                ", latitude: " + this.getLatitude().toString() +
                ", longitute: " + this.getLongitude().toString() +
                "}";
    }
}
