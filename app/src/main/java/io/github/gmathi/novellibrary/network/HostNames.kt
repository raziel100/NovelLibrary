package io.github.gmathi.novellibrary.network


import io.github.gmathi.novellibrary.model.source.online.HttpSource
import java.util.*

object HostNames {

    const val NOVEL_UPDATES = "novelupdates.com"
    const val ROYAL_ROAD_OLD = "royalroadl.com"
    const val ROYAL_ROAD = "royalroad.com"
    const val KOBATOCHAN = "kobatochan.com"
    const val GRAVITY_TALES = "gravitytales.com"
    const val WUXIA_WORLD = "wuxiaworld.com"
    const val WORD_PRESS = "wordpress.com"
    const val WLN_UPDATES = "wlnupdates.com"
    const val QIDIAN = "webnovel.com"
    const val MOON_BUNNY_CAFE = "moonbunnycafe.com"
    const val BLUE_SILVER_TRANSLATIONS = "bluesilvertranslations.wordpress.com"
    const val GOOGLE_DOCS = "docs.google.com"
    const val BAKA_TSUKI = "baka-tsuki.org"
    const val NOVEL_FULL = "novelfull.com"
    const val SCRIBBLE_HUB = "scribblehub.com"
    const val LNMTL = "lnmtl.com"
    const val WATTPAD = "wattpad.com"
    const val FOXTELLER = "foxteller.com"
    const val BABEL_NOVEL = "babelnovel.com"
    const val NEOVEL = "neoread.neovel.io"
    const val CHRYSANTHEMUMGARDEN = "chrysanthemumgarden.com"
    const val VOLARE_NOVELS = "volarenovels.com"

    const val USER_AGENT = HttpSource.DEFAULT_USER_AGENT//"Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36 EdgA/129.0.0.0"

    private val DEFAULT_ALLOWED_HOST_NAMES_ARRAY = arrayOf(
        NOVEL_UPDATES,
        ROYAL_ROAD,
        KOBATOCHAN,
        GRAVITY_TALES,
        WUXIA_WORLD,
        WORD_PRESS,
        WLN_UPDATES,
        QIDIAN,
        LNMTL,
        WATTPAD,
        FOXTELLER,
        "patreon.com",
        "royalroadlupload.blob.core.windows.net",
        "postimg.org",
        "lightnovelbastion.com",
        "fonts.googleapis.com",
        "ggpht.com",
        "gravatar.com",
        "imgur.com",
        "isohungrytls.com",
        "bootstrapcdn.com",
        "CloudFlare.com",
        "wp.com",
        "scatterdrift.com",
        "discordapp.com",
        "chubbycheeksthoughts.com",
        "omatranslations.com",
        "www.googleapis.com",
        "*.googleusercontent.com",
        "cdn.novelupdates.com",
        "*.novelupdates.com",
        "www.novelupdates.com",
        "www.wuxiaworld.com",
        "reports.crashlytics.com",
        "api.crashlytics.com"
    )

    val defaultHostNamesList: ArrayList<String>
        get() = ArrayList(listOf(*DEFAULT_ALLOWED_HOST_NAMES_ARRAY))

    var hostNamesList = ArrayList<String>()
        set(hostNames) {
            hostNamesList.addAll(hostNames)
        }

    fun isVerifiedHost(hostName: String): Boolean {
        return try {
            hostNamesList.isNotEmpty() && hostNamesList.any { it.contains(hostName) }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun addHost(hostName: String): Boolean = hostNamesList.add(hostName)
}
