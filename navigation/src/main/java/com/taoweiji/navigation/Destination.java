package com.taoweiji.navigation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Destination {
    public Uri uri;
    public Fragment fragment;
    public Ability ability;
    public Bundle arguments = new Bundle();
    public NavOptions navOptions;
    public String name;

    public static Destination with(Uri uri, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.uri = uri;
        destination.navOptions = navOptions;
        destination.parseUriQueryParameter(uri);
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

    public void parseUriQueryParameter(Uri uri) {
        for (String key : uri.getQueryParameterNames()) {
            arguments.putString(key, uri.getQueryParameter(key));
        }
    }
}