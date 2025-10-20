package devices;

public class SecurityCamera {
    private boolean enabled = false;

    public void enable() { if (!enabled){ enabled = true;  System.out.println("Camera: ENABLED"); } }
    public void disable(){ if (enabled){  enabled = false; System.out.println("Camera: DISABLED"); } }

    public boolean isEnabled() { return enabled; }
    public String status()     { return enabled ? "Camera: ENABLED" : "Camera: DISABLED"; }
}
