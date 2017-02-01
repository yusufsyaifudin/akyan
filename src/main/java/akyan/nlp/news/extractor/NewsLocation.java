package akyan.nlp.news.extractor;

import akyan.nlp.news.helpers.LocationModel;
import akyan.nlp.news.helpers.MapUtil;
import akyan.nlp.news.helpers.NewsLocationResult;
import akyan.nlp.news.helpers.locationmodel.DistrictCounter;
import akyan.nlp.news.helpers.locationmodel.ProvinceCounter;
import akyan.nlp.news.helpers.locationmodel.RegencyCounter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import wilayah.indonesia.Location;
import wilayah.indonesia.model.District;
import wilayah.indonesia.model.Province;
import wilayah.indonesia.model.Regency;
import yusufs.generator.randstring.RandomStringGenerator;
import yusufs.nlp.nerid.NERModel;
import yusufs.nlp.nerid.Prediction;
import yusufs.nlp.nerid.utils.TextSequence;

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

    public NewsLocation() {
        try {
            TREE_LEVEL = Location.TREE_LEVEL.DISTRICT;
            PROVINCES = Location.ProvinceTrees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(String[] exclude) {
        try {
            this.exclude = exclude;
            TREE_LEVEL = Location.TREE_LEVEL.DISTRICT;
            PROVINCES = Location.ProvinceTrees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(Location.TREE_LEVEL tree_level) {
        try {
            TREE_LEVEL = tree_level;
            PROVINCES = Location.ProvinceTrees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsLocation(String[] exclude, Location.TREE_LEVEL tree_level) {
        try {
            this.exclude = exclude;
            TREE_LEVEL = tree_level;
            PROVINCES = Location.ProvinceTrees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Will return NewsLocationResult
     * Contain total and count of founded data and html
     * @param news_text
     * @return NewsLocationResult
     */
    public NewsLocationResult UseDictionaryLookUp(String news_text) {
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

        NewsLocationResult newsLocationResult = new NewsLocationResult();
        newsLocationResult.setLocation(locationModels);
        newsLocationResult.setHtml(Htmlize(news_text, taggedDataMap));

        return newsLocationResult;
    }

    /**
     * System will predict the NER tag before looking for existence using UseDictionaryLookUp
     * @param news_text
     * @param withPunctuation
     * @param nerModel
     * @return NewsLocationResult
     */
    public NewsLocationResult UseHmmPrediction(String news_text, Boolean withPunctuation, NERModel nerModel) {
        TextSequence textSequence = new TextSequence();

        Prediction prediction = new Prediction();
        ArrayList<TextSequence.Sentence> sentences = prediction.predict(news_text, withPunctuation, nerModel);

        StringBuilder locationString = new StringBuilder();
        String new_news_text = news_text;

        HashMap<String, String> replacedLocationString = new HashMap<>();

        for (TextSequence.Sentence sentence : sentences) {
            for (TextSequence.Words word : sentence.getWords()) {
                if(word.getXmlTag().equals("LOCATION")) {
                    String token = word.getToken();
                    locationString.append(token + " ");

                    // Matched word should be replaced as random string.
                    // For later use, the replaced string must be saved.
                    String randomString = RandomStringGenerator.generateRandomString(10, RandomStringGenerator.Mode.ALPHANUMERIC);

                    // if have same key,
                    if (replacedLocationString.containsKey(token) == false) {
                        replacedLocationString.put(token, randomString);

                        // then use old random string to remove, instead create a new one
                        randomString = replacedLocationString.get(token);
                    }

                    // this will ensure replaced string will still same as long as they have same key (word)
                    new_news_text = new_news_text.replace(token, randomString);
                }
            }
        }

        String locations = locationString.toString().trim();
        logger.info(locations);

        NewsLocationResult result = UseDictionaryLookUp(locations);

        // The this.taggedDataMap is a hash map contain location word (i.e "DI Yogyakarta") as key
        // and id of location for class name (i.e "province-34")
        // so the key must be changed to matched random string in variable
        HashMap<String, String> newTaggedDataMap  = new HashMap<>();
        for(Map.Entry<String, String> data : replacedLocationString.entrySet()) {
            String key = data.getKey().toLowerCase(); // this is original word string in lowercase
            String value = data.getValue(); // this is random string

            // only tagged
            if(this.taggedDataMap.containsKey(key)) {
                // changed from "yogyakarta" => "province-34" to "randomstring" => "province-34"
                newTaggedDataMap.put(value, this.taggedDataMap.get(key));
            }
        }

        logger.debug("matched data in database: " + this.taggedDataMap);
        logger.debug("replaced key as random string from map: " + newTaggedDataMap);
        logger.debug("words predicted as location: " + replacedLocationString);

        // manipulate html response to full text,
        // if this not run, html will return only partial data that tagged as LOCATION
        String html = Htmlize(new_news_text, newTaggedDataMap);

        // now variable `html` have a string that have been tagged using html tag (<span>)
        // we need replaced all random string generated by random generator using appropriate word
        for(Map.Entry<String, String> data : replacedLocationString.entrySet()) {
            String key = data.getKey();
            String value = data.getValue();

            html = html.replace(value, key);
        }

        result.setHtml(html);
        return result;
    }

    private String Htmlize(String text, HashMap<String, String> taggedDataMap) {
        taggedDataMap = (LinkedHashMap<String, String>) MapUtil.sortByKeyStringLength(taggedDataMap);
        // new data map for save original string, taggedDataMap is used to save founded regex no case sensitive
        HashMap<String, String> newDataMap = new HashMap<>();

        for(Map.Entry<String, String> dataMap : taggedDataMap.entrySet()) {
            String regex = "(?i)\\b" + dataMap.getKey().toLowerCase().trim() + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String originalToken = matcher.group();
                newDataMap.put(originalToken, dataMap.getValue().trim());
            }
        }

        newDataMap = (LinkedHashMap<String, String>) MapUtil.sortByKeyStringLength(newDataMap);

        for(Map.Entry<String, String> dataMap : newDataMap.entrySet()) {
            String replacer = "<span class=\"" + dataMap.getValue().trim() + "\"" + ">" + dataMap.getKey().trim() + "</span>";
            text = str_replace(new String[]{dataMap.getKey().trim()}, replacer, text);
        }

        return text;
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
