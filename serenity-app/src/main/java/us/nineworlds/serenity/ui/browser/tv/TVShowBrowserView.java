package us.nineworlds.serenity.ui.browser.tv;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.CategoryInfo;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;

public interface TVShowBrowserView extends MvpView {

    void updateCategories(List<CategoryInfo> categories);

    void displayShows(List<SeriesContentInfo> series, String category);

    void populateSecondaryCategories(List<SecondaryCategoryInfo> catagories, String category);
}
