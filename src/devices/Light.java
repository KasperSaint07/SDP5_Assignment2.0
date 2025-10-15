package devices;

// Простой свет: 0 = OFF, 30 = DIM, 100 = BRIGHT
public class Light implements Device {
    private int brightness = 0;

    public void bright() {
        if (brightness != 100) {
            brightness = 100;
            System.out.println("Light: BRIGHT (100%)");
        }
    }

    public void dim() {
        if (brightness != 30) {
            brightness = 30;
            System.out.println("Light: DIM (30%)");
        }
    }

    public void off() {
        if (brightness != 0) {
            brightness = 0;
            System.out.println("Light: OFF");
        }
    }

    public int getBrightness() {
        return brightness;
    }

    @Override
    public void operate() {
        bright();
    }
}
