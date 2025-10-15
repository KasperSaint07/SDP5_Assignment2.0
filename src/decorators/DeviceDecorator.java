package decorators;

import devices.Device;


public abstract class DeviceDecorator implements Device {
    protected final Device wrappee;

    public DeviceDecorator(Device wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void operate() {

        wrappee.operate();
    }
}
