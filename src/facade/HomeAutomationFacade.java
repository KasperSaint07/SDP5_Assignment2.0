package facade;

public class HomeAutomationFacade {
    private final StartPartyModeFacade party;
    private final NightModeFacade night;
    private final LeaveHomeFacade leave;

    public HomeAutomationFacade(StartPartyModeFacade party,
                                NightModeFacade night,
                                LeaveHomeFacade leave) {
        this.party = party;
        this.night = night;
        this.leave = leave;
    }

    public void startPartyMode()    { party.run(); }
    public void activateNightMode() { night.run(); }
    public void leaveHome()         { leave.run(); }
}
