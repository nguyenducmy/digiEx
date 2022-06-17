package com.supertomato.restaurant.entity;

import com.supertomato.restaurant.common.enums.AppStatus;
import com.supertomato.restaurant.common.enums.TypeLine;
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
@Table(name = "product_line")

public class ProductLine extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 64)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type_line")
    @Enumerated(EnumType.STRING)
    private TypeLine typeLine;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "outlet_id", nullable = false)
    private String outletId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppStatus status;
}
