package com.custom.gateway.model.core;

import lombok.Data;

@Data
public class AllowedEntity {
    private Boolean code = true;
    private String tocken;

    public AllowedEntity() {

    }

    public AllowedEntity(Boolean code, String tocken) {
        this.code = code;
        this.tocken = tocken;
    }
}
