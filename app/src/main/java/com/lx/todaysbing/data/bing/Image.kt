package com.lx.todaysbing.data.bing

import android.net.Uri
import android.os.Parcelable
import com.lx.todaysbing.api.BingService
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.net.URL
import java.util.regex.Pattern

/**
 * Created by liuxue on 2015/5/7.
 */
@Parcelize
data class Image(
    var startdate: String? = null,
    var fullstartdate: String? = null,
    var enddate: String? = null,
    var url: String? = null,
    var urlbase: String? = null,
    var copyright: String? = null,
    var copyrightlink: String? = null,
    var title: String? = null,
    var caption: String? = null,
    var copyrightonly: String? = null,
    var desc: String? = null,
    var date: String? = null,
    var bsTitle: String? = null,
    var quiz: String? = null,
    var wp: Boolean? = null,
    var hsh: String? = null,
    var drk: Int? = null,
    var top: Int? = null,
    var bot: Int? = null,
    var hs: List<Hs>? = null,
    var og: Og? = null,

    var msg: List<Msg>? = null,
    var pano: Pano? = null,
    var vid: Vid? = null,
) : Parcelable, Serializable {

    companion object {
        //China
        // url:http://s.cn.bing.net/az/hprichbg/rb/FudanAni_ZH-CN13023015076_1920x1080.jpg
        // urlbase:/az/hprichbg/rb/FudanAni_ZH-CN13023015076
        // Other
        // url:/az/hprichbg/rb/GoldenGateFogVideo_EN-US10020580773_1920x1080.jpg
        // urlbase:/az/hprichbg/rb/GoldenGateFogVideo_EN-US10020580773
        // China->Other
        // http://global.bing.com/az/hprichbg/rb/PalaisDuPharo_ZH-CN6551548558_1920x1080.jpg
        //[a-zA-z]+://[^\s]*_(\d+x\d+).jpg$
        //    public static String rebuildImageUrl(String url, String suggestResolutionString) {
        //
        ////        Pattern p = Pattern.compile("(\\d+x\\d+)");
        ////        Matcher m = p.matcher(url);
        ////        if (m.find()) {
        ////            return m.replaceAll(suggestResolutionString);
        ////        }
        ////        return url;
        //
        //        return "http://global.bing.com" + url + "_" + suggestResolutionString + ".jpg";
        //    }
//        fun rebuildImageUrl(image: Image?, resolution: String): String? {
//            return if (image == null) null else "https://global.bing.com" + image.urlbase + "_" + resolution + ".jpg"
//        }

        @JvmStatic
        fun getUrlbase(image: Image?, resolution: String?): String? {
            image ?: return null
            if (resolution.isNullOrEmpty()) return null
            if (image.urlbase.isNullOrEmpty()) return null

            val uri = Uri.parse(BingService.BASE_URL)
                .buildUpon()
                .encodedPath("${image.urlbase}_${resolution}.jpg")
                .build()
            return uri.toString()
        }

        @JvmStatic
        fun getUrl(image: Image?): String? {
            image ?: return null
            if (image.url.isNullOrEmpty()) return null

            val uri = Uri.parse(BingService.BASE_URL)
                .buildUpon()
                .encodedPath(image.url)
                .build()
            return uri.toString()
        }

        @JvmStatic
        fun getCopyrightlink(image: Image?): String? {
            image ?: return null
            if (image.copyrightlink.isNullOrEmpty()) return null

            return try {
                URL(image.copyrightlink).toString()
            } catch (exception: Exception) {
                val uri = Uri.parse(BingService.BASE_URL)
                    .buildUpon()
                    .encodedPath(image.copyrightlink)
                    .build()
                uri.toString()
            }
        }

        @JvmStatic
        fun splitCopyRight(copyright: String?): Array<String?> {
            val ret = arrayOfNulls<String>(2)

            if (!copyright.isNullOrEmpty()) {
                val pattern = "(.*)\\s*\\((.+)\\)"
                val p = Pattern.compile(pattern)
                val m = p.matcher(copyright)
                if (m.find()) {
                    ret[0] = m.group(1)
                    ret[1] = m.group(2)
                }
            }

            return ret
        }
    }
}

@Parcelize
data class Msg(
    var title: String? = null,
    var link: String? = null,
    var text: String? = null,
) : Parcelable, Serializable

@Parcelize
class Pano(
    var url: String? = null,
    var image: String? = null,
    var hs: Boolean? = null,
    var an: Boolean? = null,
    var cap: String? = null,
    var caplink: String? = null,
    var pos: Double? = null,
    var maxfov: Double? = null,
) : Parcelable, Serializable

@Parcelize
data class Hs(
    var desc: String? = null,
    var link: String? = null,
    var query: String? = null,
    var locx: Int? = null,
    var locy: Int? = null,
) : Parcelable, Serializable

@Parcelize
data class Og(
    val img: String,
    val title: String,
    val desc: String,
    val hash: String,
) : Parcelable, Serializable

@Parcelize
data class Vid(
    var sources: List<Array<String>>? = null,
    var loop: Boolean? = null,
    var image: String? = null,
    var caption: String? = null,
    var captionlink: String? = null,
    var dark: String? = null,
) : Parcelable, Serializable