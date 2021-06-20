package com.mastersessay.blockchain.accounting.consts;

public enum UserRole {
    ROLE_CATALOG_ADMIN(BlockchainAccountingConstants.RolesName.CATALOG_ADMIN),
    ROLE_ORDER_ADMIN(BlockchainAccountingConstants.RolesName.ORDER_ADMIN),
    ROLE_MAINTENANCE_ADMIN(BlockchainAccountingConstants.RolesName.MAINTENANCE_ADMIN),
    ROLE_USER_ADMIN(BlockchainAccountingConstants.RolesName.USER_ADMIN),
    ROLE_USER_PLAIN(BlockchainAccountingConstants.RolesName.USER_PLAIN);

    private String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
