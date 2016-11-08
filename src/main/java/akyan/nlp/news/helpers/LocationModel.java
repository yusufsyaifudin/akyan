package akyan.nlp.news.helpers;

import akyan.nlp.news.helpers.locationmodel.DistrictCounter;
import akyan.nlp.news.helpers.locationmodel.ProvinceCounter;
import akyan.nlp.news.helpers.locationmodel.RegencyCounter;
import wilayah.indonesia.model.District;
import wilayah.indonesia.model.Province;
import wilayah.indonesia.model.Regency;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by yusuf on 06/11/16.
 */
public class LocationModel {

    private ProvinceCounter province;
    private Integer total;


    public ProvinceCounter getProvince() {
        return (province == null) ? new ProvinceCounter() : province;
    }

    public void setProvince(ProvinceCounter p) {
        province = p;
    }

    public Integer getTotal() {
        return (total == null) ? 0 : total;
    }

    public void setTotal(Integer total) {
        if (this.total == null) {
            this.total = total;
        } else {
            this.total += total;
        }
    }

    public String toString() {
        return "{province: " + getProvince().toString() +
                ", count: " + getTotal().toString() +
                "}";
    }

    public static ProvinceCounter toProvinceCounter(Province province) {
        ProvinceCounter p = new ProvinceCounter();
        p.setId(province.getId());
        p.setName(province.getName());
        p.setAlt_name(province.getAlt_name());
        p.setLatitude(province.getLatitude());
        p.setLongitude(province.getLongitude());

        ArrayList<RegencyCounter> regencyCounters = province.getRegencies().stream().map(
                LocationModel::toRegencyCounter
        ).collect(Collectors.toCollection(ArrayList::new));

        p.setRegencies(regencyCounters);
        return p;
    }

    public static RegencyCounter toRegencyCounter(Regency regency) {
        RegencyCounter r = new RegencyCounter();
        r.setId(regency.getId());
        r.setProvince_id(regency.getProvince_id());
        r.setName(regency.getName());
        r.setAlt_name(regency.getAlt_name());
        r.setLatitude(regency.getLatitude());
        r.setLongitude(regency.getLongitude());
        r.setCount(0);

        ArrayList<DistrictCounter> districtCounters = regency.getDistricts().stream().map(
                LocationModel::toDistrictCounter
        ).collect(Collectors.toCollection(ArrayList::new));

        r.setDistricts(districtCounters);
        return r;
    }

    public static DistrictCounter toDistrictCounter(District district) {
        DistrictCounter d = new DistrictCounter();
        d.setId(district.getId());
        d.setRegency_id(district.getRegency_id());
        d.setName(district.getName());
        d.setAlt_name(district.getAlt_name());
        d.setLatitude(district.getLatitude());
        d.setLongitude(district.getLongitude());
        d.setCount(0);

        return d;
    }




}
