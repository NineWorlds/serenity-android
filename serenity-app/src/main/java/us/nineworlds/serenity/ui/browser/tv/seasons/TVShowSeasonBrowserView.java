package us.nineworlds.serenity.ui.browser.tv.seasons;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;

public interface TVShowSeasonBrowserView extends MvpView {

    void updateEpisodes(List<VideoContentInfo> episodes);

    void populateSeasons(List<SeriesContentInfo> seasons);
}
