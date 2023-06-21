package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class EnemyBeastInfoController extends Controller {

    private String beastName;
    private int beastLvl;
    private int beastHp;

    @Inject
    public EnemyBeastInfoController() {
    }

    public void setBeastName(String beastName) {
        this.beastName = beastName;
    }

    public void setBeastLvl(int beastLvl) {
        this.beastLvl = beastLvl;
    }

    public void setBeastHp(int beastHp) {
        this.beastHp = beastHp;
    }
}
