package app;

import devices.*;
import decorators.DecoratorBuilder;
import devices.Device;

/**
 * Контекст: хранит устройства, флаги фич, профиль энергосбережения (int) и состояние "облака",
 * пересобирает декорированные ссылки.
 *
 * energyProfile:
 *   0 = OFF
 *   1 = MILD
 *   2 = AGGRESSIVE
 */
public class SmartHomeContext {
    private final Light light;
    private final MusicSystem music;
    private final Thermostat thermostat;
    private final SecurityCamera camera;

    // Light: Voice/Remote
    private boolean lightVoice = false;
    private boolean lightRemote = false;

    // Music: Remote
    private boolean musicRemote = false;

    // Energy: профиль
    private int energyProfile = 0; // 0=OFF, 1=MILD, 2=AGGRESSIVE

    // Remote cloud connection (общий)
    private boolean cloudConnected = false;

    // DECORATED devices (пересобираются при изменении флагов/профиля)
    private Device lightDecorated;
    private Device musicDecorated;

    private final DecoratorBuilder builder = new DecoratorBuilder();

    public SmartHomeContext(DeviceFactory factory) {
        this.light = factory.createLight();
        this.music = factory.createMusic();
        this.thermostat = factory.createThermostat();
        this.camera = factory.createCamera();
        rebuild();
    }

    /** Пересобрать декорированные ссылки на основе текущих флагов/профиля. */
    public final void rebuild() {
        this.lightDecorated = builder.buildForLight(light, lightVoice, lightRemote, energyProfile);
        this.musicDecorated = builder.buildForMusic(music, musicRemote, energyProfile);
    }

    // --- RAW getters ---
    public Light getLightRaw()         { return light; }
    public MusicSystem getMusicRaw()   { return music; }
    public Thermostat getThermostat()  { return thermostat; }
    public SecurityCamera getCamera()  { return camera; }
    public boolean isLightRemote() { return lightRemote; }
    public boolean isMusicRemote() { return musicRemote; }


    // --- DECORATED getters ---
    public Device getLightDecorated()  { return lightDecorated; }
    public Device getMusicDecorated()  { return musicDecorated; }

    // --- Feature toggles / setters ---
    public void toggleLightVoice()     { lightVoice  = !lightVoice;  rebuild(); }
    public void toggleLightRemote()    { lightRemote = !lightRemote; rebuild(); }
    public void toggleMusicRemote()    { musicRemote = !musicRemote; rebuild(); }

    public void setEnergyProfile(int p) {
        if (p < 0 || p > 2) p = 0;
        energyProfile = p;
        rebuild();
    }
    public int  getEnergyProfile() { return energyProfile; }

    public void setCloudConnected(boolean v) { cloudConnected = v; }
    public boolean isCloudConnected()        { return cloudConnected; }

    // --- state for UI ---
    public boolean isLightVoice()    { return lightVoice; }
    // helper для красивого текста профиля
    public static String energyProfileToText(int p) {
        return switch (p) {
            case 1 -> "MILD";
            case 2 -> "AGGRESSIVE";
            default -> "OFF";
        };
    }
}
