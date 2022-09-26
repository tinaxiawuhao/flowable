package com.example.flowable.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.idm.api.Group;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysGroup implements Group {
    private  String id;
    private  String name;
    private  String type;
}
