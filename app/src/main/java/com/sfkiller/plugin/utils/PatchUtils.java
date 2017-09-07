package com.sfkiller.plugin.utils;

/**
 * Created by qipu on 2017/9/7.
 */

public class PatchUtils {

    public static native int patch(String oldApkPath, String newApkPath, String patchPath);
}
