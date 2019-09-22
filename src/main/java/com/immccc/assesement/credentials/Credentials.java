package com.immccc.assesement.credentials;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder()
@Getter
@JsonDeserialize(builder = Credentials.CredentialsBuilder.class)
public class Credentials {
    private String username;
    private String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class CredentialsBuilder {
    }
}
