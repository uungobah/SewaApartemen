package com.apartment.swamedia.sewaapartemenbandung.util;

public interface ConstantUtil {

    interface APP {
        String VERSION = "1.6";
        String VERSIONAPP = "V2";
    }

    interface WEB_SERVICE {
        String URL = "http://172.17.2.94:7777/realcon/index.php/en/rest";
        String URL2 = "http://172.17.2.127/realcon/index.php/en/rest";
        String URL3 = "http://jagosewa.com";


        String URL_LOGIN_PROD = URL3
                + "/api_auth/login";

        String URL_REGISTER = URL3
                + "/daftar/user";

        String URL_LOCATION = URL3
                + "/api_view/autoComplete";

        String URL_RENT_TYPE = URL3
                + "/api_view/rentType";

        String URL_PROPERTY = URL3
                + "/api_post_property/list_tipe_property";

        String URL_MANAGE_PROPERTY = URL3
                + "/api_post_property/list_property";

        String URL_PERMINTAAN_SEWA = URL3
                + "/api_transaction/transaksi_list_pemilik";
        String URL_PENGAJUAN_SEWA = URL3
                + "/api_transaction/transaksi_list_penyewa";

        String URL_LIST_BANK = URL3 + "/api_profileuser/list_bank";

        String URL_PROFIL_REKENING = URL3 + "/api_profileuser/profile_rekening";

        String URL_DELETE_REKENING = URL3 + "/api_profileuser/delete_rekening";

        String URL_UPDATE_REKENING = URL3 + "/api_profileuser/edit_rekening";

        String URL_PROFIL = URL3 + "/api_profileuser/profile";

        String URL_UPDATE_PASSWORD = URL3 + "/api_profileuser/update_password";

        String URL_UPDATE_PROFIL = URL3 + "/api_profileuser/edit";

        String URL_KIRIM_KODE = URL3 + "/api_profileuser/kirim_kode";

        String URL_CEK_KODE = URL3 + "/api_profileuser/cek_hp";

        String URL_STATUS_VERIFIKASI = URL3 + "/api_profileuser/status_verification";

        String URL_SEARCH_STANDAR = URL3 + "/front_api_home/property_search_post";

        String URL_SEARCH_BY_MAP = URL3 + "/api_view/searchMaps";

        String URL_GET_DETAIL = URL3 + "/front_api_property/detailproperty/";

        String URL_GET_FASILITAS_DALAM = URL3
                + "/api_apartment/list_data_fasilitas_1/";

        String URL_GET_FASILITAS_DALAMCEKLIS = URL3
                + "/api_apartment/list_data_fasilitas_2/";

        String URL_GET_FASILITAS_LUAR = URL3
                + "/api_apartment/list_data_fasilitas_3/";
        String URL_GET_FASILITAS_JARAK = URL3
                + "/api_apartment/list_data_jarak_strategis/";

        String URL_COMPARE = URL3
                + "/api_view/compare";

        String URL_UPLOAD_GALERY = URL3
                + "/api_post_property/do_upload";
        String URL_LIST_GALERY = URL3
                + "/api_post_property/gallery/";

        String URL_DELETE_UPLOAD_GALERY = URL3
                + "/api_post_property/do_delete";

        String URL_GET_GEDUNG = URL3 + "/api_apartment/list_gedung_apartemen/";

        String URL_DETAIL_GEDUNG = URL3 + "/api_apartment/detail_gedung_apartemen/";

        String URL_KOTA = URL3 + "/api_apartment/list_kabkota";
        String URL_KEC = URL3 + "/api_apartment/list_kecamatan/";
        String URL_KEL = URL3 + "/api_apartment/list_kelurahan/";

        String URL_NON_AKTIF_APARTEMEN = URL3 + "/api_post_property/action_property";


        String URL_GET_FASILITAS_DALAM_CREATE = URL3
                + "/api_post_property/list_fasilitas/1";

        String URL_GET_FASILITAS_DALAMCEKLIS_CREATE = URL3
                + "/api_post_property/list_fasilitas/2";

        String URL_GET_FASILITAS_LAINNYA_CREATE = URL3
                + "/api_post_property/list_fasilitas/3";

        String URL_CREATE_PROPERTY = URL3 + "/api_post_property/create";

        String URL_EDIT_PROPERTY = URL3 + "/api_post_property/edit";

