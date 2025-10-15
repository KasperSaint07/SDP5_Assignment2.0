package decorators;

import devices.Device;

public class VoiceControlDecorator extends DeviceDecorator {
    private boolean voiceEnabled = false;

    public VoiceControlDecorator(Device wrappee) {
        super(wrappee);
    }

    private void enableVoice() {
        if (!voiceEnabled) {
            voiceEnabled = true;
            System.out.println("Feature: Voice control ENABLED");
        }
    }

    @Override
    public void operate() {
        enableVoice();   // включаем канал управления
        super.operate(); // делегируем дальше по цепочке
    }
}
