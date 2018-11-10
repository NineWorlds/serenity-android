package us.nineworlds.plex.rest.services;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import us.nineworlds.plex.rest.model.impl.MediaContainer;

public interface PlexMediaContainerService {

    @GET("/")
    Call<MediaContainer> retrieveRoot();

    @GET("library")
    Call<MediaContainer> retrieveLibrary();

    @GET("library/sections")
    Call<MediaContainer> retrieveSections();

    @GET("library/sections/{key}")
    Call<MediaContainer> retrieveSections(@Path("key") String key);

    @GET("library/sections/{key}/{category}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path(value = "category", encoded = true) String category);

    @GET("library/sections/{key}/{category}/{secondaryCategory}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path(value = "category", encoded = true)  String category,
                                          @Path(value = "secondaryCategory", encoded = true) String secondaryCategory);

    @GET("{urlPath}")
    Call<MediaContainer> retrieveItemByUrlPath(@Path(value = "urlPath", encoded = true) String key);


    @GET("search?type=1")
    Call<MediaContainer> movieSearch(@Query("query") String query);

    @GET("search?type=2")
    Call<MediaContainer> tvShowsSearch(@Query("query") String query);

    @GET("search?type=4")
    Call<MediaContainer> episodeSearch(@Query("query") String query);
}
