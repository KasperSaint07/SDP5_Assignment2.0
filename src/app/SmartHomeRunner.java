package app;

import devices.*;
import decorators.*;
import facade.*;

public class SmartHomeRunner {

    private HomeAutomationFacade buildFacade() {

        Light lightRaw = new Light();
        MusicSystem musicRaw = new MusicSystem();
        Thermostat thermostat = new Thermostat();
        SecurityCamera camera = new SecurityCamera();

        Device lightDecorated = new VoiceControlDecorator(lightRaw);
        Device musicDecorated = new RemoteAccessDecorator(musicRaw);

        StartPartyModeFacade party = new StartPartyModeFacade(
                lightDecorated, lightRaw,
                musicDecorated, musicRaw
        );
        NightModeFacade night = new NightModeFacade(lightRaw, thermostat, camera);
        LeaveHomeFacade leave = new LeaveHomeFacade(lightRaw, musicRaw, camera, thermostat);

        return new HomeAutomationFacade(party, night, leave);
    }

    public void runAll() {
        HomeAutomationFacade facade = buildFacade();
        facade.startPartyMode();
        facade.activateNightMode();
        facade.leaveHome();
    }

    public void runPartyOnly() {
        HomeAutomationFacade facade = buildFacade();
        facade.startPartyMode();
    }
}
