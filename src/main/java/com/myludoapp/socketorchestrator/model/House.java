package com.myludoapp.socketorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class House {

    private String id;
    private String color;

    private List<Token> tokens;

    private boolean active;

    private String classname;
    private boolean disabled;

    private String[] route;

    private String[] home;
}
