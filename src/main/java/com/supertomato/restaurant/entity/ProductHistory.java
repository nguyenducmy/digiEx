package com.supertomato.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product_history")

public class    ProductHistory extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 64)
    private String id;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "waiting_time")
    private long waitingTime;

    @Column(name = "outlet_id")
    private String outletId;


}
