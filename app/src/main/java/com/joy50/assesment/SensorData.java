package com.joy50.assesment;

public class SensorData {
    private float currentLightValue;
    private float currentProximityValue;
    private float[] currentAccelerometerValues;
    private float[] currentGyroscopeValues;

    public float getCurrentLightValue() {
        return currentLightValue;
    }

    public void setCurrentLightValue(float currentLightValue) {
        this.currentLightValue = currentLightValue;
    }

    public float getCurrentProximityValue() {
        return currentProximityValue;
    }

    public void setCurrentProximityValue(float currentProximityValue) {
        this.currentProximityValue = currentProximityValue;
    }

    public float[] getCurrentAccelerometerValues() {
        return currentAccelerometerValues;
    }

    public void setCurrentAccelerometerValues(float[] currentAccelerometerValues) {
        this.currentAccelerometerValues = currentAccelerometerValues;
    }

    public float[] getCurrentGyroscopeValues() {
        return currentGyroscopeValues;
    }

    public void setCurrentGyroscopeValues(float[] currentGyroscopeValues) {
        this.currentGyroscopeValues = currentGyroscopeValues;
    }
}
