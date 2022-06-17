package com.supertomato.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.Category;
import com.supertomato.restaurant.common.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.config.AuditingBeanDefinitionParser;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@EntityListeners(AuditingBeanDefinitionParser.class)
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@Table(name = "ingredient")

public class Ingredient extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 64)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "portion")
    private double portion;

    @Column(name = "acceptable_deviation")
    private double acceptableDeviation;

    @Column(name = "ordinal_number")
    private int ordinalNumber;

    @Column(name = "module")
    private int module;

    @Column(name = "outlet_id", nullable = false)
    private String outletId;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppStatus status;
}
