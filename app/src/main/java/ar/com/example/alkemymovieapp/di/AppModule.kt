package ar.com.example.alkemymovieapp.di

import android.content.Context
import androidx.room.Room
import ar.com.example.alkemymovieapp.application.AppConstants
import ar.com.example.alkemymovieapp.core.WebService
import ar.com.example.alkemymovieapp.data.local.AppDatabase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Retrofit
    @Singleton
    @Provides
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()


    @Singleton
    @Provides
    fun providesWebService(retrofit: Retrofit) = retrofit.create(WebService::class.java)


    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java,
        "movie_table").build()

    @Singleton
    @Provides
    fun providesDao(db:AppDatabase) = db.movieDao()


}