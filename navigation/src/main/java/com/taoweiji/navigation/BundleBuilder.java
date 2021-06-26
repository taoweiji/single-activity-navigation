package com.taoweiji.navigation;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

public class BundleBuilder {
    Bundle bundle = new Bundle();

    public BundleBuilder put(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder put(String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder put(String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    public BundleBuilder put(String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }

    public BundleBuilder put(String key, char value) {
        bundle.putChar(key, value);
        return this;
    }


    public BundleBuilder put(String key, short value) {
        bundle.putShort(key, value);
        return this;
    }

    public BundleBuilder put(String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }


    public BundleBuilder put(String key, String value) {
        bundle.putString(key, value);
        return this;
    }


    public BundleBuilder put(String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public BundleBuilder put(String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }


    public BundleBuilder put(String key, int[] value) {
        bundle.putIntArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, long[] value) {
        bundle.putLongArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, double[] value) {
        bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, float[] value) {
        bundle.putFloatArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, char[] value) {
        bundle.putCharArray(key, value);
        return this;
    }


    public BundleBuilder put(String key, short[] value) {
        bundle.putShortArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, byte[] value) {
        bundle.putByteArray(key, value);
        return this;
    }

    public BundleBuilder put(String key, Bundle value) {
        bundle.putBundle(key, value);
        return this;
    }

    public BundleBuilder put(String key, CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }

    public Bundle build() {
        return bundle;
    }
}
