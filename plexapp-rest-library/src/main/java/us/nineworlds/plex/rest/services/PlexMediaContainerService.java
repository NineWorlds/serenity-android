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
    Call<MediaContainer> retireveLibrary();

    @GET("library/sections")
    Call<MediaContainer> retrieveSections();

    @GET("library/sections/{key}")
    Call<MediaContainer> retrieveSections(@Path("key") String key);

    @GET("library/sections/{key}/{category}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path("category") String category);

    @GET("library/sections/{key}/{category}/{secondaryCategory}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path("category")  String category,
                                          @Path("secondaryCategory") String secondaryCategory);

    @GET("{urlPath}")
    Call<MediaContainer> retrieveItemByUrlPath(@Path("urlPath") String key);


    @GET("library/sections/{key}/search?type=1")
    Call<MediaContainer> movieSearch(@Path("key") String key,
                                    @Query("query") String query);

    @GET("library/sections/{key}/search?type=2")
    Call<MediaContainer> tvShowsSearch(@Path("key") String key,
                              @Query("query") String query);

    @GET("library/sections/{key}/search?type=4")
    Call<MediaContainer> episodeSearch(@Path("key") String key,
                                       @Query("query") String query);
}
