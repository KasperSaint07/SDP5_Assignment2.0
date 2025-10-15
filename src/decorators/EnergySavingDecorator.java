package decorators;

import devices.Device;
import devices.Light;
import devices.MusicSystem;

public class EnergySavingDecorator extends DeviceDecorator {
    public EnergySavingDecorator(Device wrappee) { super(wrappee); }

    private void optimizePower() {
        System.out.println("Feature: Energy saving ACTIVE");

        if (wrappee instanceof Light l) {
            l.dim();               // свет — приглушили
        } else if (wrappee instanceof MusicSystem m) {
            if (m.getVolume() > 3) m.setVolume(3); // музыка — потише
        }

    }

    @Override
    public void operate() {
        optimizePower();
        super.operate();
    }
}
