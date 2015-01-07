package us.nineworlds.serenity.volley;

import java.util.List;

import javax.inject.Inject;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.injection.BaseInjector;
import android.content.Context;

import com.android.volley.Response;

public class LibraryResponseListener extends BaseInjector implements
		Response.Listener<MediaContainer> {

	Context context;

	@Inject
	protected PlexappFactory factory;

	@Inject
	protected VolleyUtils volley;

	public LibraryResponseListener(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onResponse(MediaContainer mc) {
		List<MenuItem> menuItems = new MenuMediaContainer(mc).createMenuItems();
		if (menuItems.isEmpty()) {
			return;
		}

		for (MenuItem library : menuItems) {
			if ("movie".equals(library.getType())) {
				String section = library.getSection();
				String onDeckURL = factory.getSectionsURL(section, "onDeck");
				volley.volleyXmlGetRequest(onDeckURL,
						new MovieOnDeckRecommendationResponseListener(context),
						new DefaultLoggingVolleyErrorListener());
			}

			if ("show".equals(library.getType())) {
				String section = library.getSection();
				String onDeckUrl = factory.getSectionsURL(section, "onDeck");
				volley.volleyXmlGetRequest(onDeckUrl,
						new TVOnDeckRecommendationResponseListener(context),
						new DefaultLoggingVolleyErrorListener());
			}
		}
	}
}
