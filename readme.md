# Akyan

[![Build Status](https://travis-ci.org/yusufsyaifudin/akyan.svg?branch=master)](https://travis-ci.org/yusufsyaifudin/akyan)

> Etymology: `Akyan` is from Sanskrit language which mean "eyes" or "vision"

Akyan is libary for extracting some information from news in Bahasa Indonesia. This is early stage of Akyan development, and currently Akyan have one feature:

* Extract location mentioned in news.


## Table of contents
  * [Table of contents](#table-of-contents)
  * [Installation](#installation)
  * [APIs](#apis)
    * [Extract Location](#extract-location)
    * [Using HMM Prediction](#using-hmm-prediction)
    * [API Note](#api-note)



## Installation

You can use jitpack and install dependency from active branch `master`.

Add this to repository list:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

And this to dependency list:
```xml
<dependencies>
    <dependency>
      <groupId>com.github.yusufsyaifudin</groupId>
      <artifactId>akyan</artifactId>
      <version>0.0.1</version>
    </dependency>
</dependencies>
```

After installation please copy data inside `resources` in this project into your `resources` directory in your root project.

## APIs

### Extract Location

```
import akyan.nlp.news.extractor;
import akyan.nlp.news.helpers.LocationModel;
import wilayah.indonesia.Location;

// you can select dependency level to look up
NewsLocation newsLocation = new NewsLocation(Location.TREE_LEVEL.DISTRICT);

// source http://travel.kompas.com/read/2016/10/22/110900827/berangan-angan.di.hutan.pinus.mangunan
// accessed at: November 8th, 2016 02:45PM
String text = "Hutan Pinus Mangunan di Desa Dlingo, Mangunan, Kabupaten Bantul, DI Yogyakarta terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana.";

NewsLocationResult result = newsLocation.UseDictionaryLookUp(text);
```

The `result` variable will return this value (using `Gson`):

```json
{
  "location": [
    {
      "province": {
        "id": "34",
        "name": "DI YOGYAKARTA",
        "alt_name": "DI YOGYAKARTA",
        "latitude": 7.7956,
        "longitude": 110.3695,
        "count": 1,
        "regencies": [
          {
            "id": "3402",
            "province_id": "34",
            "name": "KABUPATEN BANTUL",
            "alt_name": "KABUPATEN BANTUL",
            "latitude": -7.9,
            "longitude": 110.36667,
            "count": 1,
            "districts": [
              {
                "id": "3402070",
                "regency_id": "3402",
                "name": "BANTUL",
                "alt_name": "Bantul, Bantul Regency, Special Region of Yogyakarta, Indonesia",
                "latitude": -7.89146,
                "longitude": 110.33615,
                "count": 1
              },
              {
                "id": "3402100",
                "regency_id": "3402",
                "name": "DLINGO",
                "alt_name": "Dlingo, Bantul Regency, Special Region of Yogyakarta, Indonesia",
                "latitude": -7.91915,
                "longitude": 110.46106,
                "count": 1
              }
            ]
          }
        ]
      },
      "total": 4
    }
  ],
  "html": "Hutan Pinus Mangunan di Desa <span class=\"district-3402100\">Dlingo<\/span>, Mangunan, <span class=\"regency-3402\">Kabupaten <span class=\"district-3402070\">Bantul<\/span><\/span>, <span class=\"province-34\">DI Yogyakarta<\/span> terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana."
}
```

If you want location in tree dependency, you can acess it using:

```java
ArrayList<LocationModel> treeLocation = result.getLocation();
```

Convert the `treeLocation` variable into json using `Gson` library, you will see the output like this:

```json
[
  {
    "province": {
      "id": "34",
      "name": "DI YOGYAKARTA",
      "alt_name": "DI YOGYAKARTA",
      "latitude": 7.7956,
      "longitude": 110.3695,
      "count": 1,
      "regencies": [
        {
          "id": "3402",
          "province_id": "34",
          "name": "KABUPATEN BANTUL",
          "alt_name": "KABUPATEN BANTUL",
          "latitude": -7.9,
          "longitude": 110.36667,
          "count": 1,
          "districts": [
            {
              "id": "3402070",
              "regency_id": "3402",
              "name": "BANTUL",
              "alt_name": "Bantul, Bantul Regency, Special Region of Yogyakarta, Indonesia",
              "latitude": -7.89146,
              "longitude": 110.33615,
              "count": 1
            },
            {
              "id": "3402100",
              "regency_id": "3402",
              "name": "DLINGO",
              "alt_name": "Dlingo, Bantul Regency, Special Region of Yogyakarta, Indonesia",
              "latitude": -7.91915,
              "longitude": 110.46106,
              "count": 1
            }
          ]
        }
      ]
    },
    "total": 4
  }
]

```


Maybe you curious where the string that matched to location name in database. To see tagged string in html, you can see via method `getHtml()`.

```java
String html = result.getHtml();
```

And then `html` variable will return value like this:

```html
Hutan Pinus Mangunan di Desa <span class="district-3402100">Dlingo</span>, Mangunan, <span class="regency-3402">Kabupaten <span class="district-3402070">Bantul</span></span>, <span class="province-34">di Yogyakarta</span> terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana.
```


### Using HMM Prediction

> What the difference between this method and UseDictionaryLookUp's method?


This method will eventually call `UseDictionaryLookUp()` method. But, before it actually called, the method will try to predict which token that predicted as **LOCATION** using Hidden Markov Model algorithm. That said, if HMM fail to detect the token as **LOCATION**, you may get blank result (only string token that have NER label as **LOCATION** will be look up). You can see the algorithm in file [NewsLocation.java](https://github.com/yusufsyaifudin/akyan/blob/master/src/main/java/akyan/nlp/news/extractor/NewsLocation.java).



> Why use this method?


You may want location be predicted using scholastic method. This can be achived using HMM which dependent from [Indonesia NER repository](https://github.com/yusufsyaifudin/indonesia-ner). You may build your own model, as mentioned in [Indonesia NER repository](https://github.com/yusufsyaifudin/indonesia-ner) as follows:

```java
Train train = new Train();
NERModel model;

try {
    model = train.doTrain(String trainingData, Boolean withPunctuation);
} catch (Exception e) {
    throw new Exception(e.getMessage());
}
```

Then you can use returned model to get prediction. But for simple example, you can use pretrained data using `ner_model_yusufs.json` to predict:

```java
Gson gson = new GsonBuilder().disableHtmlEscaping().create();
NewsLocation newsLocation = new NewsLocation(Location.TREE_LEVEL.DISTRICT);
NERModel nerModel = new NERModel();

String text = "Hutan Pinus Mangunan di Desa Dlingo, Mangunan, Kabupaten Bantul, DI Yogyakarta terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana.";

File modelfile = new File("./resources/model/ner_model_yusufs.json");
NewsLocationResult result = newsLocation.UseHmmPrediction(text, true, nerModel.loadModel(modelfile));

System.out.println(gson.toJson(result));
```

The `result` variable will return data:

```json
{
  "location": [
    {
      "province": {
        "id": "34",
        "name": "DI YOGYAKARTA",
        "alt_name": "DI YOGYAKARTA",
        "latitude": 7.7956,
        "longitude": 110.3695,
        "count": 0,
        "regencies": [
          {
            "id": "3402",
            "province_id": "34",
            "name": "KABUPATEN BANTUL",
            "alt_name": "KABUPATEN BANTUL",
            "latitude": -7.9,
            "longitude": 110.36667,
            "count": 1,
            "districts": [
              {
                "id": "3402070",
                "regency_id": "3402",
                "name": "BANTUL",
                "alt_name": "Bantul, Bantul Regency, Special Region of Yogyakarta, Indonesia",
                "latitude": -7.89146,
                "longitude": 110.33615,
                "count": 1
              },
              {
                "id": "3402100",
                "regency_id": "3402",
                "name": "DLINGO",
                "alt_name": "Dlingo, Bantul Regency, Special Region of Yogyakarta, Indonesia",
                "latitude": -7.91915,
                "longitude": 110.46106,
                "count": 1
              }
            ]
          }
        ]
      },
      "total": 3
    }
  ],
  "html": "Hutan Pinus Mangunan di Desa <span class=\"district-3402100 district-3402100\">Dlingo<\/span>, Mangunan, <span class=\"regency-3402 regency-3402\">Kabupaten <span class=\"district-3402070 district-3402070\">Bantul<\/span><\/span>, <span class=\"province-34\">DI Yogyakarta<\/span> terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana."
}
```


### API Note

Please use `disableHtmlEscaping()` in `GsonBuilder` since "characters such as <, >, =, etc. are escaped because if the JSON string evaluated by Gson is embedded in an XHTML page then we do not know what characters are actually wrapping this JSON string.", please read more in this link [http://stackoverflow.com/questions/23363843/gson-disablehtmlescaping-why-gson-html-escapes-by-default-in-the-first-place](http://stackoverflow.com/questions/23363843/gson-disablehtmlescaping-why-gson-html-escapes-by-default-in-the-first-place).