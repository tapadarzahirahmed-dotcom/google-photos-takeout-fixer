package com.example.takeoutfixer.data.notifications;

import android.content.Context;
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
public final class FixNotificationManager_Factory implements Factory<FixNotificationManager> {
  private final Provider<Context> contextProvider;

  public FixNotificationManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FixNotificationManager get() {
    return newInstance(contextProvider.get());
  }

  public static FixNotificationManager_Factory create(Provider<Context> contextProvider) {
    return new FixNotificationManager_Factory(contextProvider);
  }

  public static FixNotificationManager newInstance(Context context) {
    return new FixNotificationManager(context);
  }
}
