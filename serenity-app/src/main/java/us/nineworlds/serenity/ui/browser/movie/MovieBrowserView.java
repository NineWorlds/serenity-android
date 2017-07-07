package us.nineworlds.serenity.ui.browser.movie;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import us.nineworlds.serenity.core.model.CategoryInfo;


public interface MovieBrowserView extends MvpView {

    void populateCategory(List<CategoryInfo> categories, String key);
}
