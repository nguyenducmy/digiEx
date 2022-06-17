package com.supertomato.restaurant.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AverageNumber {

    private long fulfilmentTime;
    private long waitingTime;
    private int deviation;
    private int numberOrder;

    public AverageNumber(long fulfilmentTime, long waitingTime,
                         int deviation, int numberOrder) {
        this.fulfilmentTime = fulfilmentTime;
        this.waitingTime = waitingTime;
        this.deviation = deviation;
        this.numberOrder = numberOrder;
    }

}
