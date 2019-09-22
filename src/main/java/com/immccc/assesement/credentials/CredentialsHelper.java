package com.immccc.assesement.credentials;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class CredentialsHelper {

    public void assertValidCredentials(Credentials credentials) {
        boolean isValidCredentials =
                areAllCredentialsFieldsProvided(credentials) && passwordIsUsernameUpperCased(credentials);
        if (!isValidCredentials) {
            throw new InvalidCredentialsException();
        }
    }

    private boolean passwordIsUsernameUpperCased(Credentials credentials) {
        String upperCaseUsername = StringUtils.upperCase(credentials.getUsername());
        return StringUtils.equals(upperCaseUsername, credentials.getPassword());
    }

    private boolean areAllCredentialsFieldsProvided(Credentials credentials) {
        return Optional.ofNullable(credentials.getUsername()).isPresent() &&
                Optional.ofNullable(credentials.getPassword()).isPresent();
    }
}
