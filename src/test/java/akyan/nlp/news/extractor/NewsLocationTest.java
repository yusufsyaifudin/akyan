package akyan.nlp.news.extractor;

import akyan.nlp.news.helpers.LocationModel;
import akyan.nlp.news.helpers.NewsLocationResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;
import wilayah.indonesia.Location;
import yusufs.nlp.nerid.NERModel;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by yusuf on 08/11/16.
 */
public class NewsLocationTest {
    String text1 = "Hutan Pinus Mangunan di Desa Dlingo, Mangunan, Kabupaten Bantul, DI Yogyakarta terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana.";
    String text2 = "Arus lalu lintas di jalur alternatif via Kecamatan Bulu, Kabupaten Sukoharjo, ditutup mulai Sabtu (15/10/2016). Penutupan ruas Jl. Yos Sudarso, Bulu, itu akibat tanah di sayap jembatan di sebelah barat Kantor Kecamatan Bulu longsor. Akibatnya, sebagian konstruksi jembatan retak-retak, terutama tiang penyangga jembatan di sisi timur dan tengah. Informasi yang dihimpun Solopos.com, longsor terjadi pada Jumat (14/10/2016) malam sekitar pukul 22.15 WIB. Sebelum longsor, jembatan itu memang sedang dalam proses perbaikan yang dimulai beberapa hari lalu. Pekerja sudah mulai membuat fondasi jembatan. Seorang pekerja rehab jembatan, Hendrik, bercerita tembok lama jembatan ambrol diduga akibat tak kuat menahan getaran kendaraan yang melintas. Menurut dia, peristiwa ambrolnya dinding bangunan jembatan terjadi saat hujan. “Hujan tidak terlalu deras tetapi mengakibatkan dinding jembatan ambrol,” kata dia. Dia mengatakan saat kejadian ia belum tertidur dan mendengar suara seperti benda ambruk dari arah jembatan. Setelah itu, pemuda, warga, dan pekerja jembatan yang tidur di sekitar lokasi bergegas mendatangi arah suara. Beberapa pemuda berinisiatif menutup jalan utama agar tidak menimbulkan korban. Hendrik menyebutkan tak ada korban jiwa dalam peristiwa tersebut. Perwakilan dari CV Sugeng Mulya selaku rekanan rehab Jembatan Bulu, Sugeng, yang ditemui Solopos.com di lokasi jembatan mengaku mendapatkan kabar ambrolnya jembatan dari Kapolsek Bulu, AKP Kamiran. “Saya dikabari Pak Kapolsek, Jumat malam sekitar pukul 23.00 WIB, bahwa jembatan ambrol. Tadi [Sabtu] saya sudah koordinasi dan segera membuat rambu-rambu petunjuk arah untuk pengalihan arus lalu lintas. Yang jelas sejak longsor jalan utama penghubung lintas provinsi melalui Bulu tidak bisa dilewati kendaraan roda empat,” kata dia. Sugeng mengatakan rambu-rambu akan dipasang di beberapa lokasi untuk memudahkan pengguna jalan agar tidak tersesat. “Pengalihan arus lalu lintas dilakukan sejak dari Pasar Tawangsari dan perbatasan Bulu, Sukoharjo. Kami tidak berani mengalihkan arus lalu lintas melalui jalan kampung karena takut jalan kampung rusak.” Arus lalu lintas dari arah barat atau Tawangsari hendak ke Wonogiri dialihkan melalui Sukoharjo kota, begitu juga dari arah selatan atau Wonogiri. “Untuk sepeda motor masih bisa melintas di jembatan tetapi harus bersabar karena hanya dibuka satu lajur. Kami [CV Sugeng Mulya] mendapatkan pekerjaan rehab jembatan. Pekerjaan dimulai sejak 26 September lalu selama 80 hari,” jelas dia. Sugeng menduga sebagian dinding tiang jembatan dan longsor tanah akibat getaran kendaraan yang melintas di jembatan. Pekerjaan galian fondasi di bawah jembatan dimulai beberapa hari lalu. Kemungkinan perbedaan ketinggian antara lubang fondasi baru dengan tiang lama mengakibatkan tanah ambrol. “Kedalaman fondasi baru 2,5 meter dibanding posisi tiang lama sehingga posisi tiang lama menggantung. Apalagi hujan turun sejak Jumat siang hingga malam,” tandas dia.";
    String text3 = "Gempa bumi dengan kekuatan 4,1 magnitudo mengguncang Kabupaten Sikka, Nusa Tenggara Timur (NTT), Jumat (11/11/2016). Kepala Badan Meteorologi, Klimatologi, dan Geofisika (BMKG) Kelas I Kupang Hasanudin kepada Kompas.com mengatakan, gempa bumi itu terjadi sekitar pukul 17.57 Wita. Episenter gempa terletak pada koordinat 8,05 derajat Lintang Selatan dan 122,04 derajat Bujur Timur, pada kedalaman 208 kilometer. Pusat gempa bumi 70 kilometer barat laut Kabupaten Sikka. \"Terkait dengan peristiwa gempa bumi di Barat Laut Kabupaten Sikka yang baru saja terjadi, hingga laporan ini disusun pada pukul 18.30 Wita, belum ada laporan dari warga,\"kata Hasanudin. Ia pun mengimbau kepada masyarakat, agar tetap tenang, dan terus mengikuti arahan BPBD dan BMKG. Khusus masyarakat di daerah pesisir Utara Kabupaten Sikka diimbau agar tidak terpancing isu karena gempa bumi yang terjadi tidak berpotensi tsunami. Jika ditinjau dari kedalaman hiposenternya, gempa bumi yang terjadi merupakan jenis gempa bumi menengah akibat aktivitas sesar lokal. \"Berdasarkan peta tatanan tektonik daerah NTT tampak bahwa di lokasi episenter gempa bumi memang terdapat struktur sesar lokal. Diperkirakan struktur sesar ini, yang mengalami reaktivasi hingga memicu gempa menengah,\" sebutnya.";

