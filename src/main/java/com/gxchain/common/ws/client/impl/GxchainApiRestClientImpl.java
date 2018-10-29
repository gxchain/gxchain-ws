package com.gxchain.common.ws.client.impl;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.gxchain.common.ws.client.GxchainApiRestClient;
import com.gxchain.common.ws.client.core.GxbApiFactory;
import com.gxchain.common.ws.client.exception.GxchainApiException;
import com.gxchain.common.ws.client.graphenej.RPC;
import com.gxchain.common.ws.client.graphenej.models.*;
import com.gxchain.common.ws.client.graphenej.objects.Asset;
import com.gxchain.common.ws.client.graphenej.objects.AssetAmount;
import com.gxchain.common.ws.client.graphenej.operations.BaseOperation;
import com.gxchain.common.ws.client.util.WsGsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liruobin
 * @since 2018/7/5 上午10:39
 */
public class GxchainApiRestClientImpl implements GxchainApiRestClient {

    private GxchainApiService apiService;

    private String chainId;

    public GxchainApiRestClientImpl(String url) {
        apiService = GxbApiFactory.builder().baseUrl(url).build().newApi(GxchainApiService.class);
    }

    private String execute(ApiCall apiCall) {
        try {
            WitnessResponse<JsonElement> response = apiService.call(apiCall).execute().body();
            if (response.error != null) {
                throw new GxchainApiException(response.error);
            }
            return WsGsonUtil.toJson(response.getResult());
        } catch (IOException e) {
            throw new GxchainApiException(e);
        }
    }

    @Override
    public String getChainId() {
        if (StringUtils.isNotBlank(chainId)) {
            return chainId;
        }
        ArrayList<Serializable> emptyParams = new ArrayList<>();
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_CHAIN_ID, emptyParams, RPC.VERSION, 0);
        return execute(apiCall).replace("\"", "");
    }

    @Override
    public DynamicGlobalProperties getDynamicGlobalProperties() {
        ArrayList<Serializable> emptyParams = new ArrayList<>();
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_DYNAMIC_GLOBAL_PROPERTIES, emptyParams, RPC.VERSION, 0);
        return (DynamicGlobalProperties) WsGsonUtil.fromJson(execute(apiCall), DynamicGlobalProperties.class);
    }

    @Override
    public List<AssetAmount> getRequiredFees(List<BaseOperation> operations, Asset feeAsset) {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add((Serializable) operations);
        accountParams.add(feeAsset.getObjectId());
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_REQUIRED_FEES, accountParams, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), new TypeToken<List<AssetAmount>>() {
        }.getType());

    }

    @Override
    public List<AssetAmount> getAccountBalance(String accountId, List<String> assetIds) {
        ArrayList<Serializable> params = new ArrayList<>();
        ArrayList<Serializable> assetList = new ArrayList<>();
        assetList.addAll(assetIds);
        params.add(accountId);
        params.add(assetList);
        ApiCall apiCall = new ApiCall(0, RPC.GET_ACCOUNT_BALANCES, params, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), new TypeToken<List<AssetAmount>>() {
        }.getType());
    }

    @Override
    public AccountProperties getAccountByName(String accountName) {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add(accountName);
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_ACCOUNT_BY_NAME, accountParams, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), AccountProperties.class);
    }

    @Override
    public List<AccountProperties> getAccounts(List<String> accountIds) {
        ArrayList<Serializable> params = new ArrayList<>();
        ArrayList<Serializable> accountIdList = new ArrayList<>();
        for (String account : accountIds) {
            accountIdList.add(account);
        }
        params.add(accountIdList);
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_ACCOUNTS, params, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), new TypeToken<List<AccountProperties>>() {
        }.getType());
    }

    @Override
    public Block getBlock(long blockHeight) {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add(blockHeight);
        ApiCall apiCall = new ApiCall(0, RPC.CALL_GET_BLOCK, accountParams, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), Block.class);
    }

    @Override
    public JsonElement getObjects(List<String> objectIds) {
        if (objectIds == null || objectIds.isEmpty()) {
            return null;
        }
        ArrayList<Serializable> params = new ArrayList<>();
        ArrayList<Serializable> params2 = new ArrayList<>();
        for (String objectId : objectIds) {
            params2.add(objectId);
        }
        params.add(params2);

        ApiCall apiCall = new ApiCall(0, RPC.GET_OBJECTS, params, RPC.VERSION, 0);
        return WsGsonUtil.fromJson(execute(apiCall), JsonElement.class);
    }
}
