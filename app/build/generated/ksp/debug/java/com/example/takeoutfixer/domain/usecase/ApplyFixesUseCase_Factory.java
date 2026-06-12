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
public final class ApplyFixesUseCase_Factory implements Factory<ApplyFixesUseCase> {
  private final Provider<TakeoutRepository> repositoryProvider;

  public ApplyFixesUseCase_Factory(Provider<TakeoutRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ApplyFixesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ApplyFixesUseCase_Factory create(Provider<TakeoutRepository> repositoryProvider) {
    return new ApplyFixesUseCase_Factory(repositoryProvider);
  }

  public static ApplyFixesUseCase newInstance(TakeoutRepository repository) {
    return new ApplyFixesUseCase(repository);
  }
}
