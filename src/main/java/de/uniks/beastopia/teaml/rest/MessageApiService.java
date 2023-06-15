package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface MessageApiService {
    @POST("{namespace}/{parent}/messages")
    Observable<Message> createMessage(@Path("namespace") String namespace, @Path("parent") String parent, @Body CreateMessageDto messageDto);

    @GET("{namespace}/{parent}/messages")
    Observable<List<Message>> getMessages(@Path("namespace") String namespace, @Path("parent") String parent, @Query("createdAfter") Date createdAfter, @Query("createdBefore") Date createdBefore, @Query("limit") Integer limit);

    @PATCH("{namespace}/{parent}/messages/{id}")
    Observable<Message> updateMessage(@Path("namespace") String namespace, @Path("parent") String parent, @Path("id") String id, @Body UpdateMessageDto messageDto);

    @DELETE("{namespace}/{parent}/messages/{id}")
    Observable<Message> deleteMessage(@Path("namespace") String namespace, @Path("parent") String parent, @Path("id") String id);
}
