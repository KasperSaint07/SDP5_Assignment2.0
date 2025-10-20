package decorators;

import devices.Device;
import devices.Light;
import devices.MusicSystem;

/**
 * Сборщик цепочек декораторов с учётом текущего энергопрофиля (int).
 * energyProfile: 0=OFF, 1=MILD, 2=AGGRESSIVE
 */
public class DecoratorBuilder {

    /** Light: Voice -> Remote -> Energy(profile) -> Light */
    public Device buildForLight(Light raw, boolean voice, boolean remote, int energyProfile) {
        Device d = raw;
        if (voice)  d = new VoiceControlDecorator(d);
        if (remote) d = new RemoteAccessDecorator(d);        // просто лог "облако"
        if (energyProfile != 0) d = new EnergySavingDecorator(d, energyProfile);
        return d;
    }

    /** Music: Remote -> Energy(profile) -> Music */
    public Device buildForMusic(MusicSystem raw, boolean remote, int energyProfile) {
        Device d = raw;
        if (remote) d = new RemoteAccessDecorator(d);
        if (energyProfile != 0) d = new EnergySavingDecorator(d, energyProfile);
        return d;
    }
}
