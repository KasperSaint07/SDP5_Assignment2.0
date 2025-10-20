package devices;

public class Thermostat {
    private String mode = "COMFORT"; // COMFORT/ECO/NIGHT/AWAY/OFF
    private int targetC = 22;        // 16..30
    private int humidity = 45;       // 30..70

    public void setComfort() { mode = "COMFORT"; targetC = 22; System.out.println(status()); }
    public void setEco()     { mode = "ECO";     targetC = 19; System.out.println(status()); }
    public void setNight()   { mode = "NIGHT";   targetC = 18; System.out.println(status()); }
    public void setAway()    { mode = "AWAY";    targetC = 16; System.out.println(status()); }
    public void setOff()     { mode = "OFF";                     System.out.println(status()); }

    public void setTemperature(int c) {
        int v = Math.max(16, Math.min(30, c));
        targetC = v;
        System.out.println("Thermostat: SET TEMP (" + targetC + "°C), mode=" + mode);
    }

    public void setHumidity(int h) {
        int v = Math.max(30, Math.min(70, h));
        humidity = v;
        System.out.println("Thermostat: SET HUMIDITY (" + humidity + "%), mode=" + mode);
    }

    public String status() {
        if ("OFF".equals(mode)) return "Thermostat: OFF";
        return "Thermostat: " + mode + " (" + targetC + "°C, " + humidity + "%)";
    }

    public String getMode()   { return mode; }
    public int getTargetC()   { return targetC; }
    public int getHumidity()  { return humidity; }
}
    