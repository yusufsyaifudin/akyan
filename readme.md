# Akyan

[![Build Status](https://travis-ci.org/yusufsyaifudin/akyan.svg?branch=master)](https://travis-ci.org/yusufsyaifudin/akyan)

> Etymology: `Akyan` is from Sanskrit language which mean "eyes" or "vision"

Akyan is libary for extracting some information from news in Bahasa Indonesia. This is early stage of Akyan development, and currently Akyan have one feature:

* Extract location mentioned in news.


## Installation

You can use jitpack and install dependency from active branch `master`.

```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
<dependency>
  <groupId>com.github.yusufsyaifudin</groupId>
  <artifactId>akyan</artifactId>
  <version>-SNAPSHOT</version>
</dependency>
```

After installation please extract this zip file into your `resources` directory in your root project.

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


