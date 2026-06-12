package com.example.takeoutfixer.data.parser;

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
public final class TakeoutJsonParser_Factory implements Factory<TakeoutJsonParser> {
  private final Provider<Context> contextProvider;

  public TakeoutJsonParser_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TakeoutJsonParser get() {
    return newInstance(contextProvider.get());
  }

  public static TakeoutJsonParser_Factory create(Provider<Context> contextProvider) {
    return new TakeoutJsonParser_Factory(contextProvider);
  }

  public static TakeoutJsonParser newInstance(Context context) {
    return new TakeoutJsonParser(context);
  }
}
