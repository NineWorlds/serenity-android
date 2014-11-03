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
	PlexappFactory factory;

	public LibraryResponseListener(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onResponse(MediaContainer mc) {
		List<MenuItem> menuItems = new MenuMediaContainer(mc, context)
		.createMenuItems();
		if (menuItems.isEmpty()) {
			return;
		}

		for (MenuItem library : menuItems) {
			if ("movie".equals(library.getType())) {
				String section = library.getSection();
				String onDeckURL = factory.getSectionsURL(section, "onDeck");
				VolleyUtils.volleyXmlGetRequest(onDeckURL,
						new MovieOnDeckResponseListener(context),
						new DefaultLoggingVolleyErrorListener());
			}

			if ("show".equals(library.getType())) {
				String section = library.getSection();
				String onDeckUrl = factory.getSectionsURL(section, "onDeck");
				VolleyUtils.volleyXmlGetRequest(onDeckUrl,
						new TVOnDeckResponseListener(context),
						new DefaultLoggingVolleyErrorListener());
			}
		}
	}
}
