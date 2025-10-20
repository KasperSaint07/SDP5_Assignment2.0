package facade;

import devices.Light;
import devices.Thermostat;
import devices.SecurityCamera;
import devices.MusicSystem;

public class NightModeFacade {
    private final Light light;
    private final Thermostat thermostat;
    private final SecurityCamera camera;
    private final MusicSystem music;

    public NightModeFacade(Light light, Thermostat thermostat, SecurityCamera camera, MusicSystem music) {
        this.light = light;
        this.thermostat = thermostat;
        this.camera = camera;
        this.music = music;
    }

    public void run() {
        System.out.println("=== NIGHT MODE ===");
        light.off();
        if (music != null) music.stop(); // явно музыка OFF
        thermostat.setNight();
        camera.enable();
    }
}
