package com.example.takeoutfixer.data.repository;

import android.content.Context;
import com.example.takeoutfixer.data.parser.TakeoutJsonParser;
import com.example.takeoutfixer.data.utils.ExifToolManager;
import com.example.takeoutfixer.data.utils.FFmpegManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class TakeoutRepositoryImpl_Factory implements Factory<TakeoutRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<TakeoutJsonParser> jsonParserProvider;

  private final Provider<ExifToolManager> exifToolManagerProvider;

  private final Provider<FFmpegManager> ffmpegManagerProvider;

  public TakeoutRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<TakeoutJsonParser> jsonParserProvider,
      Provider<ExifToolManager> exifToolManagerProvider,
      Provider<FFmpegManager> ffmpegManagerProvider) {
    this.contextProvider = contextProvider;
    this.jsonParserProvider = jsonParserProvider;
    this.exifToolManagerProvider = exifToolManagerProvider;
    this.ffmpegManagerProvider = ffmpegManagerProvider;
  }

  @Override
  public TakeoutRepositoryImpl get() {
    return newInstance(contextProvider.get(), jsonParserProvider.get(), exifToolManagerProvider.get(), ffmpegManagerProvider.get());
  }

  public static TakeoutRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<TakeoutJsonParser> jsonParserProvider,
      Provider<ExifToolManager> exifToolManagerProvider,
      Provider<FFmpegManager> ffmpegManagerProvider) {
    return new TakeoutRepositoryImpl_Factory(contextProvider, jsonParserProvider, exifToolManagerProvider, ffmpegManagerProvider);
  }

  public static TakeoutRepositoryImpl newInstance(Context context, TakeoutJsonParser jsonParser,
      ExifToolManager exifToolManager, FFmpegManager ffmpegManager) {
    return new TakeoutRepositoryImpl(context, jsonParser, exifToolManager, ffmpegManager);
  }
}
