package devices;

public class MusicSystem implements Device {
    private int volume = 5;

    public void setVolume(int v) {
        int newVol = Math.max(0, Math.min(10, v));
        if (volume != newVol) {
            volume = newVol;
            System.out.println("Music: VOLUME " + volume);
        }
    }

    public int getVolume() {
        return volume;
    }

    public void play() {
        System.out.println("Music: PLAY (vol=" + volume + ")");
    }

    public void stop() {
        System.out.println("Music: STOP");
    }

    @Override
    public void operate() {
        play();
    }
}
