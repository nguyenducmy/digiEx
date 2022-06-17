package com.supertomato.restaurant.controller.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.supertomato.restaurant.common.util.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Served {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date endTime;
    private long bowl;
    private long kebab;
    private long quesadilla;
    private long burrito;
    private long total;

    public Served(Date startTime, Date endTime, long bowl, long kebab, long quesadilla, long burrito, long total) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.bowl = bowl;
        this.kebab = kebab;
        this.quesadilla = quesadilla;
        this.burrito = burrito;
        this.total = total;
    }
}
