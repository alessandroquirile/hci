package com.quiriletelese.troppadvisorproject.dao_interfaces;

import android.content.Context;

import com.amazonaws.services.cognitoidentityprovider.model.InitiateAuthResult;
import com.quiriletelese.troppadvisorproject.interfaces.VolleyCallbackCreateUser;
import com.quiriletelese.troppadvisorproject.interfaces.VolleyCallbackLogin;
import com.quiriletelese.troppadvisorproject.models.Account;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public interface AccountDAO {

    void login(VolleyCallbackLogin volleyCallbackLogin, Account account, Context context);

    void createAccount(VolleyCallbackCreateUser volleyCallbackCreateUser, Account account, Context context);

    void updatePassword(Account account, Context context, String newPassword);

}
