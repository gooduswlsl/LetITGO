package com.sook.cs.letitgo.lib;

import android.content.Context;

/**
 * 문자열 관련 라이브러리
 */
public class StringLib {
    public final String TAG = StringLib.class.getSimpleName();
    private volatile static StringLib instance;

    protected StringLib() {
    }

    public static StringLib getInstance() {
        if (instance == null) {
            synchronized (StringLib.class) {
                if (instance == null) {
                    instance = new StringLib();
                }
            }
        }
        return instance;
    }

    /**
     * 문자열이 null이거나 ""인지를 파악한다.
     * @param str 문자열 객체
     * @return null이거나 ""이라면 true, 아니라면 false
     */
    public boolean isBlank(String str) {
        if (str == null || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }


}
