package com.gxchain.common.ws.client;

import com.gxchain.common.ws.client.graphenej.models.AccountProperties;
import com.gxchain.common.ws.client.graphenej.models.Block;
import com.gxchain.common.ws.client.graphenej.models.DynamicGlobalProperties;
import com.gxchain.common.ws.client.graphenej.objects.Asset;
import com.gxchain.common.ws.client.graphenej.objects.AssetAmount;
import com.gxchain.common.ws.client.graphenej.operations.BaseOperation;

import java.util.List;

/**
 * @author liruobin
 * @since 2018/7/5 上午10:39
 */
public interface GxchainApiRestClient {
    /**
     * 查询gxchain chainId
     *
     * @return
     */
    String getChainId();

    /**
     * 查询全局动态参数
     *
     * @return
     */
    DynamicGlobalProperties getDynamicGlobalProperties();

    /**
     * 获取交易费率
     *
     * @param operations 交易操作
     * @param feeAsset   费用资产
     */
    List<AssetAmount> getRequiredFees(List<BaseOperation> operations, Asset feeAsset);

    /**
     * 查询账户余额
     * @param accountId 账户id
     * @param assetIds 资产id list
     * @return
     */
    List<AssetAmount> getAccountBalance(String accountId, List<String> assetIds);

    /**
     * 根据名称获取公链账户信息
     * @param accountName
     * @return
     */
    AccountProperties getAccountByName(String accountName);

    /**
     * 根据accountId查询公链账户信息
     * @param accountIds
     * @return
     */
    List<AccountProperties> getAccounts(List<String> accountIds);

    /**
     * 根据区块高度获取区块信息
     * @param blockHeight 区块高度
     * @return
     */
    Block getBlock(long blockHeight);
}
