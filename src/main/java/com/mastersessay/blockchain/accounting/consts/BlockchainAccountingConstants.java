package com.mastersessay.blockchain.accounting.consts;

public interface BlockchainAccountingConstants {
    interface RolesName {
        String CATALOG_ADMIN = "ROLE_CATALOG_ADMIN";
        String ORDER_ADMIN = "ROLE_ORDER_ADMIN";
        String MAINTENANCE_ADMIN = "ROLE_MAINTENANCE_ADMIN";
        String USER_ADMIN = "ROLE_USER_ADMIN";
        String USER_PLAIN = "ROLE_PLAIN_USER";
    }

    interface Http {
        String AUTHORIZATION_HEADER_NAME = "Authorization";
        String BEARER = "Bearer ";
        String REQUEST_PROCESSING_END_MESSAGE =
                "==================================REQUEST PROCESSING END==================================";
        String REQUEST_PROCESSING_START_MESSAGE =
                "==================================REQUEST PROCESSING START==================================";

        String SORT_TYPE_VALUE_ASC = "asc";
        String SORT_TYPE_VALUE_DESC = "desc";
    }

    interface Security {
        String USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE = "User not found with username";
        String CANNOT_SET_USER_AUTHENTICATION_ERROR_MESSAGE = "Cannot set user authentication";

        String USER_IS_ALREADY_TAKEN_ERROR_MESSAGE = "Error: Username is already taken";
        String EMAIL_IS_ALREADY_IN_USE_ERROR_MESSAGE = "Error: Email is already in use";
        String ROLE_NOT_FOUND_ERROR_MESSAGE = "Error: Role not found";
        String UNAUTHORIZED_ERROR_MESSAGE = "Error: Unauthorized";
        String SUCCESSFUL_REGISTRATION_MESSAGE = "User registered successfully";

        String INVALID_JWT_SIGNATURE_ERROR_MESSAGE = "Invalid JWT signature";
        String INVALID_JWT_TOKEN_ERROR_MESSAGE = "Invalid JWT token";
        String JWT_TOKEN_EXPIRED_ERROR_MESSAGE = "JWT token is expired";
        String JWT_TOKEN_UNSUPPORTED_ERROR_MESSAGE = "JWT token is unsupported";
        String JWT_CLAIMS_STRING_EMPTY_ERROR_MESSAGE = "JWT claims string is empty";
    }

    interface BusinessMessages {
        String OBJECT_NOT_FOUND_MESSAGE = "Object not found";
        String MANUFACTURER_NOT_FOUND_MESSAGE = "Manufacturer not found";
        String OBJECT_EXISTS_MESSAGE = "Object already exists";
        String ENUM_VALUE_NOT_FOUND = "Enum value for field is not allowed";

        String ORDER_STATUS_FOR_UPDATE_INAPPROPRIATE = "Status for order update is inappropriate";
        String ORDER_STATUS_FOR_UPDATE_FINISHED_OR_CANCELLED = "Status for order update is finished or cancelled";

        String SUCCESSFULLY_CREATED_MESSAGE = "Successfully created";
        String SUCCESSFULLY_UPDATE_MESSAGE = "Successfully update";
        String SUCCESSFULLY_DELETED_MESSAGE = "Successfully deleted";
    }

}
