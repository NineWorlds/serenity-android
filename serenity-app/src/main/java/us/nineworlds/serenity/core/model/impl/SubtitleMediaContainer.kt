package us.nineworlds.serenity.core.model.impl

import us.nineworlds.serenity.common.media.model.IMediaContainer

class SubtitleMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {

    fun createSubtitle(): List<Subtitle> {
        val streams = mc.videos?.firstOrNull()?.medias?.firstOrNull()?.videoPart?.firstOrNull()?.streams ?: return emptyList()

        val subtitles = mutableListOf<Subtitle>()
        for (stream in streams) {
            if (stream.format == "srt" || stream.format == "ass") {
                val subtitle = Subtitle().apply {
                    format = stream.format
                    languageCode = stream.languageCode
                    key = stream.key?.let { "${factory.baseURL()}${it.replaceFirst("/", "")}" }
                    description = stream.language?.let { "$it (${stream.format})" } ?: "Unknown (${stream.format})"
                }
                if (subtitle.key != null) {
                    subtitles.add(subtitle)
                }
            }
        }
        return subtitles
    }
}
