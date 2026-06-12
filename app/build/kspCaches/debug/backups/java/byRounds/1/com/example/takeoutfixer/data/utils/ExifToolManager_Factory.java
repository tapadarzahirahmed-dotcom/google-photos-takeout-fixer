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
public final class ExifToolManager_Factory implements Factory<ExifToolManager> {
  private final Provider<Context> contextProvider;

  public ExifToolManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ExifToolManager get() {
    return newInstance(contextProvider.get());
  }

  public static ExifToolManager_Factory create(Provider<Context> contextProvider) {
    return new ExifToolManager_Factory(contextProvider);
  }

  public static ExifToolManager newInstance(Context context) {
    return new ExifToolManager(context);
  }
}
