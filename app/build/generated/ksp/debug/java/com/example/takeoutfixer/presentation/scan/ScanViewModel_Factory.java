package com.example.takeoutfixer.presentation.scan;

import com.example.takeoutfixer.domain.usecase.ScanTakeoutUseCase;
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
public final class ScanViewModel_Factory implements Factory<ScanViewModel> {
  private final Provider<ScanTakeoutUseCase> scanUseCaseProvider;

  public ScanViewModel_Factory(Provider<ScanTakeoutUseCase> scanUseCaseProvider) {
    this.scanUseCaseProvider = scanUseCaseProvider;
  }

  @Override
  public ScanViewModel get() {
    return newInstance(scanUseCaseProvider.get());
  }

  public static ScanViewModel_Factory create(Provider<ScanTakeoutUseCase> scanUseCaseProvider) {
    return new ScanViewModel_Factory(scanUseCaseProvider);
  }

  public static ScanViewModel newInstance(ScanTakeoutUseCase scanUseCase) {
    return new ScanViewModel(scanUseCase);
  }
}
