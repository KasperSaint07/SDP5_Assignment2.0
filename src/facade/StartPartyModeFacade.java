package facade;

import devices.Device;
import devices.Light;
import devices.MusicSystem;
import devices.Thermostat;

public class StartPartyModeFacade {
    private final Device lightDecorated;
    private final Light lightRaw;
    private final Device musicDecorated; // оставим на будущее, но не вызываем здесь
    private final MusicSystem musicRaw;
    private final Thermostat thermostat;

    public StartPartyModeFacade(Device lightDecorated, Light lightRaw,
                                Device musicDecorated, MusicSystem musicRaw,
                                Thermostat thermostat) {
        this.lightDecorated = lightDecorated;
        this.lightRaw = lightRaw;
        this.musicDecorated = musicDecorated;
        this.musicRaw = musicRaw;
        this.thermostat = thermostat;
    }

    public void run() {
        System.out.println("=== PARTY MODE ===");

        // Свет — можно печатать сразу (одна строка)
        lightRaw.bright();

        // Музыка — готовим ТИХО (без промежуточных логов), потом выводим РОВНО ОДНУ строку статуса
        musicRaw.setVolumeQuiet(8);
        musicRaw.playQuiet();
        System.out.println(musicRaw.status()); // ровно одна строка про музыку

        // Термостат — как и раньше
        if (thermostat != null) {
            thermostat.setComfort();
        }

        // ВАЖНО: НЕ вызываем musicDecorated.operate() — иначе опять будет дублирование статуса/фич.
        // Если захочешь показать эффект декораторов — делай это через пункт "Preview (apply features)" в меню.
    }
}
