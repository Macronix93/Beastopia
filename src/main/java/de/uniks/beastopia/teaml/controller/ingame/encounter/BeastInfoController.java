package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class BeastInfoController extends Controller {

    private String beastName;
    private String beastType;
    private int beastLvl;
    private int beastHp;
    private int beastXp;

    @Inject
    public BeastInfoController() {
    }


    public void setBeastName(String beastName) {
        this.beastName = beastName;
    }

    public void setBeastType(String beastType) {
        this.beastType = beastType;
    }

    public void setBeastLvl(int beastLvl) {
        this.beastLvl = beastLvl;
    }

    public void setBeastHp(int beastHp) {
        this.beastHp = beastHp;
    }

    public void setBeastXp(int beastXp) {
        this.beastXp = beastXp;
    }
}
