package akyan.nlp.news.extractor;

import akyan.nlp.news.helpers.LocationModel;
import org.junit.Test;
import wilayah.indonesia.Location;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by yusuf on 08/11/16.
 */
public class NewsLocationTest {
    NewsLocation newsLocation = new NewsLocation(Location.TREE_LEVEL.DISTRICT);

    public NewsLocationTest() {
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Test
    public void UseDictionaryLookUp() {
        String text1 = "Hutan Pinus Mangunan di Desa Dlingo, Mangunan, Kabupaten Bantul, DI Yogyakarta terkenal sebagai tempat selfie dan berfoto ria. Deretan pohon pinus tampak seperti lokasi syuting film di utara Eropa sana.   Misterius, begitu kesan Hutan Pinus Mangunan jika didatangi pada pagi hari. Kabut tipis menyelimuti pandangan. Semburat cahaya matahari menelusup dari balik batang-batang pinus, menghasilkan lanskap fotogenik yang jadi incaran pengunjung.  Meski KompasTravel tidak datang ke Hutan Pinus Mangunan pada pagi buta, antusiasme pengunjung rupanya tak menipis. Matahari sedang di atas kepala saat rombongan \"Take Me Anywhere 2\" tiba di pintu gerbang Hutan Pinus Mangunan. Meski bukan hari libur, kawasan wisata tersebut ternyata cukup ramai.  Masuk ke dalam area hutan pinus, pandangan sudah tertuju pada hammock warna-warni yang digantung di antara batang pinus. Tak hanya satu, ada beberapa hammock yang disusun bertingkat. Wisatawan bisa menyewanya untuk berfoto.  Hutan Pinus Mangunan masuk dalam kawasan Resort Pengelolaan Hutan (RPH) Mangunan. Lokasinya yang searah dengan Makam Raja-raja Imogiri seringkali membuat wisatawan terkecoh dengan menyebutnya Hutan Pinus Imogiri. Selain pinus, hutan ini juga ditumbuhi beberapa jenis tanaman lainnya seperti akasia, kemiri, dan kayu putih. Sepuluh peserta \"Take Me Anywhere 2\" mulai berpencar, mengambil selfie dengan latar hutan pinus. Banyak wisatawan tumplek blek di daerah sekitar gerbang. Namun semakin berjalan ke bagian dalam, wisatawan semakin jarang. Sunyi pun perlahan menyergap.  Sedikit letih karena trek menanjak, saya beristirahat di bangku kayu. Tak ada seorang pun yang terlihat di tempat itu. Semilir angin terasa sejuk. Meski matahari sedang terik-teriknya, dedaunan pinus senantiasa menjadi peneduh.  Tak berapa lama, angan saya mulai melanglangbuana. Angin sepoi-sepoi yang menyapu kulit terasa sangat nyaman. Saya seperti mendapatkan spot pribadi, padahal tidak sampai lima menit jalan kaki, keramaian sudah menghadang di dekat pintu gerbang.  Banyak orang yang bilang, Hutan Pinus Mangunan kini menjadi tempat wisata mainstream di DI Yogyakarta. Namun selalu ada tempat untuk menyepi di hutan ini. Selalu ada spot kosong untuk berfoto ciamik untuk diunggah di jejaring sosial.  Untuk masuk ke sini, wisatawan hanya perlu merogoh kocek Rp 3.000 per orang dan Rp 10.000 untuk parkir mobil. Dari sini, Anda juga bisa bertolak ke beberapa tempat seperti Makam Raja-raja Imogiri , Kebun Buah Mangunan, juga Gunung Api Purba Nglanggeran.  Setelah kesuksesan \"Take Me Anywhere\" pertama yaitu edisi Bali, Kompas.com kali ini bersama OPPO menggelar \"Take Me Anywhere 2\". Sebanyak 10 orang pemenang kompetisi \"Take Me Anywhere 2\" berlibur ke Yogyakarta bersama KompasTravel selama tiga hari mulai Jumat (14/10/2016) hingga Minggu (16/10/2016).  Reservasi tiket penerbangan dilakukan melalui situs perjalanan Tiket.com.  Para pemenang menjalani beragam aktivitas wisata penuh petualangan sampai mencicipi kuliner khas Yogyakarta. Semuanya dipandu oleh biro perjalanan Jogja Geowisata.  Selain itu, para pemenang juga merasakan kenyamanan menginap di Hotel Santika Premiere Jogja, salah satu properti dari jaringan hotel Santika Indonesia Hotels & Resorts.  Ikuti petualangan seru para pemenang di Yogyakarta. Kisah mereka tayang dalam liputan khusus: \"Liburan Seru ala 'Take Me Anywhere 2'\".";
        ArrayList<LocationModel> treeLocation1 = newsLocation.UseDictionaryLookUp(text1);
        assertTrue(treeLocation1.get(0).getProvince().getName().equals("DI YOGYAKARTA"));

        String text2 = "Arus lalu lintas di jalur alternatif via Kecamatan Bulu, Kabupaten Sukoharjo, ditutup mulai Sabtu (15/10/2016). Penutupan ruas Jl. Yos Sudarso, Bulu, itu akibat tanah di sayap jembatan di sebelah barat Kantor Kecamatan Bulu longsor. Akibatnya, sebagian konstruksi jembatan retak-retak, terutama tiang penyangga jembatan di sisi timur dan tengah. Informasi yang dihimpun Solopos.com, longsor terjadi pada Jumat (14/10/2016) malam sekitar pukul 22.15 WIB. Sebelum longsor, jembatan itu memang sedang dalam proses perbaikan yang dimulai beberapa hari lalu. Pekerja sudah mulai membuat fondasi jembatan. Seorang pekerja rehab jembatan, Hendrik, bercerita tembok lama jembatan ambrol diduga akibat tak kuat menahan getaran kendaraan yang melintas. Menurut dia, peristiwa ambrolnya dinding bangunan jembatan terjadi saat hujan. “Hujan tidak terlalu deras tetapi mengakibatkan dinding jembatan ambrol,” kata dia. Dia mengatakan saat kejadian ia belum tertidur dan mendengar suara seperti benda ambruk dari arah jembatan. Setelah itu, pemuda, warga, dan pekerja jembatan yang tidur di sekitar lokasi bergegas mendatangi arah suara. Beberapa pemuda berinisiatif menutup jalan utama agar tidak menimbulkan korban. Hendrik menyebutkan tak ada korban jiwa dalam peristiwa tersebut. Perwakilan dari CV Sugeng Mulya selaku rekanan rehab Jembatan Bulu, Sugeng, yang ditemui Solopos.com di lokasi jembatan mengaku mendapatkan kabar ambrolnya jembatan dari Kapolsek Bulu, AKP Kamiran. “Saya dikabari Pak Kapolsek, Jumat malam sekitar pukul 23.00 WIB, bahwa jembatan ambrol. Tadi [Sabtu] saya sudah koordinasi dan segera membuat rambu-rambu petunjuk arah untuk pengalihan arus lalu lintas. Yang jelas sejak longsor jalan utama penghubung lintas provinsi melalui Bulu tidak bisa dilewati kendaraan roda empat,” kata dia. Sugeng mengatakan rambu-rambu akan dipasang di beberapa lokasi untuk memudahkan pengguna jalan agar tidak tersesat. “Pengalihan arus lalu lintas dilakukan sejak dari Pasar Tawangsari dan perbatasan Bulu, Sukoharjo. Kami tidak berani mengalihkan arus lalu lintas melalui jalan kampung karena takut jalan kampung rusak.” Arus lalu lintas dari arah barat atau Tawangsari hendak ke Wonogiri dialihkan melalui Sukoharjo kota, begitu juga dari arah selatan atau Wonogiri. “Untuk sepeda motor masih bisa melintas di jembatan tetapi harus bersabar karena hanya dibuka satu lajur. Kami [CV Sugeng Mulya] mendapatkan pekerjaan rehab jembatan. Pekerjaan dimulai sejak 26 September lalu selama 80 hari,” jelas dia. Sugeng menduga sebagian dinding tiang jembatan dan longsor tanah akibat getaran kendaraan yang melintas di jembatan. Pekerjaan galian fondasi di bawah jembatan dimulai beberapa hari lalu. Kemungkinan perbedaan ketinggian antara lubang fondasi baru dengan tiang lama mengakibatkan tanah ambrol. “Kedalaman fondasi baru 2,5 meter dibanding posisi tiang lama sehingga posisi tiang lama menggantung. Apalagi hujan turun sejak Jumat siang hingga malam,” tandas dia.";
        ArrayList<LocationModel> treeLocation2 = newsLocation.UseDictionaryLookUp(text2);
        assertTrue(treeLocation2.get(0).getProvince().getName().equals("JAWA TENGAH"));
    }
}
