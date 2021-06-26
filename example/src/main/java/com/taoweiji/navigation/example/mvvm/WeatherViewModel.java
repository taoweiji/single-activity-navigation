package com.taoweiji.navigation.example.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class WeatherViewModel extends ViewModel {
    public MutableLiveData<Weather> weatherData = new MutableLiveData<>();

    public void updateWeather() {
        weatherData.postValue(new Weather(new Random().nextInt(32)));
    }
}
