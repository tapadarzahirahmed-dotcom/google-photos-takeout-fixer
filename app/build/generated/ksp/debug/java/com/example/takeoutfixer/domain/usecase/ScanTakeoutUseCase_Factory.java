package com.example.takeoutfixer.domain.usecase;

import com.example.takeoutfixer.domain.repository.TakeoutRepository;
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
public final class ScanTakeoutUseCase_Factory implements Factory<ScanTakeoutUseCase> {
  private final Provider<TakeoutRepository> repositoryProvider;

  public ScanTakeoutUseCase_Factory(Provider<TakeoutRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ScanTakeoutUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ScanTakeoutUseCase_Factory create(Provider<TakeoutRepository> repositoryProvider) {
    return new ScanTakeoutUseCase_Factory(repositoryProvider);
  }

  public static ScanTakeoutUseCase newInstance(TakeoutRepository repository) {
    return new ScanTakeoutUseCase(repository);
  }
}
