package com.taoweiji.navigation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Destination {
    private Uri uri;
    private Ability ability;
    private Bundle arguments = new Bundle();
    private NavOptions navOptions;
    private String name;
    public static final String URI_KEY = "__URI__";

    public static Destination with(Uri uri) {
        return with(uri, null);
    }

    public static Destination with(Uri uri, NavOptions navOptions) {
        Destination destination = new Destination();
        destination.uri = uri;
        destination.navOptions = navOptions;
        destination.parseUriQueryParameter(uri);
        return destination;
    }

    public static Destination with(String name) {
        return with(name, null);
    }

    public static Destination with(String name, Bundle arguments) {
        Destination destination = new Destination();
        destination.name = name;
        destination.arguments = arguments;
        return destination;
    }

    public static Destination with(Ability ability) {
        return with(ability,null);
    }
    public static Destination with(Ability ability, Bundle arguments) {
        Destination destination = new Destination();
        destination.ability = ability;
        destination.arguments = arguments;
        return destination;
    }

    public Destination withNavOptions(NavOptions navOptions) {
        this.navOptions = navOptions;
        return this;
    }

    public void parseUriQueryParameter(Uri uri) {
        for (String key : uri.getQueryParameterNames()) {
            arguments.putString(key, uri.getQueryParameter(key));
        }
        arguments.putParcelable(URI_KEY, uri);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Bundle getArguments() {
        return arguments;
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public NavOptions getNavOptions() {
        return navOptions;
    }

    public void setNavOptions(NavOptions navOptions) {
        this.navOptions = navOptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}