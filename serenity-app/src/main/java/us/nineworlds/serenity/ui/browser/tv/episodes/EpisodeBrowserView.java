package us.nineworlds.serenity.ui.browser.tv.episodes;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.VideoContentInfo;


public interface EpisodeBrowserView extends MvpView {

    void fetchEpisodes(String key);

    void updateGallery(List<VideoContentInfo> episodes);
}
