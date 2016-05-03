package com.wangyi.function;

import com.wangyi.define.EventName;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import com.wangyi.function.funchelp.Function;

public class SensorFunc implements Function {
	private static final SensorFunc INSTANCE = new SensorFunc();
	private SensorManager sensorManager; 
    private Vibrator vibrator; 
    private Handler handler;
    
    public static SensorFunc getInstance(){
    	return INSTANCE;
    }
    
	private SensorFunc(){}

    @Override
	public void init(Context context){
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); 
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public SensorFunc connect(Handler handler){
        if(this.handler != handler)
            this.handler = handler;
        return INSTANCE;
	}
	
	public void registerListener(){
		if (sensorManager != null) {
			sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	
	public void unregisterListener(){
		if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener); 
        } 
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener() { 
		 
        @Override 
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values; 
            float x = values[0];
            float y = values[1];
            float z = values[2];
            int medumValue = 19;
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) { 
                vibrator.vibrate(200); 
                Message msg = new Message();
                msg.what = EventName.UI.FINISH;
                msg.obj = EventName.SensorFunc.SENSOR;
                handler.sendMessage(msg); 
            }
        } 

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    };
}
