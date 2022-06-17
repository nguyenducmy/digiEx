package com.supertomato.restaurant.controller.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class TotalServedResponse {

    private long totalBowl;
    private long totalKebab;
    private long totalQuesadilla;
    private long totalBurrito;
    private long totalServed;
   private List<Served> servedList;

    public TotalServedResponse(long totalBowl, long totalKebab,
                               long totalQuesadilla, long totalBurrito,
                               long totalServed, List<Served> servedList) {
        this.totalBowl = totalBowl;
        this.totalKebab = totalKebab;
        this.totalQuesadilla = totalQuesadilla;
        this.totalBurrito = totalBurrito;
        this.totalServed = totalServed;
        this.servedList = servedList;
    }

}
