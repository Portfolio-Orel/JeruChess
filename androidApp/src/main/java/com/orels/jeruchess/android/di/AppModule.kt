package com.orels.jeruchess.android.di

import android.app.Application
import com.orels.jeruchess.authentication.data.KtorAuthClient
import com.orels.jeruchess.authentication.data.SqlDelightAuthDataSource
import com.orels.jeruchess.authentication.domain.dataSource.AuthDataSource
import com.orels.jeruchess.core.data.local.DatabaseDriverFactory
import com.orels.jeruchess.database.JeruChessDatabase
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
}