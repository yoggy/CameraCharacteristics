package net.sabamiso.android.cameracharacteristics;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.widget.TextView;

public class CameraCharacteristicsActivity extends AppCompatActivity {

    public static final String TAG = "CameraCharacteristics";

    TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_characteristics);

        textMessage = (TextView)findViewById(R.id.textMessage);
        textMessage.setText("");

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String camera_id : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(camera_id);
                msg("====================");
                msg("camera_id=" + camera_id);

                // facing : 0:フロントカメラ, 1: リアカメラ, 2: External
                // https://developer.android.com/reference/android/hardware/camera2/CameraCharacteristics#LENS_FACING
                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                msg("LENS_FACING=" + (facing == CameraCharacteristics.LENS_FACING_FRONT ? "front" : "back"));

                // 焦点距離 (単位はミリ)
                // 光学ズームがサポートされている場合は、複数の値が入っている（はず)
                // https://developer.android.com/reference/android/hardware/camera2/CameraCharacteristics#LENS_INFO_AVAILABLE_FOCAL_LENGTHS
                float[] f = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                msg("LENS_INFO_AVAILABLE_FOCAL_LENGTHS={f:" + f[0] + "}");

                // CMOSセンサの物理サイズ (単位はミリ)
                // https://developer.android.com/reference/android/hardware/camera2/CameraCharacteristics.html#SENSOR_INFO_PHYSICAL_SIZE
                SizeF ps = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
                msg("SENSOR_INFO_PHYSICAL_SIZE={w:" + ps.getWidth() + ",h:" + ps.getHeight() + "}");

                // CMOSセンサのピクセルサイズ (単位はピクセル数)
                Size pas = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
                msg("SENSOR_INFO_PIXEL_ARRAY_SIZE={w:" + pas.getWidth() + ",h:" + pas.getHeight() + "}");

                // CMOSセンサで実際に使われているピクセル領域(単位はピクセル数)
                Rect aas = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                msg("SENSOR_INFO_ACTIVE_ARRAY_SIZE={left=" + aas.left + ",right:"+aas.right + ",top:" + aas.top + ",bottom:" + aas.bottom + "}");

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void msg(String msg) {
        Log.d(TAG, msg);

        String str = textMessage.getText().toString();
        str += msg + "\n";
        textMessage.setText(str);
    }
}
