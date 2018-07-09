package com.gxchain.common.ws.client.graphenej.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Enum type used to keep track of all the operation types and their corresponding ids.
 *
 * <a href="https://bitshares.org/doxygen/operations_8hpp_source.html">Source</a>
 *
 * Created by nelson on 11/6/16.
 */
@AllArgsConstructor
@NoArgsConstructor
public enum OperationType {
    TRANSFER_OPERATION(0,"转账"),
    BROADCAST_STORE_DATA(73,"数据存储广播"),



    LIMIT_ORDER_CREATE_OPERATION(-1,""),
    LIMIT_ORDER_CANCEL_OPERATION(-1,""),
    CALL_ORDER_UPDATE_OPERATION(-1,""),
    FILL_ORDER_OPERATION(-1,""),           // VIRTUAL
    ACCOUNT_CREATE_OPERATION(-1,""),
    ACCOUNT_UPDATE_OPERATION(-1,""),
    ACCOUNT_WHITELIST_OPERATION(-1,""),
    ACCOUNT_UPGRADE_OPERATION(-1,""),
    ACCOUNT_TRANSFER_OPERATION(-1,""),
    ASSET_CREATE_OPERATION(-1,""),
    ASSET_UPDATE_OPERATION(-1,""),
    ASSET_UPDATE_BITASSET_OPERATION(-1,""),
    ASSET_UPDATE_FEED_PRODUCERS_OPERATION(-1,""),
    ASSET_ISSUE_OPERATION(-1,""),
    ASSET_RESERVE_OPERATION(-1,""),
    ASSET_FUND_FEE_POOL_OPERATION(-1,""),
    ASSET_SETTLE_OPERATION(-1,""),
    ASSET_GLOBAL_SETTLE_OPERATION(-1,""),
    ASSET_PUBLISH_FEED_OPERATION(-1,""),
    WITNESS_CREATE_OPERATION(-1,""),
    WITNESS_UPDATE_OPERATION(-1,""),
    PROPOSAL_CREATE_OPERATION(-1,""),
    PROPOSAL_UPDATE_OPERATION(-1,""),
    PROPOSAL_DELETE_OPERATION(-1,""),
    WITHDRAW_PERMISSION_CREATE_OPERATION(-1,""),
    WITHDRAW_PERMISSION_UPDATE_OPERATION(-1,""),
    WITHDRAW_PERMISSION_CLAIM_OPERATION(-1,""),
    WITHDRAW_PERMISSION_DELETE_OPERATION(-1,""),
    COMMITTEE_MEMBER_CREATE_OPERATION(-1,""),
    COMMITTEE_MEMBER_UPDATE_OPERATION(-1,""),
    COMMITTEE_MEMBER_UPDATE_GLOBAL_PARAMETERS_OPERATION(-1,""),
    VESTING_BALANCE_CREATE_OPERATION(-1,""),
    VESTING_BALANCE_WITHDRAW_OPERATION(-1,""),
    WORKER_CREATE_OPERATION(-1,""),
    CUSTOM_OPERATION(-1,""),
    ASSERT_OPERATION(-1,""),
    BALANCE_CLAIM_OPERATION(-1,""),
    OVERRIDE_TRANSFER_OPERATION(-1,""),
    TRANSFER_TO_BLIND_OPERATION(-1,""),
    BLIND_TRANSFER_OPERATION(-1,""),
    TRANSFER_FROM_BLIND_OPERATION(-1,""),
    ASSET_SETTLE_CANCEL_OPERATION(-1,""),  // VIRTUAL
    ASSET_CLAIM_FEES_OPERATION(-1,"");

    @Getter private int code;
    @Getter private String sub;
}
