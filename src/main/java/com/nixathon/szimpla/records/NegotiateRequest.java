package com.nixathon.szimpla.records;

import java.util.List;

public class NegotiateRequest {

    public long gameId;
    public int turn;
    public Tower playerTower;
    public List<EnemyTower> enemyTowers;
    public List<CombatAction> combatActions;
}
