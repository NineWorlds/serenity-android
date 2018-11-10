package us.nineworlds.plex.rest.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlexTextService {

    @GET(":/scrobble?identifier=com.plexapp.plugins.library")
    Call<String> watched(@Query("key") String key);

    @GET(":/unscrobble?identifier=com.plexapp.plugins.library")
    Call<String> unwatched(@Query("key") String key);

    @GET(":/progress?identifier=com.plexapp.plugins.library")
    Call<String> progress(@Query("key") String key,
                          @Query("time") String offset);
}
