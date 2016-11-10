package akyan.nlp.news.extractor;

import akyan.nlp.news.helpers.LocationModel;
import akyan.nlp.news.helpers.MapUtil;
import akyan.nlp.news.helpers.locationmodel.DistrictCounter;
import akyan.nlp.news.helpers.locationmodel.ProvinceCounter;
import akyan.nlp.news.helpers.locationmodel.RegencyCounter;
import org.apache.log4j.Logger;
import wilayah.indonesia.Location;
import wilayah.indonesia.model.District;
import wilayah.indonesia.model.Province;
import wilayah.indonesia.model.Regency;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static akyan.nlp.news.helpers.RegexUtil.*;

/**
 * Created by yusuf on 06/11/16.
 */
public class NewsLocation {
    private final Logger logger = Logger.getLogger(NewsLocation.class);

    private List<Province> PROVINCES;
    private Location.TREE_LEVEL TREE_LEVEL;
    private String[] exclude = {};
    private LinkedHashMap<String, String> taggedDataMap  = new LinkedHashMap<>();
    private String originalText;

    public NewsLocation() {
        try {
            TREE_LEVEL = Location.TREE_LEVEL.DISTRICT;
            PROVINCES = Location.ProvinceTrees(Location.TREE_LEVEL.DISTRICT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(String[] exclude) {
        try {
            this.exclude = exclude;
            TREE_LEVEL = Location.TREE_LEVEL.DISTRICT;
            PROVINCES = Location.ProvinceTrees(Location.TREE_LEVEL.DISTRICT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(Location.TREE_LEVEL tree_level) {
        try {
            TREE_LEVEL = tree_level;
            PROVINCES = Location.ProvinceTrees(tree_level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(String[] exclude, Location.TREE_LEVEL tree_level) {
        try {
            this.exclude = exclude;
            TREE_LEVEL = tree_level;
            PROVINCES = Location.ProvinceTrees(tree_level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Will return ArrayList of LocationModel
     * LocationModel is model like wilayah.indonesia.model.Province but contain total and count of founded data
     * @param news_text
     * @return ArrayList<LocationModel>
     */
    public ArrayList<LocationModel> UseDictionaryLookUp(String news_text) {
        this.originalText = news_text; // set to current context variable
        ArrayList<LocationModel> locationModels = new ArrayList<>();

        PROVINCES.forEach(p -> {
            LocationModel locationModel = new LocationModel();
            ProvinceCounter localProvinceCounter = LocationModel.toProvinceCounter(p);

            String provinceName = str_ireplace(exclude, "", p.getName());
            int countProvinceName = preg_match_all(news_text, "(?i)\\b" + preg_quote(provinceName) + "\\b");

            if(countProvinceName > 0) {
                localProvinceCounter.setCount(countProvinceName);
                locationModel.setTotal(countProvinceName); // always increment the total value

                // add to map
                if( taggedDataMap.containsKey(provinceName.toLowerCase()) ) {
                    taggedDataMap.put(provinceName.toLowerCase(),
                            taggedDataMap.get(provinceName.toLowerCase()) + " province-" + p.getId());
                } else {
                    taggedDataMap.put(provinceName.toLowerCase(), " province-" + p.getId());
                }
            }

            // lookup in regency level
            ArrayList<RegencyCounter> regencyCounterArrayList = new ArrayList<>();
            p.getRegencies().forEach(r -> {

                ArrayList<DistrictCounter> districtCounterArrayList = new ArrayList<>();
                r.getDistricts().forEach(d -> {
                    DistrictCounter districtCounter = LookUpDistrict(d, news_text);
                    if(districtCounter != null && districtCounter.getCount() > 0) {
                        // always increment the total value it count more than 0
                        locationModel.setTotal(districtCounter.getCount());
                        districtCounterArrayList.add(districtCounter);
                    }
                });

                RegencyCounter regencyCounter = LookUpRegency(r, news_text);

                // if regency counter return null, but have a child (district) that not null,
                // we must made regency counter object from current wilayah.indonesia.model.Regency object
                if(regencyCounter == null && !districtCounterArrayList.isEmpty()) {
                    regencyCounter = LocationModel.toRegencyCounter(r);
                }

                // then we must check if regency counter is really not null
                // (because of && operand, there is still have possibilities to null if districtCounterArrayList is empty)
                // [true && !true] is same [true && false] and will return [false]
                if(regencyCounter != null) {
                    regencyCounter.setDistricts(districtCounterArrayList);
                    locationModel.setTotal(regencyCounter.getCount()); // always increment the total value
                    regencyCounterArrayList.add(regencyCounter);
                }
            });

            localProvinceCounter.setRegencies(regencyCounterArrayList);
            locationModel.setProvince(localProvinceCounter);

            // only return if total is more than zero
            if(locationModel.getTotal() > 0) {
                locationModels.add(locationModel);
            }
        });

        // sorting
        locationModels.sort((o1, o2) -> o2.getTotal().compareTo(o1.getTotal()));

        return locationModels;
    }

    public LinkedHashMap<String, String> getTaggedDataMap() {
        taggedDataMap = (LinkedHashMap<String, String>) MapUtil.sortByKeyStringLength(taggedDataMap);
        return taggedDataMap;
    }

    public String getHtml() {
        String html = this.originalText;
        // new data map for save original string, taggedDataMap is used to save founded regex no case sensitive
        HashMap<String, String> newDataMap = new HashMap<>();

        for(Map.Entry<String, String> dataMap : getTaggedDataMap().entrySet()) {
            String regex = "(?i)\\b" + dataMap.getKey().toLowerCase().trim() + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(html);

            while (matcher.find()) {
                String originalToken = matcher.group();
                newDataMap.put(originalToken, dataMap.getValue().trim());
            }
        }

        newDataMap = (LinkedHashMap<String, String>) MapUtil.sortByKeyStringLength(newDataMap);

        for(Map.Entry<String, String> dataMap : newDataMap.entrySet()) {
            String replacer = "<span class=\"" + dataMap.getValue().trim() + "\"" + ">" + dataMap.getKey().trim() + "</span>";
            html = str_replace(new String[]{dataMap.getKey().trim()}, replacer, html);
        }

        return html;
    }

    private RegencyCounter LookUpRegency(Regency regency, String news_text) {
        // if tree level is province then return null, otherwise let it be processed
        if(TREE_LEVEL.equals(Location.TREE_LEVEL.PROVINCE)) {
            return null;
        }

        String regencyName = str_ireplace(exclude, "", regency.getName());
        // the regex is: (?i)\bsukoharjo\b
        int countRegencyName = preg_match_all(news_text, "(?i)\\b" + preg_quote(regencyName) + "\\b");
        if(countRegencyName > 0) {
            RegencyCounter tmp = LocationModel.toRegencyCounter(regency);
            tmp.setCount(countRegencyName);

            // add to map
            if( taggedDataMap.containsKey(regencyName.toLowerCase()) ) {
                taggedDataMap.put(regencyName.toLowerCase(),
                        taggedDataMap.get(regencyName.toLowerCase()) + " regency-" + regency.getId());
            } else {
                taggedDataMap.put(regencyName.toLowerCase(), " regency-" + regency.getId());
            }

            return tmp;
        }

        return null;
    }

    private DistrictCounter LookUpDistrict(District district, String news_text) {
        // if tree level not district, then return null
        if(!TREE_LEVEL.equals(Location.TREE_LEVEL.DISTRICT)) {
            return null;
        }

        String districtName = str_ireplace(exclude, "", district.getName());
        int countDistrictName = preg_match_all(news_text, "(?i)\\b" + preg_quote(districtName) + "\\b");
//        logger.debug(countDistrictName);
        if(countDistrictName > 0) {
            DistrictCounter tmp = LocationModel.toDistrictCounter(district);
            tmp.setCount(countDistrictName);

            if( taggedDataMap.containsKey(districtName.toLowerCase()) ) {
                taggedDataMap.put(districtName.toLowerCase(),
                        taggedDataMap.get(districtName.toLowerCase()) + " district-" + district.getId());
            } else {
                taggedDataMap.put(districtName.toLowerCase(), " district-" + district.getId());
            }
            return tmp;
        }

        return null;
    }



}
