package ru.sinitsynme.logistapi.exception;

public class ServiceExceptionMessageTemplates {

    public static final String AUTHORITY_NOT_FOUND_TEMPLATE
            = "Authority with name %s is not found";

    public static final String AUTHORITY_EXISTS_TEMPLATE
            = "Authority with name %s already exists";

    public static final String AUTHORITY_IS_BASIC_TEMPLATE
            = "Cannot delete authority %s because it is basic";

    public static final String USERS_HAVE_AUTHORITY_THAT_IS_BEING_REMOVED_TEMPLATE
            = "There are users with authority %s which is wanted to be deleted";

    public static final String AUTHORITY_MUST_HAVE_ROLE_PREFIX_MESSAGE
            = "Authority must start with \"ROLE_\" prefix";

    public static final String USER_EMAIL_NOT_FOUND_TEMPLATE
            = "User with email %s is not found";
    public static final String USER_ID_NOT_FOUND_TEMPLATE
            = "User with email %s is not found";

    public static final String USER_EXISTS_TEMPLATE
            = "User with email %s already exists";

    public static final String USER_ALREADY_BLOCKED_TEMPLATE
            = "User with id %s block state wasn't changed";

    public static final String USER_ALREADY_DISABLED_TEMPLATE
            = "User with id %s account disability wasn't changed";

    public static final String USER_ALREADY_HAS_ROLE_TEMPLATE
            = "User with id %s already has this role";

    public static final String CLIENT_ROLE_IS_TRIED_TO_BE_REVOKED_TEMPLATE
            = "Client role cannot be revoked from user with id %s. Block or disable user to revoke access";

    public static final String USER_DOESNT_HAVE_ROLE_TEMPLATE
            = "User with id %s doesn't have role %s";

    public static final String INVALID_AUTH_TEMPLATE
            = "Authentication for user with email '%s' was invalid due to cause '%s'";

    public static final String INVALID_JWT_MESSAGE
            = "JWT is invalid";
}
