package com.nixathon.szimpla.service;

import com.nixathon.szimpla.records.CombatAction;
import com.nixathon.szimpla.records.DiplomacyAction;
import com.nixathon.szimpla.records.EnemyTower;
import com.nixathon.szimpla.records.NegotiateRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NegociationService {


    /**
     * Decide diplomacy actions: who to ally with and who to attack.
     */
    public List<DiplomacyAction> negociate(NegotiateRequest request) {

        // Skip if no enemies
        if (request.enemyTowers == null || request.enemyTowers.isEmpty()) {
            return List.of();
        }

        // Build aggression map: track attacks on our tower
        Map<Long, Integer> aggressionMap = new HashMap<>();
        if (request.combatActions != null) {
            for (CombatAction action : request.combatActions) {
                if (action.action != null && action.action.targetId.equals(request.playerTower.playerId)) {
                    int old = aggressionMap.getOrDefault(action.playerId, 0);
                    // Weight repeated attacks more
                    aggressionMap.put(action.playerId, old + action.action.troopCount * 2);
                }
            }
        }

        // Compute threat scores for each enemy
        Map<EnemyTower, Integer> threatScores = new HashMap<>();
        for (EnemyTower enemy : request.enemyTowers) {
            int aggression = aggressionMap.getOrDefault(enemy.playerId, 0);
            int score = computeThreatScore(enemy, aggression, request.turn);
            threatScores.put(enemy, score);
        }

        // Pick attack target = enemy with highest threat
        EnemyTower attackTarget = threatScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Pick ally candidate: lowest threat, not attacking us, not the attack target
        EnemyTower allyCandidate = request.enemyTowers.stream()
                .filter(e -> !e.playerId.equals(attackTarget != null ? attackTarget.playerId : -1))
                .filter(e -> aggressionMap.getOrDefault(e.playerId, 0) == 0)
                .min(Comparator.comparingInt(e -> computeThreatScore(e, 0, request.turn)))
                .orElse(null);

        // Return diplomacy action if valid
        if (allyCandidate != null && attackTarget != null
                && !allyCandidate.playerId.equals(attackTarget.playerId)) {
            DiplomacyAction action = new DiplomacyAction();
            action.allyId = allyCandidate.playerId;
            action.attackTargetId = attackTarget.playerId;
            return List.of(action);
        }

        // Default: no diplomacy
        return List.of();
    }

    /**
     * Compute a threat score combining level, armor, hp, aggression, and turn/fatigue
     */
    private int computeThreatScore(EnemyTower enemy, int aggression, int turn) {
        int hpFactor = 100 - enemy.hp; // lower HP = higher threat
        int levelFactor = enemy.level * 5;
        int armorFactor = enemy.armor * 2;
        int aggressionFactor = aggression * 3;
        int fatigueFactor = Math.max(0, turn - 25) * 2; // escalating damage after turn 25

        return levelFactor + armorFactor + hpFactor + aggressionFactor + fatigueFactor;
    }

}
