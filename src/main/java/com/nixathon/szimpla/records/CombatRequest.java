package com.nixathon.szimpla.records;

import java.util.List;

public class CombatRequest {

    public long gameId;
    public int turn;
    public Tower playerTower;
    public List<EnemyTower> enemyTowers;
    public List<CombatAction> diplomacy;
    public List<CombatAction> previousAttacks;
}
