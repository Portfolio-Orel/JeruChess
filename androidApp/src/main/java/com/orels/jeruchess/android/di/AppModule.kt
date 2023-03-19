package com.orels.jeruchess.android.di

import android.app.Application
import com.orels.jeruchess.NetworkConstants
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.data.interactor.AuthInteractorImpl
import com.orels.jeruchess.android.domain.AuthInteractor
import com.orels.jeruchess.android.domain.annotation.AuthConfigFile
import com.orels.jeruchess.android.domain.annotation.BaseUrl
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.core.data.interactor.EventsInteractorImpl
import com.orels.jeruchess.core.data.local.DatabaseDriverFactory
import com.orels.jeruchess.core.data.remote.HttpClientFactory
import com.orels.jeruchess.core.domain.interactors.EventsInteractor
import com.orels.jeruchess.core.domain.interactors.UserInteractor
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.chess_user_data.KtorChessUserDataClient
import com.orels.jeruchess.main.data.chess_user_data.SqlDelightChessUserDataDataSource
import com.orels.jeruchess.main.data.events.KtorEventsClient
import com.orels.jeruchess.main.data.events.SqlDelightEventsDataSource
import com.orels.jeruchess.main.data.events_participants.KtorEventsParticipantsClient
import com.orels.jeruchess.main.data.events_participants.SqlDelightEventsParticipantsDataSource
import com.orels.jeruchess.main.data.games.KtorGamesClient
import com.orels.jeruchess.main.data.games.SqlDelightGamesDataSource
import com.orels.jeruchess.main.data.main.KtorMainClient
import com.orels.jeruchess.main.data.main.SqlDelightMainDataSource
import com.orels.jeruchess.main.data.users.KtorUsersClient
import com.orels.jeruchess.main.data.users.SqlDelightUsersDataSource
import com.orels.jeruchess.main.domain.data.chess_user_data.ChessUserDataClient
import com.orels.jeruchess.main.domain.data.chess_user_data.ChessUserDataDataSource
import com.orels.jeruchess.main.domain.data.events.EventsClient
import com.orels.jeruchess.main.domain.data.events.EventsDataSource
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsClient
import com.orels.jeruchess.main.domain.data.events_participants.EventsParticipantsDataSource
import com.orels.jeruchess.main.domain.data.games.GamesClient
import com.orels.jeruchess.main.domain.data.games.GamesDataSource
import com.orels.jeruchess.main.domain.data.main.MainClient
import com.orels.jeruchess.main.domain.data.main.MainDataSource
import com.orels.jeruchess.main.domain.data.users.UsersClient
import com.orels.jeruchess.main.domain.data.users.UsersDataSource
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(): String = NetworkConstants.BASE_URL

    @Provides
    @Singleton
    fun provideHttpClient(
        @BaseUrl baseUrl: String,
       dataSource: UsersDataSource
    ): HttpClient = HttpClientFactory()
        .create(
            baseUrl = baseUrl,
            dataSource = dataSource
        )


    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideMainClient(
    ): MainClient = KtorMainClient()

    @Provides
    @Singleton
    fun provideMainDataSource(
        driver: SqlDriver,
    ): MainDataSource {
        return SqlDelightMainDataSource(JeruChessDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideUserClient(
        httpClient: HttpClient
    ): UsersClient = KtorUsersClient(
        httpClient = httpClient
    )

    @Provides
    @Singleton
    fun provideUsersDataSource(
        driver: SqlDriver,
    ): UsersDataSource =
        SqlDelightUsersDataSource(JeruChessDatabase(driver))


    @Provides
    @Singleton
    fun provideEventsDataSource(
        driver: SqlDriver,
    ): EventsDataSource =
        SqlDelightEventsDataSource(JeruChessDatabase(driver))

    @Provides
    @Singleton
    fun provideEventsClient(
        httpClient: HttpClient
    ): EventsClient = KtorEventsClient(
        httpClient = httpClient
    )

    @Provides
    @Singleton
    fun provideEventsParticipantsDataSource(
        driver: SqlDriver,
    ): EventsParticipantsDataSource =
        SqlDelightEventsParticipantsDataSource(JeruChessDatabase(driver))

    @Provides
    @Singleton
    fun provideEventsParticipantsClient(
        httpClient: HttpClient
    ): EventsParticipantsClient = KtorEventsParticipantsClient(
        httpClient = httpClient
    )

    @Provides
    @Singleton
    fun provideChessUsersDataSource(
        driver: SqlDriver,
    ): ChessUserDataDataSource =
        SqlDelightChessUserDataDataSource(JeruChessDatabase(driver))

    @Provides
    @Singleton
    fun provideChessUsersClient(
        httpClient: HttpClient
    ): ChessUserDataClient = KtorChessUserDataClient(
        httpClient = httpClient
    )

    @Provides
    @Singleton
    fun provideGamesDataSource(
        driver: SqlDriver,
    ): GamesDataSource =
        SqlDelightGamesDataSource(JeruChessDatabase(driver))

    @Provides
    @Singleton
    fun provideGamesClient(
        httpClient: HttpClient
    ): GamesClient = KtorGamesClient(
        httpClient = httpClient
    )

    @Provides
    @Singleton
    fun provideAuthInteractor(
        authInteractor: AuthInteractorImpl
    ): AuthInteractor = authInteractor

    @Provides
    @AuthConfigFile
    fun provideAuthConfigFile(
    ): ConfigFile = ConfigFile(R.raw.dev_amplifyconfiguration)

    @Provides
    @Singleton
    fun provideEventsInteractor(
        eventsDataSource: EventsDataSource,
        eventsClient: EventsClient,
        eventsParticipantsClient: EventsParticipantsClient,
        eventsParticipantsDataSource: EventsParticipantsDataSource,
        userInteractor: UserInteractor

    ): EventsInteractor = EventsInteractorImpl(
        eventsDataSource = eventsDataSource,
        eventsClient = eventsClient,
        eventsParticipantsClient = eventsParticipantsClient,
        eventsParticipantsDataSource = eventsParticipantsDataSource,
        userInteractor = userInteractor
    )

}