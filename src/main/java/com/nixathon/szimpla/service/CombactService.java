package com.nixathon.szimpla.service;

import com.nixathon.szimpla.records.CombatAction;
import com.nixathon.szimpla.records.CombatRequest;
import com.nixathon.szimpla.records.CombatResponseAction;
import com.nixathon.szimpla.records.EnemyTower;
import com.nixathon.szimpla.records.Tower;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CombactService {

    /**
     * Decide combat actions for this turn
     */
    public List<CombatResponseAction> decideCombat(
            CombatRequest request
    ) {
        List<CombatResponseAction> actions = new ArrayList<>();
        int resources = request.playerTower.resources;

// 1️⃣ Estimate incoming damage
        int expectedIncoming = 0;
        if (request.previousAttacks != null) {
            for (CombatAction attack : request.previousAttacks) {
                if (attack.action != null && attack.action.targetId.equals(request.playerTower.playerId)) {
                    expectedIncoming += attack.action.troopCount;
                }
            }
        }

// 2️⃣ Armor if necessary (spend min(resources, expected damage))
        if (expectedIncoming > 0 && resources > 0) {
            int armorAmount = Math.min(expectedIncoming, resources);
            actions.add(CombatResponseAction.armor(armorAmount));
            resources -= armorAmount;
        }

// 3️⃣ Compute threat score for enemies
        Map<EnemyTower, Integer> threatScores = new HashMap<>();
        for (EnemyTower enemy : request.enemyTowers) {
            int aggression = countAggression(enemy.playerId, request.previousAttacks);
            int score = computeThreatScore(enemy, aggression);
            threatScores.put(enemy, score);
        }

// Sort enemies by threat descending
        List<EnemyTower> sortedEnemies = threatScores.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .map(Map.Entry::getKey)
                .toList();

// 4️⃣ Attack highest threat enemies until resources run out
        for (EnemyTower enemy : sortedEnemies) {
            if (resources <= 0) break;

            int attackTroops = Math.min(resources, 20); // or any smart allocation
            actions.add(CombatResponseAction.attack(enemy.playerId, attackTroops));
            resources -= attackTroops;
        }

// 5️⃣ Upgrade if enough resources left
        int upgradeCost = computeUpgradeCost(request.playerTower.level);
        if (resources >= upgradeCost) {
            actions.add(CombatResponseAction.upgrade());
        }

        return actions;
    }

    /**
     * Compute threat score like in negotiation
     */
    private int computeThreatScore(EnemyTower enemy, int aggression) {
        return (enemy.level * 2) + enemy.armor - (enemy.hp / 10) + aggression;
    }

    /**
     * Count how many troops attacked you from previous attacks
     */
    private int countAggression(Long playerId, List<CombatAction> previousAttacks) {
        if (previousAttacks == null) return 0;
        return previousAttacks.stream()
                .filter(a -> a.playerId.equals(playerId) && a.action != null)
                .mapToInt(a -> a.action.troopCount)
                .sum();
    }

    /**
     * Compute cost of upgrading from current level
     */
    private int computeUpgradeCost(int level) {
        return (int) Math.round(50 * Math.pow(1.75, level - 1));
    }
}
