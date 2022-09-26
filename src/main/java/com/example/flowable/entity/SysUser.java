package com.example.flowable.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.idm.api.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String displayName;
    protected String email;
    protected String password;
    protected String tenantId;

    @Override
    public boolean isPictureSet() {
        return false;
    }
}
