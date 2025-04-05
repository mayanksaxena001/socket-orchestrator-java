package com.myludoapp.socketorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Token {

    private String id;

    private String house_id;

    private String token_id;
    private String color;

    private boolean active;

    private String classname;
    private boolean disabled;

    private boolean safe;

    private String position="base";

}
