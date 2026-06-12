package com.example.takeoutfixer.presentation.processing;

import com.example.takeoutfixer.data.notifications.FixNotificationManager;
import com.example.takeoutfixer.domain.repository.TakeoutRepository;
import com.example.takeoutfixer.domain.usecase.ApplyFixesUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ProcessingViewModel_Factory implements Factory<ProcessingViewModel> {
  private final Provider<ApplyFixesUseCase> applyFixesUseCaseProvider;

  private final Provider<TakeoutRepository> repositoryProvider;

  private final Provider<FixNotificationManager> notificationManagerProvider;

  public ProcessingViewModel_Factory(Provider<ApplyFixesUseCase> applyFixesUseCaseProvider,
      Provider<TakeoutRepository> repositoryProvider,
      Provider<FixNotificationManager> notificationManagerProvider) {
    this.applyFixesUseCaseProvider = applyFixesUseCaseProvider;
    this.repositoryProvider = repositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
  }

  @Override
  public ProcessingViewModel get() {
    return newInstance(applyFixesUseCaseProvider.get(), repositoryProvider.get(), notificationManagerProvider.get());
  }

  public static ProcessingViewModel_Factory create(
      Provider<ApplyFixesUseCase> applyFixesUseCaseProvider,
      Provider<TakeoutRepository> repositoryProvider,
      Provider<FixNotificationManager> notificationManagerProvider) {
    return new ProcessingViewModel_Factory(applyFixesUseCaseProvider, repositoryProvider, notificationManagerProvider);
  }

  public static ProcessingViewModel newInstance(ApplyFixesUseCase applyFixesUseCase,
      TakeoutRepository repository, FixNotificationManager notificationManager) {
    return new ProcessingViewModel(applyFixesUseCase, repository, notificationManager);
  }
}
