package com.nixathon.szimpla.controller;

import com.nixathon.szimpla.records.BotInfo;
import com.nixathon.szimpla.records.CombatAction;
import com.nixathon.szimpla.records.CombatRequest;
import com.nixathon.szimpla.records.CombatResponseAction;
import com.nixathon.szimpla.records.DiplomacyAction;
import com.nixathon.szimpla.records.NegotiateRequest;
import com.nixathon.szimpla.service.CombactService;
import com.nixathon.szimpla.service.NegociationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    private final NegociationService negotiationService;

    private final CombactService combactService;

    public TestController(NegociationService negotiationService, CombactService combactService) {
        this.negotiationService = negotiationService;
        this.combactService = combactService;
    }

    // Basic ping test
    @GetMapping("/healthz")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "OK"
        );
    }

    @PostMapping("/negotiate")
    public List<DiplomacyAction> negotiate(@RequestBody NegotiateRequest request) {
        return negotiationService.negociate(request);
    }


    @PostMapping("/combat")
    public List<CombatResponseAction> combat(@RequestBody CombatRequest request) {
        return combactService.decideCombat(request);
    }

    @GetMapping("/info")
    public BotInfo info() {
        BotInfo info = new BotInfo();
        info.name = "Mega ogudor";
        info.strategy = "AI-trapped-strategy";
        info.version = "1.0";
        return info;
    }

}
