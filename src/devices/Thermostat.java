package devices;

public class Thermostat {
    private String mode = "COMFORT";
    private int targetC = 22;

    public void setEco() {
        mode = "ECO";
        targetC = 19;
        System.out.println("Thermostat: ECO (" + targetC + "°C)");
    }

    public void setComfort() {
        mode = "COMFORT";
        targetC = 22;
        System.out.println("Thermostat: COMFORT (" + targetC + "°C)");
    }

    public void setAway() {
        mode = "AWAY";
        targetC = 16;
        System.out.println("Thermostat: AWAY (" + targetC + "°C)");
    }

    public void setNight() {
        mode = "NIGHT";
        targetC = 18;
        System.out.println("Thermostat: NIGHT (" + targetC + "°C)");
    }

    public String getMode()   { return mode; }
    public int getTargetC()   { return targetC; }
}
