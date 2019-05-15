package com.github.travelplannerapp.travels;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class TravelsViewModel {
    @Binds
    abstract TravelsContract.View provideTravelsView(TravelsActivity travelsActivity);
}
