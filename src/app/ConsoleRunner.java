package app;

import devices.*;
import facade.*;
import java.util.Locale;
import java.util.Scanner;

/**вф
 * Консольный UI:
 *  - цифровое главное меню
 *  - voice mode: те же пункты, но ввод словами
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
                case 2 -> handleDevices();
                case 3 -> handleFeatures();
                case 4 -> handleSettings();
                case 5 -> runVoiceMode();
                case 0 -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== SMART HOME ===");
        System.out.println("1) Scenes (Facade)          -> Party / Night / Leave");
        System.out.println("2) Devices control          -> Light / Music / Camera / Thermostat");
        System.out.println("3) Features (Decorators)    -> Toggle Voice/Energy/Remote per device");
        System.out.println("4) Settings                 -> Light: dim/bright/off; Music: volume; Thermostat: modes");
        System.out.println("5) Voice Control mode       -> same menu, but choose by typing words");
        System.out.println("0) Exit");
    }

    // ======== SCENES ========
    private HomeAutomationFacade buildFacade() {
        StartPartyModeFacade party = new StartPartyModeFacade(
                ctx.getLightDecorated(), ctx.getLightRaw(),
                ctx.getMusicDecorated(), ctx.getMusicRaw()
        );
        NightModeFacade night = new NightModeFacade(ctx.getLightRaw(), ctx.getThermostat(), ctx.getCamera());
        LeaveHomeFacade leave = new LeaveHomeFacade(ctx.getLightRaw(), ctx.getMusicRaw(), ctx.getCamera(), ctx.getThermostat());
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

    // ======== DEVICES CONTROL ========
    private void handleDevices() {
        System.out.println("\n=== DEVICES ===");
        System.out.println("1) Light");
        System.out.println("2) Music");
        System.out.println("3) Camera");
        System.out.println("4) Thermostat");
        System.out.println("0) Back");
        int pick = readInt("Choose: ");
        switch (pick) {
            case 1 -> deviceLight();
            case 2 -> deviceMusic();
            case 3 -> deviceCamera();
            case 4 -> deviceThermostat();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private void deviceLight() {
        System.out.println("\n[LIGHT]");
        System.out.println("1) Bright");
        System.out.println("2) Dim");
        System.out.println("3) Off");
        System.out.println("4) Operate (with decorators)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        Light l = ctx.getLightRaw();
        switch (p) {
            case 1 -> l.bright();
            case 2 -> l.dim();
            case 3 -> l.off();
            case 4 -> ctx.getLightDecorated().operate();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private void deviceMusic() {
        System.out.println("\n[MUSIC]");
        System.out.println("1) Play");
        System.out.println("2) Stop");
        System.out.println("3) Set volume (1..10)");
        System.out.println("4) Operate (with decorators)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        MusicSystem m = ctx.getMusicRaw();
        switch (p) {
            case 1 -> m.play();
            case 2 -> m.stop();
            case 3 -> {
                int v = readInt("Volume = ");
                m.setVolume(v);
            }
            case 4 -> ctx.getMusicDecorated().operate();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private void deviceCamera() {
        System.out.println("\n[CAMERA]");
        System.out.println("1) Enable");
        System.out.println("2) Disable");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        SecurityCamera c = ctx.getCamera();
        switch (p) {
            case 1 -> c.enable();
            case 2 -> c.disable();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private void deviceThermostat() {
        System.out.println("\n[THERMOSTAT]");
        System.out.println("1) Comfort");
        System.out.println("2) Eco");
        System.out.println("3) Night");
        System.out.println("4) Away");
        System.out.println("5) Set temperature (15..28)");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        Thermostat t = ctx.getThermostat();
        switch (p) {
            case 1 -> t.setComfort();
            case 2 -> t.setEco();
            case 3 -> t.setNight();
            case 4 -> t.setAway();
            case 5 -> {
                int c = readInt("Target °C = ");
                // прямого API нет — покажем как меняется режимом ближе всего
                if (c >= 15 && c <= 28) {
                    // простая имитация: подбираем ближайший режим
                    if (c >= 21) t.setComfort();
                    else if (c >= 19) t.setEco();
                    else if (c >= 18) t.setNight();
                    else t.setAway();
                    System.out.println("(Note) Direct set not supported; mapped to closest mode.");
                } else {
                    System.out.println("Invalid range (15..28).");
                }
            }
            case 0 -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    // ======== FEATURES (DECORATORS) ========
    private void handleFeatures() {
        while (true) {
            System.out.println("\n=== FEATURES (Decorators) ===");
            System.out.println("Light:  Voice=" + onOff(ctx.isLightVoice()) + ", Energy=" + onOff(ctx.isLightEnergy()));
            System.out.println("Music:  Remote=" + onOff(ctx.isMusicRemote()) + ", Energy=" + onOff(ctx.isMusicEnergy()));
            System.out.println("1) Toggle Light Voice");
            System.out.println("2) Toggle Light Energy");
            System.out.println("3) Toggle Music Remote");
            System.out.println("4) Toggle Music Energy");
            System.out.println("0) Back");
            int p = readInt("Choose: ");
            switch (p) {
                case 1 -> ctx.toggleLightVoice();
                case 2 -> ctx.toggleLightEnergy();
                case 3 -> ctx.toggleMusicRemote();
                case 4 -> ctx.toggleMusicEnergy();
                case 0 -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private String onOff(boolean b) { return b ? "ON" : "OFF"; }

    // ======== SETTINGS (shortcuts) ========
    private void handleSettings() {
        System.out.println("\n=== SETTINGS ===");
        System.out.println("1) Light -> Bright");
        System.out.println("2) Light -> Dim");
        System.out.println("3) Light -> Off");
        System.out.println("4) Music -> Volume (1..10)");
        System.out.println("5) Thermostat -> Night");
        System.out.println("0) Back");
        int p = readInt("Choose: ");
        switch (p) {
            case 1 -> ctx.getLightRaw().bright();
            case 2 -> ctx.getLightRaw().dim();
            case 3 -> ctx.getLightRaw().off();
            case 4 -> {
                int v = readInt("Volume = ");
                ctx.getMusicRaw().setVolume(v);
            }
            case 5 -> ctx.getThermostat().setNight();
            case 0 -> {}
            default -> System.out.println("Unknown option.");
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

            // главные разделы (регистронезависимо + допускаем варианты)
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
        System.out.println("light: bright | dim | off | operate | back");
        while (true) {
            System.out.print("light> ");
            String s = in.nextLine().trim().toLowerCase(Locale.ROOT);
            if (s.equals("back") || s.equals("exit")) break;
            switch (s) {
                case "bright" -> ctx.getLightRaw().bright();
                case "dim" -> ctx.getLightRaw().dim();
                case "off" -> ctx.getLightRaw().off();
                case "operate" -> ctx.getLightDecorated().operate();
                case "help" -> System.out.println("bright | dim | off | operate | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceMusic() {
        System.out.println("music: play | stop | volume <1..10> | operate | back");
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
                case "play" -> ctx.getMusicRaw().play();
                case "stop" -> ctx.getMusicRaw().stop();
                case "operate" -> ctx.getMusicDecorated().operate();
                case "help" -> System.out.println("play | stop | volume <1..10> | operate | back");
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
                case "disable" -> ctx.getCamera().disable();
                case "help" -> System.out.println("enable | disable | back");
                default -> System.out.println("Unknown.");
            }
        }
    }

    private void voiceThermostat() {
        System.out.println("thermostat: comfort | eco | night | away | temp <15..28> | back");
        while (true) {
            System.out.print("thermostat> ");
            String s = in.nextLine().trim();
            String lc = s.toLowerCase(Locale.ROOT);
            if (lc.equals("back") || lc.equals("exit")) break;
            if (lc.startsWith("temp")) {
                String[] parts = s.split("\\s+");
                if (parts.length >= 2) {
                    try {
                        int c = Integer.parseInt(parts[1]);
                        if (c >= 15 && c <= 28) {
                            if (c >= 21) ctx.getThermostat().setComfort();
                            else if (c >= 19) ctx.getThermostat().setEco();
                            else if (c >= 18) ctx.getThermostat().setNight();
                            else ctx.getThermostat().setAway();
                            System.out.println("(Note) Direct set not supported; mapped to closest mode.");
                        } else System.out.println("Invalid range (15..28).");
                    } catch (NumberFormatException e) { System.out.println("Invalid number."); }
                } else System.out.println("Usage: temp 21");
                continue;
            }
            switch (lc) {
                case "comfort" -> ctx.getThermostat().setComfort();
                case "eco" -> ctx.getThermostat().setEco();
                case "night" -> ctx.getThermostat().setNight();
                case "away" -> ctx.getThermostat().setAway();
                case "help" -> System.out.println("comfort | eco | night | away | temp <15..28> | back");
                default -> System.out.println("Unknown.");
            }
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
    // ======== VOICE: FEATURES ========
    // ======== VOICE: FEATURES ========
    // ======== VOICE: FEATURES ========
    private void voiceFeatures() {
        System.out.println("[voice] features mode. Type:");
        System.out.println("  status");
        System.out.println("  toggle light voice");
        System.out.println("  toggle light energy");
        System.out.println("  toggle music remote");
        System.out.println("  toggle music energy");
        System.out.println("  back");
        while (true) {
            System.out.print("features> ");
            String s = in.nextLine().trim().toLowerCase(java.util.Locale.ROOT);
            switch (s) {
                case "status" -> {
                    System.out.println("Light:  Voice=" + onOff(ctx.isLightVoice()) + ", Energy=" + onOff(ctx.isLightEnergy()));
                    System.out.println("Music:  Remote=" + onOff(ctx.isMusicRemote()) + ", Energy=" + onOff(ctx.isMusicEnergy()));
                }
                case "toggle light voice"  -> ctx.toggleLightVoice();
                case "toggle light energy" -> ctx.toggleLightEnergy();
                case "toggle music remote" -> ctx.toggleMusicRemote();
                case "toggle music energy" -> ctx.toggleMusicEnergy();
                case "help" -> System.out.println("status | toggle light voice | toggle light energy | toggle music remote | toggle music energy | back");
                case "back", "exit" -> { return; }
                default -> System.out.println("Unknown. Type 'help'.");
            }
        }
    }

    // ======== VOICE: SETTINGS ========
    private void voiceSettings() {
        System.out.println("[voice] settings mode. Examples:");
        System.out.println("  light bright | light dim | light off");
        System.out.println("  music play | music stop | music volume 7");
        System.out.println("  thermostat comfort|eco|night|away | thermostat temp 21");
        System.out.println("  back");
        while (true) {
            System.out.print("settings> ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;
            String lc = line.toLowerCase(java.util.Locale.ROOT);

            if (lc.equals("back") || lc.equals("exit")) return;
            if (lc.equals("help")) {
                System.out.println("light bright|dim|off | music play|stop|volume N | thermostat comfort|eco|night|away|temp N | back");
                continue;
            }

            if (lc.startsWith("light")) {
                switch (lc) {
                    case "light bright" -> ctx.getLightRaw().bright();
                    case "light dim"    -> ctx.getLightRaw().dim();
                    case "light off"    -> ctx.getLightRaw().off();
                    default -> System.out.println("Unknown light command.");
                }
                continue;
            }

            if (lc.startsWith("music")) {
                if (lc.equals("music play")) { ctx.getMusicRaw().play(); continue; }
                if (lc.equals("music stop")) { ctx.getMusicRaw().stop(); continue; }
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
                if (lc.equals("thermostat eco"))     { ctx.getThermostat().setEco(); continue; }
                if (lc.equals("thermostat night"))   { ctx.getThermostat().setNight(); continue; }
                if (lc.equals("thermostat away"))    { ctx.getThermostat().setAway(); continue; }
                if (lc.startsWith("thermostat temp")) {
                    String[] p = line.split("\\s+");
                    if (p.length >= 3) {
                        try {
                            int c = Integer.parseInt(p[2]);
                            if (c >= 15 && c <= 28) {
                                if (c >= 21) ctx.getThermostat().setComfort();
                                else if (c >= 19) ctx.getThermostat().setEco();
                                else if (c >= 18) ctx.getThermostat().setNight();
                                else ctx.getThermostat().setAway();
                                System.out.println("(Note) Direct set not supported; mapped to closest mode.");
                            } else System.out.println("Invalid range (15..28).");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number.");
                        }
                    } else {
                        System.out.println("Usage: thermostat temp 21");
                    }
                    continue;
                }
                System.out.println("Unknown thermostat command.");
                continue;
            }

            System.out.println("Unknown command. Type 'help'.");
        }
    }



}
