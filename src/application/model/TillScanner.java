package application.model;

/**
 * Created by Long laptop on 9/24/2018.
 */
public class TillScanner {
    private static int scannerId = 0;
    private int sensorSpeed = 2000;
    private int reductionTime = 500;

    public TillScanner() {
        scannerId++;
    }

    public static int getScannerId() {
        return scannerId;
    }

    public static void setScannerId(int scannerId) {
        TillScanner.scannerId = scannerId;
    }

    public int getSensorSpeed() {
        return sensorSpeed;
    }

    public void setSensorSpeed(int sensorSpeed) {
        this.sensorSpeed = sensorSpeed;
    }

    public int getReductionTime() {
        return reductionTime;
    }

    public void setReductionTime(int reductionTime) {
        this.reductionTime = reductionTime;
    }
}
