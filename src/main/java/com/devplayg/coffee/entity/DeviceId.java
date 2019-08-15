package com.devplayg.coffee.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class DeviceId  implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    private Long assetId;

    @EqualsAndHashCode.Include
    @Id
    private String uid;
}
