package com.supertomato.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supertomato.restaurant.common.enums.AppStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.config.AuditingBeanDefinitionParser;

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
@EntityListeners(AuditingBeanDefinitionParser.class)
@Table(name = "ingredient_history")

public class IngredientHistory extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 64)
    private String id;

    @Column(name = "ingredient_id", nullable = false)
    private String ingredientId;

    @Column(name = "portion")
    private Double portion;

    @Column(name = "dispensed_time")
    private Date dispensedTime;

    @Column(name = "is_deviation", nullable = false, columnDefinition = "tinyint(1) default 0")
    @JsonProperty(value = "is_deviation")
    private boolean isDeviation;

}
