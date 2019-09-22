package com.immccc.assesement.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserToken {
    private final String token;
}
