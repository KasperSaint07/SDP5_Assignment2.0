package facade;

import devices.Light;
import devices.Thermostat;
import devices.SecurityCamera;

public class NightModeFacade {
    private final Light light;
    private final Thermostat thermostat;
    private final SecurityCamera camera;

    public NightModeFacade(Light light, Thermostat thermostat, SecurityCamera camera) {
        this.light = light;
        this.thermostat = thermostat;
        this.camera = camera;
    }

    public void run() {
        System.out.println("=== NIGHT MODE ===");
        light.off();
        thermostat.setNight();
        camera.enable();
    }
}
