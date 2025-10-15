package facade;

import devices.Device;
import devices.Light;
import devices.MusicSystem;
//fix
public class StartPartyModeFacade {
    private final Device lightDecorated;
    private final Light lightRaw;
    private final Device musicDecorated;
    private final MusicSystem musicRaw;

    public StartPartyModeFacade(Device lightDecorated, Light lightRaw,
                                Device musicDecorated, MusicSystem musicRaw) {
        this.lightDecorated = lightDecorated;
        this.lightRaw = lightRaw;
        this.musicDecorated = musicDecorated;
        this.musicRaw = musicRaw;
    }

    public void run() {
        System.out.println("=== PARTY MODE ===");
        lightRaw.bright();
        musicRaw.setVolume(8);
        lightDecorated.operate();
        musicDecorated.operate();
    }
}
