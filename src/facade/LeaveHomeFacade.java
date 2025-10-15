package facade;

import devices.Light;
import devices.MusicSystem;
import devices.SecurityCamera;
import devices.Thermostat;

public class LeaveHomeFacade {
    private final Light light;
    private final MusicSystem music;
    private final SecurityCamera camera;
    private final Thermostat thermostat;

    public LeaveHomeFacade(Light light, MusicSystem music,
                           SecurityCamera camera, Thermostat thermostat) {
        this.light = light;
        this.music = music;
        this.camera = camera;
        this.thermostat = thermostat;
    }

    public void run() {
        System.out.println("=== LEAVE HOME ===");
        light.off();
        music.stop();
        camera.enable();
        thermostat.setAway(); // экономичный режим когда никого нет
    }
}
