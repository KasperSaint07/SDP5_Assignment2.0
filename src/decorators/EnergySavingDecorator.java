package decorators;

import devices.Device;
import devices.Light;
import devices.MusicSystem;

/**
 * EnergySavingDecorator (int profile):
 *   0 = OFF         -> ничего не делаем
 *   1 = MILD        -> Light: >50% -> 50% (level 5/10), Music: >5 -> 5
 *   2 = AGGRESSIVE  -> Light: DIM (level 3/10), Music: >3 -> 3
 *
 * Делает лог и применяет ограничения к базовому девайсу, потом делегирует operate().
 */
public class EnergySavingDecorator extends DeviceDecorator {
    private final Device base;   // базовый девайс (развёрнутый)
    private final int profile;   // 0/1/2

    public EnergySavingDecorator(Device wrappee, int profile) {
        super(wrappee);
        this.profile = (profile < 0 || profile > 2) ? 0 : profile;
        this.base = unwrapToBase(wrappee);
    }

    private Device unwrapToBase(Device d) {
        while (d instanceof DeviceDecorator dd) {
            d = dd.wrappee;
        }
        return d;
    }

    private String profileText() {
        return switch (profile) {
            case 1 -> "MILD";
            case 2 -> "AGGRESSIVE";
            default -> "OFF";
        };
    }

    private void optimizePower() {
        System.out.println("Feature: Energy saving ACTIVE (" + profileText() + ")");
        if (profile == 0) return;

        if (base instanceof Light l) {
            int cur = l.getLevel();
            if (profile == 1) { // MILD
                if (cur > 5) l.setLevel(5);
            } else if (profile == 2) { // AGGRESSIVE
                if (cur > 3) l.dim(); // 30%
            }
        } else if (base instanceof MusicSystem m) {
            int vol = m.getVolume();
            if (profile == 1) { // MILD
                if (vol > 5) m.setVolume(5);
            } else if (profile == 2) { // AGGRESSIVE
                if (vol > 3) m.setVolume(3);
            }
        }
    }

    @Override
    public void operate() {
        optimizePower();
        super.operate(); // статус
    }


}
