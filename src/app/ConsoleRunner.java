package app;

import devices.*;
import facade.*;

import java.util.Locale;
import java.util.Scanner;

/**
 * Консольный UI:
 * 1) Scenes (Facade)  -> Party / Night / Leave
 * 2) Settings         -> Light / MusicSystem / Camera / Thermostat
 * 3) Features         -> Voice Mode / Energy Profile / Remote Control
 * 0) Exit
 */
public class ConsoleRunner {
    private final SmartHomeContext ctx;
    private final Scanner in = new Scanner(System.in);

    public ConsoleRunner() {
        this.ctx = new SmartHomeContext(new DeviceFactory());
    }

    // ======== ENTRY =========
    public void start() {
        while (true) {
            printMainMenu();
            int pick = readInt("Choose: ");
            switch (pick) {
                case 1 -> handleScenes();
                case 2 -> handleSettings();
                case 3 -> handleFeatures();
                case 0 -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== SMART HOME ===");
        System.out.println("1) Scenes (Facade)          -> Party / Night / Leave");
        System.out.println("2) Settings                 -> Light / MusicSystem / Camera / Thermostat");
        System.out.println("3) Features (Decorators)    -> Voice Mode / Energy Profile / Remote Control");
        System.out.println("0) Exit");
    }

    // ======== SCENES ========
    private HomeAutomationFacade buildFacade() {
        StartPartyModeFacade party = new StartPartyModeFacade(
                ctx.getLightDecorated(), ctx.getLightRaw(),
                ctx.getMusicDecorated(), ctx.getMusicRaw(),
                ctx.getThermostat()
        );
        NightModeFacade night = new NightModeFacade(
                ctx.getLightRaw(), ctx.getThermostat(), ctx.getCamera(), ctx.getMusicRaw()
        );
        LeaveHomeFacade leave = new LeaveHomeFacade(
                ctx.getLightRaw(), ctx.getMusicRaw(), ctx.getCamera(), ctx.getThermostat()
        );
        return new HomeAutomationFacade(party, night, leave);
    }

    private void handleScenes() {
        System.out.println("\n=== SCENES ===");
        System.out.println("1) Party");
        System.out.println("2) Night");
        System.out.println("3) Leave Home");
        System.out.println("0) Back");
        int pick = readInt("Choose: ");
        HomeAutomationFacade facade = buildFacade();
        switch (pick) {
            case 1 -> facade.startPartyMode();
            case 2 -> facade.activateNightMode();
            case 3 -> facade.leaveHome();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ======== SETTINGS (главное подменю) ========
    private void handleSettings() {
        System.out.println("\n=== SETTINGS ===");
        System.out.println("1) Light");
        System.out.println("2) MusicSystem");
        System.out.println("3) Camera");
        System.out.println("4) Thermostat");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        switch (p) {
            case 1 -> settingsLight();
            case 2 -> settingsMusic();
            case 3 -> settingsCamera();
            case 4 -> settingsThermostat();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ----- Settings: Light -----
    private void settingsLight() {
        System.out.println("\n[LIGHT SETTINGS]");
        System.out.println("1) Current condition");
        System.out.println("2) Choose power (level 0..10)");
        System.out.println("3) Bright (100%)");
        System.out.println("4) Dim (30%)");
        System.out.println("5) OFF");
        System.out.println("6) Preview (apply features)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        Light l = ctx.getLightRaw();
        switch (p) {
            case 1 -> System.out.println(l.status());
            case 2 -> l.setLevel(readInt("Level 0..10 = "));
            case 3 -> l.bright();
            case 4 -> l.dim();
            case 5 -> l.off();
            case 6 -> ctx.getLightDecorated().operate(); // применить фичи и показать статус
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ----- Settings: Music -----
    private void settingsMusic() {
        System.out.println("\n[MUSIC SETTINGS]");
        System.out.println("1) Current condition");
        System.out.println("2) Choose power (volume 0..10)");
        System.out.println("3) OFF (stop)");
        System.out.println("4) Preview (apply features)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        MusicSystem m = ctx.getMusicRaw();
        switch (p) {
            case 1 -> System.out.println(m.status());
            case 2 -> m.setVolume(readInt("Volume 0..10 = "));
            case 3 -> m.stop();
            case 4 -> ctx.getMusicDecorated().operate();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ----- Settings: Camera -----
    private void settingsCamera() {
        System.out.println("\n[CAMERA SETTINGS]");
        System.out.println("1) Current condition");
        System.out.println("2) On");
        System.out.println("3) Off");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        SecurityCamera c = ctx.getCamera();
        switch (p) {
            case 1 -> System.out.println(c.status());
            case 2 -> c.enable();
            case 3 -> c.disable();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ----- Settings: Thermostat -----
    private void settingsThermostat() {
        System.out.println("\n[THERMOSTAT SETTINGS]");
        System.out.println("1) Current condition");
        System.out.println("2) Choose temperature (16..30)");
        System.out.println("3) Choose humidity (30..70)");
        System.out.println("4) OFF");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        Thermostat t = ctx.getThermostat();
        switch (p) {
            case 1 -> System.out.println(t.status());
            case 2 -> t.setTemperature(readInt("Temp 16..30 = "));
            case 3 -> t.setHumidity(readInt("Humidity 30..70 = "));
            case 4 -> t.setOff();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ======== FEATURES (Decorators) ========
    private void handleFeatures() {
        while (true) {
            System.out.println("\n=== FEATURES ===");
            System.out.println("1) Voice Mode");
            System.out.println("2) Energy Profile (current: "
                    + SmartHomeContext.energyProfileToText(ctx.getEnergyProfile()) + ")");
            System.out.println("3) Remote Control (cloud="
                    + (ctx.isCloudConnected() ? "CONNECTED" : "DISCONNECTED")
                    + ", LightRemote=" + (ctx.isLightRemote() ? "ON" : "OFF")
                    + ", MusicRemote=" + (ctx.isMusicRemote() ? "ON" : "OFF") + ")");
            System.out.println("0) Back");
            int p = readInt("Choose: ");
            switch (p) {
                case 1 -> runVoiceMode();
                case 2 -> energyProfileMenu();
                case 3 -> remoteControlMenu();
                case 0 -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    // ======== VOICE MODE ========
    private void runVoiceMode() {
        System.out.println("\n=== VOICE MODE === (type words; 'help' for commands, 'exit' to quit)");
        printVoiceHelp();
        while (true) {
            System.out.print("> ");
            String line = in.nextLine();
            if (line == null) continue;
            String cmd = line.trim();
            if (cmd.isEmpty()) continue;
            String lc = cmd.toLowerCase(Locale.ROOT);

            if (lc.equals("exit")) break;
            if (lc.equals("help")) { printVoiceHelp(); continue; }

            if (equalsAny(lc, "scenes", "scene")) { voiceScenes(); continue; }
            if (equalsAny(lc, "devices", "device")) { voiceDevices(); continue; }
            if (equalsAny(lc, "features", "feature")) { voiceFeatures(); continue; }
            if (equalsAny(lc, "settings", "setting")) { voiceSettings(); continue; }

            System.out.println("Unknown command. Type 'help'.");
        }
    }

    private void printVoiceHelp() {
        System.out.println("Type one of: scenes | devices | features | settings | help | exit");
    }

    private boolean equalsAny(String s, String... opts) {
        for (String o : opts) if (s.equals(o)) return true;
        return false;
    }

    private void voiceScenes() {
        System.out.println("[voice] scenes: party | night | leave");
        HomeAutomationFacade facade = buildFacade();
        while (true) {
            System.out.print("scenes> ");
            String s = in.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.equals("back") || s.equals("exit")) break;
            switch (s) {
                case "party" -> facade.startPartyMode();
                case "night" -> facade.activateNightMode();
                case "leave" -> facade.leaveHome();
                case "help"  -> System.out.println("party | night | leave | back");
                default -> System.out.println("Unknown. Type 'help' or 'back'.");
            }
        }
    }

    private void voiceDevices() {
        System.out.println("[voice] devices: light | music | camera | thermostat (type 'back' to return)");
        while (true) {
            System.out.print("devices> ");
            String dev = in.nextLine().trim().toLowerCase(Locale.ROOT);
            if (dev.equals("back") || dev.equals("exit")) break;
            switch (dev) {
                case "light" -> voiceLight();
                case "music" -> voiceMusic();
                case "camera" -> voiceCamera();
                case "thermostat" -> voiceThermostat();
                case "help" -> System.out.println("light | music | camera | thermostat | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceLight() {
        System.out.println("light: bright | dim | off | operate | level <0..10> | back");
        while (true) {
            System.out.print("light> ");
            String s = in.nextLine().trim();
            String lc = s.toLowerCase(Locale.ROOT);
            if (lc.equals("back") || lc.equals("exit")) break;

            if (lc.startsWith("level")) {
                String[] p = s.split("\\s+");
                if (p.length >= 2) {
                    try { ctx.getLightRaw().setLevel(Integer.parseInt(p[1])); }
                    catch (NumberFormatException e) { System.out.println("Invalid number."); }
                } else System.out.println("Usage: level 7");
                continue;
            }
            switch (lc) {
                case "bright" -> ctx.getLightRaw().bright();
                case "dim"    -> ctx.getLightRaw().dim();
                case "off"    -> ctx.getLightRaw().off();
                case "operate"-> ctx.getLightDecorated().operate();
                case "help"   -> System.out.println("bright | dim | off | operate | level N | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceMusic() {
        System.out.println("music: play | stop | volume <0..10> | operate | back");
        while (true) {
            System.out.print("music> ");
            String s = in.nextLine().trim();
            String lc = s.toLowerCase(Locale.ROOT);
            if (lc.equals("back") || lc.equals("exit")) break;

            if (lc.startsWith("volume")) {
                String[] parts = s.split("\\s+");
                if (parts.length >= 2) {
                    try { ctx.getMusicRaw().setVolume(Integer.parseInt(parts[1])); }
                    catch (NumberFormatException e) { System.out.println("Invalid number."); }
                } else System.out.println("Usage: volume 7");
                continue;
            }
            switch (lc) {
                case "play"    -> ctx.getMusicRaw().play();
                case "stop"    -> ctx.getMusicRaw().stop();
                case "operate" -> ctx.getMusicDecorated().operate();
                case "help"    -> System.out.println("play | stop | volume N | operate | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceCamera() {
        System.out.println("camera: enable | disable | back");
        while (true) {
            System.out.print("camera> ");
            String s = in.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.equals("back") || s.equals("exit")) break;
            switch (s) {
                case "enable" -> ctx.getCamera().enable();
                case "disable"-> ctx.getCamera().disable();
                case "help"   -> System.out.println("enable | disable | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceThermostat() {
        System.out.println("thermostat: comfort | eco | night | away | off | temp <16..30> | humidity <30..70> | back");
        while (true) {
            System.out.print("thermostat> ");
            String s = in.nextLine().trim();
            String lc = s.toLowerCase(Locale.ROOT);
            if (lc.equals("back") || lc.equals("exit")) break;

            if (lc.startsWith("temp")) {
                String[] p = s.split("\\s+");
                if (p.length >= 2) {
                    try { ctx.getThermostat().setTemperature(Integer.parseInt(p[1])); }
                    catch (NumberFormatException e) { System.out.println("Invalid number."); }
                } else System.out.println("Usage: temp 21");
                continue;
            }
            if (lc.startsWith("humidity")) {
                String[] p = s.split("\\s+");
                if (p.length >= 2) {
                    try { ctx.getThermostat().setHumidity(Integer.parseInt(p[1])); }
                    catch (NumberFormatException e) { System.out.println("Invalid number."); }
                } else System.out.println("Usage: humidity 45");
                continue;
            }

            switch (lc) {
                case "comfort" -> ctx.getThermostat().setComfort();
                case "eco"     -> ctx.getThermostat().setEco();
                case "night"   -> ctx.getThermostat().setNight();
                case "away"    -> ctx.getThermostat().setAway();
                case "off"     -> ctx.getThermostat().setOff();
                case "help"    -> System.out.println("comfort | eco | night | away | off | temp N | humidity N | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    // ======== VOICE: FEATURES (профиль энергии + тумблеры) ========
    private void voiceFeatures() {
        System.out.println("[voice] features mode. Type:");
        System.out.println("  status");
        System.out.println("  energy off | energy mild | energy aggressive");
        System.out.println("  toggle light remote | toggle music remote | toggle light voice");
        System.out.println("  back");
        while (true) {
            System.out.print("features> ");
            String s = in.nextLine().trim().toLowerCase(java.util.Locale.ROOT);
            switch (s) {
                case "status" -> {
                    System.out.println("Energy: " + SmartHomeContext.energyProfileToText(ctx.getEnergyProfile()));
                    System.out.println("Light:  Voice=" + (ctx.isLightVoice() ? "ON" : "OFF")
                            + ", Remote=" + (ctx.isLightRemote() ? "ON" : "OFF"));
                    System.out.println("Music:  Remote=" + (ctx.isMusicRemote() ? "ON" : "OFF"));
                }
                case "energy off"        -> ctx.setEnergyProfile(0);
                case "energy mild"       -> ctx.setEnergyProfile(1);
                case "energy aggressive" -> ctx.setEnergyProfile(2);
                case "toggle light remote"  -> { ctx.toggleLightRemote();  System.out.println("Light Remote  -> " + (ctx.isLightRemote() ? "ON" : "OFF")); }
                case "toggle music remote"  -> { ctx.toggleMusicRemote();  System.out.println("Music Remote  -> " + (ctx.isMusicRemote() ? "ON" : "OFF")); }
                case "toggle light voice"   -> { ctx.toggleLightVoice();   System.out.println("Light Voice   -> " + (ctx.isLightVoice() ? "ON" : "OFF")); }
                case "help" -> System.out.println("status | energy off|mild|aggressive | toggle light remote | toggle music remote | toggle light voice | back");
                case "back", "exit" -> { return; }
                default -> System.out.println("Unknown. Type 'help'.");
            }
        }
    }

    // ======== VOICE: SETTINGS (шорткаты словами) ========
    private void voiceSettings() {
        System.out.println("[voice] settings mode. Examples:");
        System.out.println("  light level 7 | light bright | light dim | light off");
        System.out.println("  music play | music stop | music volume 7");
        System.out.println("  thermostat comfort|eco|night|away|off | thermostat temp 21 | thermostat humidity 45");
        System.out.println("  camera enable|disable");
        System.out.println("  back");
        while (true) {
            System.out.print("settings> ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;
            String lc = line.toLowerCase(java.util.Locale.ROOT);

            if (lc.equals("back") || lc.equals("exit")) return;
            if (lc.equals("help")) {
                System.out.println("light level N|bright|dim|off | music play|stop|volume N | thermostat comfort|eco|night|away|off|temp N|humidity N | camera enable|disable | back");
                continue;
            }

            if (lc.startsWith("light")) {
                String[] p = line.split("\\s+");
                if (lc.equals("light bright")) { ctx.getLightRaw().bright(); continue; }
                if (lc.equals("light dim"))    { ctx.getLightRaw().dim();    continue; }
                if (lc.equals("light off"))    { ctx.getLightRaw().off();    continue; }
                if (p.length == 3 && p[1].equalsIgnoreCase("level")) {
                    try { ctx.getLightRaw().setLevel(Integer.parseInt(p[2])); }
                    catch (NumberFormatException e) { System.out.println("Invalid number."); }
                    continue;
                }
                System.out.println("Unknown light command.");
                continue;
            }

            if (lc.startsWith("music")) {
                if (lc.equals("music play"))  { ctx.getMusicRaw().play();  continue; }
                if (lc.equals("music stop"))  { ctx.getMusicRaw().stop();  continue; }
                if (lc.startsWith("music volume")) {
                    String[] p = line.split("\\s+");
                    if (p.length >= 3) {
                        try { ctx.getMusicRaw().setVolume(Integer.parseInt(p[2])); }
                        catch (NumberFormatException e) { System.out.println("Invalid number."); }
                    } else System.out.println("Usage: music volume 7");
                    continue;
                }
                System.out.println("Unknown music command.");
                continue;
            }

            if (lc.startsWith("thermostat")) {
                if (lc.equals("thermostat comfort")) { ctx.getThermostat().setComfort(); continue; }
                if (lc.equals("thermostat eco"))     { ctx.getThermostat().setEco();     continue; }
                if (lc.equals("thermostat night"))   { ctx.getThermostat().setNight();   continue; }
                if (lc.equals("thermostat away"))    { ctx.getThermostat().setAway();    continue; }
                if (lc.equals("thermostat off"))     { ctx.getThermostat().setOff();     continue; }
                if (lc.startsWith("thermostat temp")) {
                    String[] p = line.split("\\s+");
                    if (p.length >= 3) {
                        try { ctx.getThermostat().setTemperature(Integer.parseInt(p[2])); }
                        catch (NumberFormatException e) { System.out.println("Invalid number."); }
                    } else System.out.println("Usage: thermostat temp 21");
                    continue;
                }
                if (lc.startsWith("thermostat humidity")) {
                    String[] p = line.split("\\s+");
                    if (p.length >= 3) {
                        try { ctx.getThermostat().setHumidity(Integer.parseInt(p[2])); }
                        catch (NumberFormatException e) { System.out.println("Invalid number."); }
                    } else System.out.println("Usage: thermostat humidity 45");
                    continue;
                }
                System.out.println("Unknown thermostat command.");
                continue;
            }

            if (lc.startsWith("camera")) {
                if (lc.equals("camera enable")) { ctx.getCamera().enable();  continue; }
                if (lc.equals("camera disable")){ ctx.getCamera().disable(); continue; }
                System.out.println("Unknown camera command.");
                continue;
            }

            System.out.println("Unknown command. Type 'help'.");
        }
    }

    // ======== UTIL ========
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            try { return Integer.parseInt(s.trim()); }
            catch (Exception e) { System.out.println("Invalid number."); }
        }
    }

    // ----- Energy Profile submenu -----
    private void energyProfileMenu() {
        System.out.println("\n[ENERGY PROFILE]");
        System.out.println("Current: " + SmartHomeContext.energyProfileToText(ctx.getEnergyProfile()));
        System.out.println("1) OFF        (no saving)");
        System.out.println("2) MILD       (Light<=50%, Music<=5)");
        System.out.println("3) AGGRESSIVE (Light=Dim, Music<=3)");
        System.out.println("4) Preview energy impact now");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        switch (p) {
            case 1 -> ctx.setEnergyProfile(0);
            case 2 -> ctx.setEnergyProfile(1);
            case 3 -> ctx.setEnergyProfile(2);
            case 4 -> { ctx.getLightDecorated().operate(); ctx.getMusicDecorated().operate(); }
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private void remoteControlMenu() {
        System.out.println("\n[REMOTE CONTROL]");
        System.out.println("Cloud: " + (ctx.isCloudConnected() ? "CONNECTED" : "DISCONNECTED"));
        System.out.println("Light Remote: " + (ctx.isLightRemote() ? "ON" : "OFF"));
        System.out.println("Music Remote: " + (ctx.isMusicRemote() ? "ON" : "OFF"));
        System.out.println("1) Toggle cloud connect/disconnect");
        System.out.println("2) Toggle Light Remote");
        System.out.println("3) Toggle Music Remote");
        System.out.println("4) Quick preview (apply features on both)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        switch (p) {
            case 1 -> {
                boolean now = !ctx.isCloudConnected();
                ctx.setCloudConnected(now);
                System.out.println("Cloud -> " + (now ? "CONNECTED" : "DISCONNECTED"));
            }
            case 2 -> {
                ctx.toggleLightRemote();
                System.out.println("Light Remote -> " + (ctx.isLightRemote() ? "ON" : "OFF"));
            }
            case 3 -> {
                ctx.toggleMusicRemote();
                System.out.println("Music Remote -> " + (ctx.isMusicRemote() ? "ON" : "OFF"));
            }
            case 4 -> { ctx.getLightDecorated().operate(); ctx.getMusicDecorated().operate(); }
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }
}
