package com.supertomato.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supertomato.restaurant.common.enums.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "outlet")
public class Outlet extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 64)
    private String id;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "outlet_prefix")
    private String outletPrefix;

    @Column(name = "kitchen_account")
    private String kitchenAccount;

    @Column(name = "kitchen_password")
    private String kitchenPassword;

    @Column(name = "start_line_status")
    @Enumerated(EnumType.STRING)
    private AppStatus startLineStatus;

    @Column(name = "end_line_status")
    @Enumerated(EnumType.STRING)
    private AppStatus endLineStatus;

    @Column(name = "master_ingredients_status")
    @Enumerated(EnumType.STRING)
    private AppStatus masterIngredientsStatus;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppStatus status;


}
