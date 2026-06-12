package com.example.takeoutfixer;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
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
public final class TakeoutFixerApp_MembersInjector implements MembersInjector<TakeoutFixerApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public TakeoutFixerApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<TakeoutFixerApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new TakeoutFixerApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(TakeoutFixerApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.example.takeoutfixer.TakeoutFixerApp.workerFactory")
  public static void injectWorkerFactory(TakeoutFixerApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
