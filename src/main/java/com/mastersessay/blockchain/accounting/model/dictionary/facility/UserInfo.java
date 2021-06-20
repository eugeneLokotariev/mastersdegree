package com.mastersessay.blockchain.accounting.model.dictionary.facility;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@MappedSuperclass
@Setter
@Getter
public abstract class UserInfo implements Serializable {
    public UserInfo() {
    }

    public UserInfo(@NotBlank String createdWhen, @NotBlank String createdBy, String modifiedWhen, String modifiedBy) {
        this.createdWhen = createdWhen;
        this.createdBy = createdBy;
        this.modifiedWhen = modifiedWhen;
        this.modifiedBy = modifiedBy;
    }

    @Column(name = "created_when")
    @NotBlank
    protected String createdWhen;

    @Column(name = "created_by_user")
    @NotBlank
    protected String createdBy;

    @Column(name = "modified_when")
    protected String modifiedWhen;

    @JoinColumn(name = "updated_by_user")
    protected String modifiedBy;
}
