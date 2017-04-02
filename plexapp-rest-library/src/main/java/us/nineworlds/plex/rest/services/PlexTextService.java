package us.nineworlds.plex.rest.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dcarver on 4/1/17.
 */

public interface PlexTextService {

    @GET(":/scrobble?key={key}&identifier=com.plexapp.plugins.library")
    Call<String> watched(@Path("key") String key);

    @GET(":/unscrobble?key={key}&identifier=com.plexapp.plugins.library")
    Call<String> unwatched(@Path("key") String key);

    @GET(":/progress?key={key}&identifier=com.plexapp.plugins.library&time={offset}")
    Call<String> progress(@Path("key") String key,
                          @Path("offset") String offset);
}
