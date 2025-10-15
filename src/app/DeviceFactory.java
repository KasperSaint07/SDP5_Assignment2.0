package app;

import devices.Light;
import devices.MusicSystem;
import devices.SecurityCamera;
import devices.Thermostat;

/**
 * Простая фабрика устройств.
 * Если добавишь новый девайс, просто добавь сюда ещё один create-метод.
 */
public class DeviceFactory {

    public Light createLight() {
        return new Light();
    }

    public MusicSystem createMusic() {
        return new MusicSystem();
    }

    public Thermostat createThermostat() {
        return new Thermostat();
    }

    public SecurityCamera createCamera() {
        return new SecurityCamera();
    }
}
