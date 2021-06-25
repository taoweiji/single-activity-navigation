package com.taoweiji.navigation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Destination {
    Uri uri;
    Fragment fragment;
    Ability ability;
    Bundle arguments;
    NavOptions navOptions;
    String name;

    public static Destination with(Uri uri, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.uri = uri;
        destination.navOptions = navOptions;
        return destination;
    }

    public static Destination with(String name, Bundle arguments, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.name = name;
        destination.arguments = arguments;
        destination.navOptions = navOptions;
        return destination;
    }

    public static Destination with(Fragment fragment, Bundle arguments, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.fragment = fragment;
        destination.arguments = arguments;
        destination.navOptions = navOptions;
        return destination;
    }

    public static Destination with(Ability ability, Bundle arguments, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.ability = ability;
        destination.arguments = arguments;
        destination.navOptions = navOptions;
        return destination;
    }
}