    final NewsLocation newsLocation = new NewsLocation(Location.TREE_LEVEL.DISTRICT);

    NERModel nerModel = new NERModel();
    File modelFile = new File("./resources/model/ner_model_yusufs.json");

    Logger logger = Logger.getLogger(NewsLocationTest.class);
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public NewsLocationTest() {

    }

    @Test
    public void UseDictionaryLookUp1() {
        NewsLocationResult result = newsLocation.UseDictionaryLookUp(text1);
        ArrayList<LocationModel> treeLocation1 = result.getLocation();
        assertTrue(treeLocation1.get(0).getProvince().getName().equals("DI YOGYAKARTA"));
//        logger.info(gson.toJson(result));
    }

    @Test
    public void UseDictionaryLookup2() {
        NewsLocationResult result = newsLocation.UseDictionaryLookUp(text2);
        ArrayList<LocationModel> treeLocation2 = result.getLocation();
        assertTrue(treeLocation2.get(0).getProvince().getName().equals("JAWA TENGAH"));
        logger.info(gson.toJson(result));
    }

    @Test
    public void UseDictionaryLookup3() {
        NewsLocationResult result = newsLocation.UseDictionaryLookUp(text3);
        ArrayList<LocationModel> treeLocation2 = result.getLocation();
        assertTrue(treeLocation2.get(0).getProvince().getName().equals("NUSA TENGGARA TIMUR"));
        logger.info(gson.toJson(result));
    }

    @Test
    public void UseHmmPrediction1() {
        NewsLocationResult result = newsLocation.UseHmmPrediction(text1, true, nerModel.loadModel(modelFile));

        ArrayList<LocationModel> treeLocation = result.getLocation();
        assertTrue(treeLocation.get(0).getProvince().getName().equals("DI YOGYAKARTA"));
        logger.info(gson.toJson(result));
    }

    @Test
    public void UseHmmPrediction2() {
        NewsLocationResult result = newsLocation.UseHmmPrediction(text2, true, nerModel.loadModel(modelFile));

        ArrayList<LocationModel> treeLocation = result.getLocation();
        assertTrue(treeLocation.get(0).getProvince().getName().equals("JAWA TENGAH"));
        logger.info(gson.toJson(result));
    }

    @Test
    public void UseHmmPrediction3() {
        NewsLocationResult result = newsLocation.UseHmmPrediction(text3, true, nerModel.loadModel(modelFile));

        ArrayList<LocationModel> treeLocation = result.getLocation();
        assertTrue(treeLocation.get(0).getProvince().getName().equals("JAWA TIMUR"));
        logger.info(gson.toJson(result));
    }
}
