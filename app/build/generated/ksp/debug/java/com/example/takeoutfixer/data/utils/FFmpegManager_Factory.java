package com.example.takeoutfixer.data.utils;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class FFmpegManager_Factory implements Factory<FFmpegManager> {
  private final Provider<Context> contextProvider;

  public FFmpegManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FFmpegManager get() {
    return newInstance(contextProvider.get());
  }

  public static FFmpegManager_Factory create(Provider<Context> contextProvider) {
    return new FFmpegManager_Factory(contextProvider);
  }

  public static FFmpegManager newInstance(Context context) {
    return new FFmpegManager(context);
  }
}
