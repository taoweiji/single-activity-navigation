package com.taoweiji.navigation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Destination {
    public Uri uri;
    public Ability ability;
    public Bundle arguments = new Bundle();
    public NavOptions navOptions;
    public String name;
    public static final String URI_KEY = "__URI__";

    public static Destination with(Uri uri, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.uri = uri;
        destination.navOptions = navOptions;
        destination.parseUriQueryParameter(uri);
        return destination;
    }

    public static Destination with(String name, Bundle arguments) {
        Destination destination = new Destination();
        destination.name = name;
        destination.arguments = arguments;
        return destination;
    }

    public static Destination with(Ability ability, Bundle arguments) {
        Destination destination = new Destination();
        destination.ability = ability;
        destination.arguments = arguments;
        return destination;
    }

    public Destination navOptions(NavOptions navOptions) {
        this.navOptions = navOptions;
        return this;
    }

    public void parseUriQueryParameter(Uri uri) {
        for (String key : uri.getQueryParameterNames()) {
            arguments.putString(key, uri.getQueryParameter(key));
        }
        arguments.putParcelable(URI_KEY, uri);
    }
}