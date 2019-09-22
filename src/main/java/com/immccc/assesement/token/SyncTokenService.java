package com.immccc.assesement.token;

import com.immccc.assesement.credentials.Credentials;
import com.immccc.assesement.user.User;

abstract class SyncTokenService {

    protected abstract User authenticate(Credentials credentials);

    protected abstract UserToken issueToken(User user);

    public UserToken requestToken(Credentials credentials) {
        User user = authenticate(credentials);
        return issueToken(user);
    }
}
