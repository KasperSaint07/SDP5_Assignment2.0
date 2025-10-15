package decorators;

import devices.Device;
import devices.Light;
import devices.MusicSystem;

/**
 * Сборщик (Builder/Assembler) для цепочек декораторов.
 * Фиксированный и понятный порядок:
 * - Light:  Voice -> Energy -> Light
 * - Music:  Remote -> Energy -> Music
 */
public class DecoratorBuilder {

    /** Собираем цепочку для света из "сырых" Light + флаги фич. */
    public Device buildForLight(Light raw, boolean voice, boolean energy) {
        Device d = raw;
        if (voice)  d = new VoiceControlDecorator(d);   // Voice сначала
        if (energy) d = new EnergySavingDecorator(d);   // затем Energy
        return d;
    }

    /** Собираем цепочку для музыки из "сырого" MusicSystem + флаги фич. */
    public Device buildForMusic(MusicSystem raw, boolean remote, boolean energy) {
        Device d = raw;
        if (remote) d = new RemoteAccessDecorator(d);   // Music: Remote сначала
        if (energy) d = new EnergySavingDecorator(d);   // затем Energy
        return d;
    }
}
