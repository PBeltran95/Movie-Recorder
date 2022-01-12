package ar.com.example.alkemymovieapp.di

import android.content.Context
import androidx.room.Room
import ar.com.example.alkemymovieapp.BuildConfig
import ar.com.example.alkemymovieapp.data.local.AppDatabase
import ar.com.example.alkemymovieapp.data.local.MovieDao
import ar.com.example.alkemymovieapp.data.remote.WebService
import ar.com.example.alkemymovieapp.repository.MovieTrailerRepository
import ar.com.example.alkemymovieapp.repository.MovieTrailerRepositoryImpl
import ar.com.example.alkemymovieapp.repository.RemoteMovieRepository
import ar.com.example.alkemymovieapp.repository.RemoteMovieRepositoryImpl
import ar.com.example.alkemymovieapp.repository.local.LocalListRepo
import ar.com.example.alkemymovieapp.repository.local.LocalListRepoImpl
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepository
import ar.com.example.alkemymovieapp.repository.local.LocalMovieRepositoryImpl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Retrofit
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()


    @Provides
    fun providesWebService(retrofit: Retrofit): WebService = retrofit.create(WebService::class.java)


    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java,
        "movie_table").build()

    @Provides
    fun providesDao(db:AppDatabase) = db.movieDao()

    @Provides
    fun providesLocalMovieRepoImpl(dao: MovieDao) : LocalListRepo {
        return LocalListRepoImpl(dao)
    }

    @Provides
    fun providesMovieTrailerRepoImpl(webService: WebService): MovieTrailerRepository {
        return MovieTrailerRepositoryImpl(webService)
    }

    @Provides
    fun providesRemoteDataSourceImpl(webService: WebService): RemoteMovieRepository {
        return RemoteMovieRepositoryImpl(webService)
    }

    @Provides
    fun providesLocalMovieDataSource(dao:MovieDao): LocalMovieRepository {
        return LocalMovieRepositoryImpl(dao)
    }

}