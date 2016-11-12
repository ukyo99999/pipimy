package com.macrowell.pipimy.utility;

import android.util.Log;

import tw.com.pipimy.app.android.BuildConfig;

public class LogUtility {
    private static boolean enable = true;
    private String tag;

    private LogUtility(String tag) {
        this.tag = tag;
    }

    public static LogUtility getInstance(Class<?> classes) {
        String tag = classes.getName();
        return new LogUtility(tag);

    }

    public String getTag() {
        return tag;
    }

    public void d(String message) {
        if (enable) {
            Log.d(this.tag, message);
        }
    }

    public void v(String message) {
        if (enable) {
            Log.v(this.tag, message);
        }
    }

    public void i(String message) {
        if (enable) {
            Log.i(this.tag, message);
        }
    }

    public void w(String message) {
        if (enable) {
            Log.w(this.tag, message);
        }
    }

    public void e(String message) {
        if (enable) {
            Log.e(this.tag, message);
        }
    }

    public void w(String message, Throwable t) {
        if (enable) {
            Log.w(this.tag, message, t);
        }
    }

    public void v(String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.v(this.tag, message, t);
        }
    }

    public void d(String message, Throwable t) {
        if (enable) {
            Log.d(this.tag, message, t);
        }
    }

    public void i(String message, Throwable t) {
        if (enable) {
            Log.i(this.tag, message, t);
        }
    }

    public void e(String message, Throwable t) {
        if (enable) {
            Log.e(this.tag, message, t);
        }
    }
}
