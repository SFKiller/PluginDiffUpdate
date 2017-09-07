package plugin.sfkiller.com.plugindiffupdate;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.sfkiller.plugin.utils.PatchUtils;

import java.io.File;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
//        System.loadLibrary("native-lib");
        Log.e("QIPU", " loadLibrary start ");
        System.loadLibrary("patch");
        Log.e("QIPU", " loadLibrary end ");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("QIPU", " onCreate ");
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("QIPU", " checkCallingOrSelfPermission ");
            doPatch();
        } else {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 100);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("QIPU", " onRequestPermissionsResult ");
        if (100 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("QIPU", " PERMISSION_GRANTED ");
                doPatch();
            }
        }
    }

    private void doPatch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                patch();
            }
        }).start();
    }

    private void patch() {
        Log.e("QIPU", " patch ");
        String oldApkPath = Environment.getExternalStorageDirectory() + File.separator + "old.apk";
        String patchPath = Environment.getExternalStorageDirectory() + File.separator + "patch.patch";
        String newApkPath = Environment.getExternalStorageDirectory() + File.separator + "new.apk";

        Log.e("QIPU", " oldApkPath  : " + oldApkPath);

        File oldApkFile = new File(oldApkPath);
        File patchApkFile = new File(patchPath);
        if (oldApkFile.exists() && patchApkFile.exists()) {
            Log.e("QIPU", " oldApkFile.exists ");
            long currentTime = System.currentTimeMillis();
            Log.e("QIPU", " currentTime ");
            int result = -1111111;
            try {
                result = PatchUtils.patch(oldApkPath, newApkPath, patchPath);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("QIPU", "Exception e : " + e.getMessage());
            }
            Log.e("QIPU", "patch take : " + (System.currentTimeMillis() - currentTime));
            Log.e("QIPU", " patch result : " + result);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
