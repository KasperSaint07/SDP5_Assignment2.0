package app;

import devices.Device;
import devices.Light;
import devices.MusicSystem;
import devices.SecurityCamera;
import devices.Thermostat;
import decorators.DecoratorBuilder;

/**
 * Единая "контекст"-модель приложения:
 * - хранит сырые устройства,
 * - хранит флаги фич для декораторов,
 * - пересобирает декорированные ссылки,
 * - отдаёт ссылки фасадам и меню.
 *
 * Начинаем с по одному экземпляру каждого устройства (Light/Music/Thermostat/Camera).
 * Позже можно расширить до нескольких (комнаты/индексы).
 */
public class SmartHomeContext {

    // --- RAW devices (без декораторов) ---
    private final Light light;
    private final MusicSystem music;
    private final Thermostat thermostat;
    private final SecurityCamera camera;

    // --- Feature flags (включение фич для сборки декораторов) ---
    // Light: Voice/Energy
    private boolean lightVoice = false;
    private boolean lightEnergy = false;

    // Music: Remote/Energy
    private boolean musicRemote = false;
    private boolean musicEnergy = false;

    // --- DECORATED devices (пересобираются при изменении флагов) ---
    private Device lightDecorated;
    private Device musicDecorated;

    // --- helpers ---
    private final DecoratorBuilder builder = new DecoratorBuilder();

    public SmartHomeContext(DeviceFactory factory) {
        // создаём сырые устройства через фабрику
        this.light = factory.createLight();
        this.music = factory.createMusic();
        this.thermostat = factory.createThermostat();
        this.camera = factory.createCamera();

        // сборка декорированных ссылок по умолчанию (все фичи OFF)
        rebuild();
    }

    /** Пересобрать декорированные ссылки на основе текущих флагов. */
    public final void rebuild() {
        this.lightDecorated = builder.buildForLight(light, lightVoice, lightEnergy);
        this.musicDecorated = builder.buildForMusic(music, musicRemote, musicEnergy);
    }

    // ---------- Getters для фасадов и меню ----------
    public Light getLightRaw() { return light; }
    public MusicSystem getMusicRaw() { return music; }
    public Thermostat getThermostat() { return thermostat; }
    public SecurityCamera getCamera() { return camera; }

    public Device getLightDecorated() { return lightDecorated; }
    public Device getMusicDecorated() { return musicDecorated; }

    // ---------- Тогглы фич (меняют флаг и пересобирают цепочки) ----------
    public void toggleLightVoice() { this.lightVoice = !this.lightVoice; rebuild(); }
    public void toggleLightEnergy() { this.lightEnergy = !this.lightEnergy; rebuild(); }
    public void toggleMusicRemote() { this.musicRemote = !this.musicRemote; rebuild(); }
    public void toggleMusicEnergy() { this.musicEnergy = !this.musicEnergy; rebuild(); }

    // Можно добавить прямые setters, если нужно управлять из меню отдельно:
    public void setLightVoice(boolean v) { this.lightVoice = v; rebuild(); }
    public void setLightEnergy(boolean v) { this.lightEnergy = v; rebuild(); }
    public void setMusicRemote(boolean v) { this.musicRemote = v; rebuild(); }
    public void setMusicEnergy(boolean v) { this.musicEnergy = v; rebuild(); }

    // Для отображения состояния в UI/консоли
    public boolean isLightVoice() { return lightVoice; }
    public boolean isLightEnergy() { return lightEnergy; }
    public boolean isMusicRemote() { return musicRemote; }
    public boolean isMusicEnergy() { return musicEnergy; }
}
