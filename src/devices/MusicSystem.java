package devices;

public class MusicSystem implements Device {
    private int volume = 5;      // 0..10
    private boolean playing = false;

    public void setVolume(int v) {
        int newVol = Math.max(0, Math.min(10, v));
        if (volume != newVol) {
            volume = newVol;
            System.out.println("Music: VOLUME " + volume);
        }
    }

    // ТИХО: без вывода
    public void setVolumeQuiet(int v) {
        volume = Math.max(0, Math.min(10, v));
    }

    public int getVolume() { return volume; }

    public void play() {
        if (!playing) {
            playing = true;
            System.out.println("Music: PLAY (vol=" + volume + ")");
        } else {
            System.out.println("Music: PLAY (vol=" + volume + ")");
        }
    }

    // ТИХО: без вывода
    public void playQuiet() { playing = true; }

    public void stop() {
        if (playing) {
            playing = false;
            System.out.println("Music: STOP");
        } else {
            System.out.println("Music: STOP");
        }
    }

    public boolean isPlaying() { return playing; }

    public String status() {
        return playing ? ("Music: PLAY (vol=" + volume + ")") : "Music: STOP";
    }

    @Override
    public void operate() {
        System.out.println(status());
    }
}
