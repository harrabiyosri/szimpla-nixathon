package com.nixathon.szimpla.records;

// For response to /combat
public class CombatResponseAction {
    public String type;       // "armor", "attack", "upgrade"
    public Integer amount;    // for armor
    public Long targetId;     // for attack
    public Integer troopCount; // for attack

    // Convenience constructors
    public static CombatResponseAction armor(int amount) {
        CombatResponseAction a = new CombatResponseAction();
        a.type = "armor";
        a.amount = amount;
        return a;
    }

    public static CombatResponseAction attack(long targetId, int troopCount) {
        CombatResponseAction a = new CombatResponseAction();
        a.type = "attack";
        a.targetId = targetId;
        a.troopCount = troopCount;
        return a;
    }

    public static CombatResponseAction upgrade() {
        CombatResponseAction a = new CombatResponseAction();
        a.type = "upgrade";
        return a;
    }
}
