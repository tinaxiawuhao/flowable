package com.example.flowable.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartProcess {
    private String processDefinitionKey;
    private Map<String, Object> variables;
}