        String URL_DETAIL_PROPERTY = URL3 + "/api_post_property/detail";

        String URL_GET_ID_NEW_PROPERTY = URL3 + "/api_post_property/id_post_property";

        String URL_GET_DETAIL_FASILITAS = URL3 + "/api_post_property/list_detail_fasilitas/";

        String URL_POST_DETAIL_REK = URL3 + "/api_profileuser/profile_detail_rekening";

        String URL_POST_EDIT_REK = URL3 + "/api_profileuser/edit_rekening";

        String URL_POST_CREATE_REK = URL3 + "/api_profileuser/create_rekening";

        String URL_POST_CEK_VERIFIKASI = URL3 + "/api_profileuser/status_verifikasi";

        String URL_POST_CEK_KTP = URL3 + "/api_profileuser/cek_ktp";

        String URL_POST_BOOKING = URL3 + "/front_api_property/booking_rent";

        String URL_FRONT_IMAGE_SLIDER = URL3 + "/api_view/front_img_slider";

        String URL_LIST_NOTIF_PEMILIK = URL3 + "/api_transaction/transaksi_list_pemilik";

        String URL_LIST_NOTIF_PENYEWA = URL3 + "/api_transaction/transaksi_list_penyewa";


        String URL_ACTION_SEWA = URL3 + "/api_transaction/action_sewa";

        String URL_DETAIL_PEMBAYARAN = URL3 + "/api_transaction/transaksi_detail_pembayaran";

        String URL_LIST_BANK_SWA = URL3 + "/api_transaction/list_bank";

        String URL_KONFIRMASI_PEMBAYARAN = URL3 + "/api_transaction/transaksi_konfirmasi_pembayaran";
        String URL_DETAIL_USER = URL3 + "/front_api_home/detailuser";
        String URL_SEND_MESSAGE = URL3 + "/api_message/message_create";


        //        String URL_LOGIN_PROD = URL2
//                + "/login/auth";
        String URL_SEARCH_BIASA = URL2
                + "/search/searchApartemen";
        String URL_DETAIL_APARTMENT = URL2
                + "/view/singleApartment";

        String URL_GET_SEARCH_MAP ="http://maps.googleapis.com/maps/api/geocode/json?sensor=true&address=";
        String URL_GET_AUTO_COMPLETE ="https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&key=AIzaSyC4vnTHkhzFB9v8nr8LTkdRmG51S8XpbGI&language=id&input=";

        String URL_KOTAK_MASUK = URL3
                + "/api_message/message_list_inbox";

        String URL_NOTIF_PESAN = URL3
                + "/api_message/message_notification";

        String URL_UPDATE_NOTIF = URL3
                + "/api_message/message_open_notification";

        String URL_KOTAK_KELUAR = URL3
                + "/api_message/message_list_outbox";

        String URL_DETAIL_MESSAGE = URL3
                + "/api_message/message_detail";

        String URL_REPLY_MESSAGE = URL3
                + "/api_message/message_reply";
        String URL_DELETE_MESSAGE = URL3
                + "/api_message/message_delete";

        String URL_LIST_COMMENT = URL3
                + "/api_comment/list_comment";

        String URL_SEND_COMMENT = URL3
                + "/api_comment/create_comment";

        String URL_SET_REK_UTAMA = URL3 + "/api_profileuser/selectedprimarybank";
        String URL_SAVE_REK_UTAMA = URL3 + "/api_profileuser/primarybank";

        String URL_DISABLED_CALENDER = URL3
                + "/api_calendar/list_disabled_date_android/";

        String URL_DISABLED_EDIT_CALENDER = URL3
                + "/api_calendar/addEvent";
        String URL_ENABLED_EDIT_CALENDER = URL3
                + "/api_calendar/deleteEvent";

        String URL_CHECK_TANGGAL = URL3
                + "/front_api_property/check_availability_property";



}

    interface SHAREDPREFERENCE {
        String LOGIN = "LOGIN";
        String USER = "USER";
        String SEARCH = "SEARCH";

    }

    interface PDF {
        String PDF_DIRECTORY_PROD = "paperless";

    }

    interface PICTURE {
        // Dev
        // public String path =
        // "/ajax_ifesdev/ccfUpload/file/paperless/file_upload/";
        // Prod
        String path_prod = "/ccfUpload/file/paperless/file_upload/";
        int LOW = 50;
        int MED = 80;
        int HIGH = 100;

    }
}
