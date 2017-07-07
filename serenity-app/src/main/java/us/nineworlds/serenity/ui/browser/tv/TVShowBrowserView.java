package us.nineworlds.serenity.ui.browser.tv;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.events.TVCategoryEvent;
import us.nineworlds.serenity.events.TVCategorySecondaryEvent;

public interface TVShowBrowserView extends MvpView {

    void updateCategories(TVCategoryEvent tvCategoryEvent);

    void displayShows(List<SeriesContentInfo> series, String category);

    void populateSecondaryCategories(List<SecondaryCategoryInfo> catagories, String category);
}
