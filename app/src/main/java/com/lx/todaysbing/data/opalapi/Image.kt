package com.lx.todaysbing.data.opalapi

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.regex.Pattern

data class Image(
    @field:SerializedName("category") val category: String,
    @field:SerializedName("color") val color: String,
    @field:SerializedName("country") val country: String,
    @field:SerializedName("date") val date: Int,
    @field:SerializedName("holiday") val holiday: String,
    @field:SerializedName("region") val region: String,
    @field:SerializedName("tag") val tag: String,
    @field:SerializedName("title") val title: String,

    @field:SerializedName("infoUrl") val infoUrl: String,
    @field:SerializedName("infoLink") val infoLink: String,
    @field:SerializedName("similarLink") val similarLink: String,

    @field:SerializedName("wallpaper") val wallpaper: Boolean,

    @field:SerializedName("wpFullFilename") val wpFullFilename: String?,
    @field:SerializedName("wpShortFilename") val wpShortFilename: String?,

    @field:SerializedName("text") val text: List<String>,

    @field:SerializedName("s1") val s1: String?,
    @field:SerializedName("s2") val s2: String?,
    @field:SerializedName("s3") val s3: String?,
    @field:SerializedName("s4") val s4: String?,
    @field:SerializedName("s5") val s5: String?,

    @field:SerializedName("dateString") val dateString: String
) : Serializable {

    companion object {

        @JvmStatic
        fun getS(image: Image?, s: String? = null): String? {
            image ?: return null
            val map = linkedMapOf(
                Pair("s1", image.s1), // s
                Pair("s2", image.s2), // m
                Pair("s3", image.s3), // l
                Pair("s4", image.s4), // b
                Pair("s5", image.s5), // w
            )

            return map.values.toList().subList(0, map.keys.indexOf(s) + 1).findLast {
                !it.isNullOrEmpty()
            }
        }

        @JvmStatic
        fun getUrl(s: String?): String? {
            if (!s.isNullOrEmpty()) {
                val uri = Uri.parse("https://az638417.vo.msecnd.net/")
                    .buildUpon()
                    .encodedPath(s)
                    .build()
//                println(uri.toString())
                return uri.toString()
            }
            return null
        }

        @JvmStatic
        fun splitCopyRight(copyright: String?): Array<String?> {
            val ret = arrayOfNulls<String>(2)

            if (!copyright.isNullOrEmpty()) {
//                val pattern = "(.*)\\s*\\((.+)\\)"
                val pattern = "(.*)\\s*\\(([^)]+)\\)?"
//                val pattern = "^(.*)\\s*\\((.+)\\s*\\)?\\s*$"
                val p = Pattern.compile(pattern)
                val m = p.matcher(copyright)
                if (m.find()) {
//                    println("${m.groupCount()}")
//                    println("${m.group()}")
                    ret[0] = m.group(1)
                    ret[1] = m.group(2)
                }
            }

            return ret
        }
    }
}
