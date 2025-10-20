package devices;

public class Light implements Device {
    // 0..10 (OFF..BRIGHT), где 10 = 100%, 3 ~ 30%
    private int level = 0;

    public void setLevel(int n) {
        int v = Math.max(0, Math.min(10, n));
        if (level != v) {
            level = v;
            printState();
        }
    }

    public void bright() { setLevel(10); } // 100%
    public void dim()    { setLevel(3); }  // 30%
    public void off()    { setLevel(0); }

    public int getLevel() { return level; }

    public String status() {
        if (level == 10) return "Light: BRIGHT (100%)";
        if (level == 3)  return "Light: DIM (30%)";
        if (level == 0)  return "Light: OFF";
        return "Light: LEVEL " + (level * 10) + "%";
    }

    private void printState() { System.out.println(status()); }

    @Override
    public void operate() {
        // Не меняем состояние — просто показываем текущее:
        printState();
    }
}
