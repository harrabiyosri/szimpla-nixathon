package com.nixathon.szimpla.records;

public class CombatAction {

    public Long playerId;
    public Action action;

    public static class Action {
        public Long targetId;
        public Integer troopCount;
    }
}
