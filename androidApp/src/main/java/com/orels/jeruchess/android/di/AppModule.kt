package com.orels.jeruchess.android.di

import android.app.Application
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.data.interactor.AuthInteractorImpl
import com.orels.jeruchess.android.domain.annotation.AuthConfigFile
import com.orels.jeruchess.android.domain.interactors.AuthInteractor
import com.orels.jeruchess.android.domain.model.ConfigFile
import com.orels.jeruchess.authentication.data.KtorAuthClient
import com.orels.jeruchess.authentication.data.SqlDelightAuthDataSource
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.core.data.local.DatabaseDriverFactory
import com.orels.jeruchess.database.JeruChessDatabase
import com.orels.jeruchess.main.data.KtorMainClient
import com.orels.jeruchess.main.data.SqlDelightMainDataSource
import com.orels.jeruchess.main.domain.data.MainClient
import com.orels.jeruchess.main.domain.data.MainDataSource
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
    fun provideHttpClient(): HttpClient {
        return com.orels.jeruchess.core.data.remote.HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideAuthClient(httpClient: HttpClient): com.orels.jeruchess.authentication.domain.client.AuthClient {
        return KtorAuthClient()
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideSqlDelightUserDataSource(
        driver: SqlDriver,
    ): AuthDataSource {
        return SqlDelightAuthDataSource(JeruChessDatabase(driver))
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
    fun provideAuthInteractor(
        authInteractor: AuthInteractorImpl
    ): AuthInteractor = authInteractor

    @Provides
    @AuthConfigFile
    fun provideAuthConfigFile(
    ): ConfigFile = ConfigFile(R.raw.dev_amplifyconfiguration)

}