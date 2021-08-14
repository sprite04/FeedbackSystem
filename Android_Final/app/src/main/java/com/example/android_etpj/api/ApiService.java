package com.example.android_etpj.api;

import com.example.android_etpj.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson=new GsonBuilder()
            //.setDateFormat("yyyy-MM-dd")
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.9/systemfeedback/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);


    //Feedback
    @GET("feedback")
    Call<List<Feedback>> getFeedbacks();

    @GET("feedback/{id}")
    Call<Feedback> getFeedbackById(@Path("id") int id);

    @POST("feedback")
    Call<Boolean> addFeedback(@Body FeedbackQuestion feedbackQuestion);

    @PUT("feedback")
    Call<Boolean> editFeedback(@Body FeedbackQuestion feedbackQuestion);

    @DELETE("feedback/{id}")
    Call<Boolean> deleteFeedback(@Path("id") int id);

    @GET("feedback")
    Call<Integer> checkFeedbackUsed(@Query("idUsed") int idUsed);

    //Module
    @GET("module")
    Call<List<Module>> getModules();

    @GET("module")
    Call<Boolean> checkModuleUsed(@Query("idUsed") int idUsed);

    @DELETE("module/{id}")
    Call<Boolean> deleteModule(@Path("id") int id);

    @POST("module")
    Call<Boolean> addModule(@Body Module m);

    @PUT("module")
    Call<Boolean> editModule(@Body Module m);

    @GET("module")
    Call<List<Module>> getModuleByIdTrainer(@Query("idTrainer") String idTrainer);

    @GET("module")
    Call<List<Module>> getModuleByIdTrainee(@Query("idTrainee") String idTrainee);


    //Admin
    @GET("admin")
    Call<List<Admin>> getAdmins();

    @GET("admin")
    Call<Admin> loginAdmin(
            @Query("username") String username,
            @Query("password") String password);


    //Trainee
    @GET("trainee")
    Call<Trainee> getTraineeByUsername(@Query("username") String username);

    @GET("trainee")
    Call<List<Trainee>> getTrainee();

    @GET("trainee")
    Call<Trainee> loginTrainee(@Query("username") String username,
                               @Query("password") String password);

    //Trainer
    @GET("trainer")
    Call<List<Trainer>> getTrainers();

    @GET("trainer")
    Call<Trainer> loginTrainer(
            @Query("username") String username,
            @Query("password") String password);


    //Assignment
    @GET("assignment")
    Call<List<Assignment>> getAssignmentsByTrainee(@Query("idTrainee") String idTrainee);

    @GET("assignment")
    Call<List<Assignment>> getAssignments();

    @GET("assignment")
    Call<List<Assignment>> searchAssignments(@Query("stringSearch") String stringSearch);

    @GET("assignment")
    Call<List<Assignment>> getAssignmentByRegistrationCode(@Query("registrationCode") String registrationCode);

    @POST("assignment")
    Call<Boolean> addAssignment(@Body Assignment a);

    @DELETE("assignment")
    Call<Boolean> deleteAssignment(@Query("idClass") int idClass,
                                   @Query("idModule") int idModule);

    @PUT("assignment")
    Call<Boolean> editAssignment(@Body Assignment a);

    //Topic
    @GET("topic")
    Call<List<Topic>> getTopics();

    @GET("topic")
    Call<List<Topic>> getTopicsByFeedback(@Query("idFeedback") int idFeedback);


    //TypeFeedback
    @GET("typefeedback")
    Call<List<TypeFeedback>> getTypeFeedbacks();

    //Answer
    @GET("answer")
    Call<Boolean> checkAnswerUsed(@Query("clss") int idClass,
                                  @Query("module") int idModule,
                                  @Query("traineeId") String idTrainee);

    @POST("answer")
    Call<Boolean> addAnswers(@Body Review review);

    @GET("answer")
    Call<List<Answer>> getAnswersByClassModule(@Query("idClass") int idClass,
                                               @Query("idModule") int idModule);





    //TopicAnswers
    @GET("topicanswers")
    Call<List<TopicAnswers>> getTopicAnswersByClassModule(@Query("idClass") int idClass,
                                                          @Query("idModule") int idModule);

    @GET("topicanswers")
    Call<List<TopicStatistic>> getTopicStatisticByClassModule(@Query("classID") int classID,
                                                              @Query("moduleID") int moduleID);




    //TraineeComment
    @POST("traineecomment")
    Call<Boolean> addTraineeComment(@Body Trainee_Comment traineeComment);

    @GET("traineecomment")
    Call<Boolean> checkCommentUsed(@Query("idClass") int idClass,
                                   @Query("idModule") int idModule,
                                   @Query("idTrainee") String idTrainee);

    //Class
    @GET("class")
    Call<List<com.example.android_etpj.models.Class>> getClasses();

    @GET("class/{id}")
    Call<com.example.android_etpj.models.Class> getClassById(@Path("id") int id);

    @GET("class")
    Call<List<com.example.android_etpj.models.Class>> getClassesByTrainer(@Query("idTrainer") String idTrainer);

    @GET("class")
    Call<List<com.example.android_etpj.models.Class>> getClassesByTrainee(@Query("idTrainee") String idTrainee);

    @POST("class")
    Call<Boolean> addClass(@Body com.example.android_etpj.models.Class cl);

    @PUT("class")
    Call<Boolean> editClass(@Body com.example.android_etpj.models.Class cl);

    @DELETE("class/{id}")
    Call<Boolean> deleteClass(@Path("id") int id);


    //Enrollment
    @GET("enrollment")
    Call<List<Enrollment>> getEnrollment();

    @GET("enrollment")
    Call<List<Enrollment>> getEnrollmentByIdClass(@Query("idClass") int idClass);

    @GET("enrollment")
    Call<List<Enrollment>> searchEnrollment(@Query("idClass") int classId);

    @POST("enrollment")
    Call<Boolean> addEnrollment(@Query("classId") int classId,
                                @Query("username") String username);

    @PUT("enrollment")
    Call<Boolean> editEnrollment(@Query("classIdOld") int classIdOld,
                                 @Query("classIdNew") int classIdNew,
                                 @Query("username") String username);
    @DELETE("enrollment")
    Call<Boolean> deleteEnrollment(@Query("classId") int classId,
                                   @Query("username") String username);



    //Question
    @GET("question")
    Call<List<Question>> getQuestions();

    @GET("question")
    Call<List<Question>> getQuestionByIdTopic(@Query("idTopic") int idTopic);

    @GET("question/{id}")
    Call<Question> getQuestionById(@Path("id") int id);

    @POST("question")
    Call<Boolean> addQuestion(@Body Question qs);

    @PUT("question")
    Call<Boolean> editQuestion(@Body Question qs);

    @DELETE("question/{id}")
    Call<Boolean> deleteQuestion(@Path("id") int id);

    @GET("question")
    Call<Integer> checkQuestionUsed(@Query("idUsed") int idUsed);


    //CommentResult
    @GET("traineecomment")
    Call<List<CommentResult>> getCommentResult(@Query("classId") int classId,
                                               @Query("moduleId") int moduleId);





}
