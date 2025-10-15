package decorators;

import devices.Device;

public class RemoteAccessDecorator extends DeviceDecorator {
    private boolean cloudConnected = false;

    public RemoteAccessDecorator(Device wrappee) {
        super(wrappee);
    }

    private void connectCloud() {
        if (!cloudConnected) {
            cloudConnected = true;
            System.out.println("Feature: Remote access via CLOUD");
        }
    }

    @Override
    public void operate() {
        connectCloud();  // подключаем "облако"
        super.operate(); // делегируем дальше по цепочке
    }
}
