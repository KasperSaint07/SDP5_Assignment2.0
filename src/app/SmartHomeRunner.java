package app;

import devices.*;
import facade.*;

/**
 * Быстрый сценарный запуск без меню (для демонстрации сцен подряд).
 * Если используешь Demo->ConsoleRunner, этот класс можно не трогать.
 */
public class SmartHomeRunner {

    public static void run() {
        SmartHomeContext ctx = new SmartHomeContext(new DeviceFactory());

        // ВАЖНО: новые сигнатуры фасадов
        StartPartyModeFacade party = new StartPartyModeFacade(
                ctx.getLightDecorated(),   // Device (decorated light)
                ctx.getLightRaw(),         // Light   (raw light)
                ctx.getMusicDecorated(),   // Device (decorated music)
                ctx.getMusicRaw(),         // MusicSystem (raw)
                ctx.getThermostat()        // Thermostat  <<< добавили
        );

        NightModeFacade night = new NightModeFacade(
                ctx.getLightRaw(),         // Light
                ctx.getThermostat(),       // Thermostat
                ctx.getCamera(),           // SecurityCamera
                ctx.getMusicRaw()          // MusicSystem  <<< добавили
        );

        LeaveHomeFacade leave = new LeaveHomeFacade(
                ctx.getLightRaw(),         // Light
                ctx.getMusicRaw(),         // MusicSystem
                ctx.getCamera(),           // SecurityCamera
                ctx.getThermostat()        // Thermostat
        );

        HomeAutomationFacade facade = new HomeAutomationFacade(party, night, leave);

        // Прогон сцен (пример)
        facade.startPartyMode();
        facade.activateNightMode();
        facade.leaveHome();
    }
}
