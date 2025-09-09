package com.banking.cdeh_msa_dm_account.util;

public class LogMessages {

    // Request messages
    public static final String CREATING_ACCOUNT_REQUEST = "Request received to create account with number: {}";
    public static final String GETTING_ACCOUNT_BY_ID_REQUEST = "Request received to get account by ID: {}";
    public static final String GETTING_ACCOUNT_BY_NUMBER_REQUEST = "Request received to get account by number: {}";
    public static final String GETTING_ALL_ACCOUNTS_REQUEST = "Request received to get all active accounts";
    public static final String GETTING_ALL_ACCOUNT_ACTIVE_REQUEST = "Request received to get all active accounts using custom query";
    public static final String GETTING_ACCOUNTS_BY_CUSTOMER_REQUEST = "Request received to get accounts by customer ID: {}";
    public static final String UPDATING_ACCOUNT_REQUEST = "Request received to update account with ID: {}";
    public static final String UPDATING_BALANCE_REQUEST = "Request received to update balance for account ID: {}";
    public static final String DELETING_ACCOUNT_REQUEST = "Request received to soft delete account with ID: {}";

    // Success messages
    public static final String ACCOUNT_CREATED_SUCCESS = "Account created successfully with ID: {}";
    public static final String ACCOUNT_FOUND_BY_ID = "Account found with ID: {}";
    public static final String ACCOUNT_FOUND_BY_NUMBER = "Account found with number: {}";
    public static final String ALL_ACCOUNTS_RETRIEVED = "Retrieved all active accounts";
    public static final String ALL_ACCOUNT_ACTIVE_RETRIEVED = "Retrieved all active accounts using custom query";
    public static final String ACCOUNTS_BY_CUSTOMER_RETRIEVED = "Retrieved accounts for customer: {}";
    public static final String ACCOUNT_UPDATED_SUCCESS = "Account updated successfully with ID: {}";
    public static final String ACCOUNT_BALANCE_UPDATED_SUCCESS = "Account balance updated successfully for ID: {}";
    public static final String ACCOUNT_DELETED_SUCCESS = "Account soft deleted successfully with ID: {}";

    // Error messages
    public static final String ERROR_CREATING_ACCOUNT = "Error creating account: {}";
    public static final String ERROR_GETTING_ACCOUNT_BY_ID = "Error getting account by ID {}: {}";
    public static final String ERROR_GETTING_ACCOUNT_BY_NUMBER = "Error getting account by number {}: {}";
    public static final String ERROR_GETTING_ALL_ACCOUNTS = "Error getting all accounts: {}";
    public static final String ERROR_GETTING_ALL_ACCOUNT_ACTIVE = "Error getting all active accounts: {}";
    public static final String ERROR_GETTING_ACCOUNTS_BY_CUSTOMER = "Error getting accounts for customer {}: {}";
    public static final String ERROR_UPDATING_ACCOUNT = "Error updating account {}: {}";
    public static final String ERROR_UPDATING_BALANCE = "Error updating balance for account {}: {}";
    public static final String ERROR_DELETING_ACCOUNT = "Error soft deleting account {}: {}";

    // Exception messages
    public static final String ACCOUNT_NOT_FOUND_BY_ID = "Account not found with ID: ";
    public static final String ACCOUNT_NOT_FOUND_BY_NUMBER = "Account not found with number: ";
}
