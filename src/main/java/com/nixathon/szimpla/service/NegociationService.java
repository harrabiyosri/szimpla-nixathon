package com.nixathon.szimpla.service;

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

        if (request.enemyTowers == null || request.enemyTowers.isEmpty()) {
            return List.of(); // no enemies means skip
        }

        // Map to track how aggressive each enemy was last turn
        Map<Long, Integer> aggressionMap = new HashMap<>();
        if (request.combatActions != null) {
            for (var action : request.combatActions) {
                if (action.action != null && action.action.targetId.equals(request.playerTower.playerId)) {
                    aggressionMap.put(action.playerId, aggressionMap.getOrDefault(action.playerId, 0) + action.action.troopCount);
                }
            }
        }

        // Compute threat score for each enemy
        Map<EnemyTower, Integer> threatScores = new HashMap<>();
        for (EnemyTower enemy : request.enemyTowers) {
            int aggression = aggressionMap.getOrDefault(enemy.playerId, 0);
            int score = computeThreatScore(enemy, aggression);
            threatScores.put(enemy, score);
        }

        // Pick attack target = enemy with highest threat
        EnemyTower attackTarget = threatScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Pick ally candidate = enemy with lowest threat
        EnemyTower allyCandidate = threatScores.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // If both exist and are not the same
        if (allyCandidate != null && attackTarget != null && !allyCandidate.playerId.equals(attackTarget.playerId)) {
            DiplomacyAction action = new DiplomacyAction();
            action.allyId = allyCandidate.playerId;
            action.attackTargetId = attackTarget.playerId;
            return List.of(action);
        }

        return List.of(); // default: no diplomacy
    }

    /**
     * Compute a simple threat score combining level, armor, hp, and aggression
     */
    private int computeThreatScore(EnemyTower enemy, int aggression) {
        return (enemy.level * 2) + enemy.armor - (enemy.hp / 10) + aggression;
    }

}
