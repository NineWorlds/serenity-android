package us.nineworlds.serenity.ui.browser.movie;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;


public interface MovieBrowserView extends MvpView {

    void populateCategory(List<CategoryInfo> categories, String key);

    void populateSecondaryCategory(List<SecondaryCategoryInfo> categories, String key, String category);

    void displayPosters(List<VideoContentInfo> videos);
}
