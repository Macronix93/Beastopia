package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface GroupApiService {
    @POST("groups")
    Observable<Group> createGroup(@Body CreateGroupDto groupDto);

    @GET("groups")
    Observable<List<Group>> getGroups(@Query("members") List<String> members);

    @GET("groups/{id}")
    Observable<Group> getGroup(@Path("id") String id);

    @PATCH("groups/{id}")
    Observable<Group> updateGroup(@Path("id") String id, @Body UpdateGroupDto groupDto);

    @DELETE("groups/{id}")
    Observable<Group> deleteGroup(@Path("id") String id);
}
