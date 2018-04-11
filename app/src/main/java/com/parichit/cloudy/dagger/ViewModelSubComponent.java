package com.parichit.cloudy.dagger;

import com.parichit.cloudy.ui.MainViewModel;

import dagger.Subcomponent;

@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    MainViewModel mainViewModel();
}
