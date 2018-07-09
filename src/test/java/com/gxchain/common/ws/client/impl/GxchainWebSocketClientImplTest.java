package com.gxchain.common.ws.client.impl;

import com.gxchain.common.ws.client.GxchainApiRestClient;
import com.gxchain.common.ws.client.GxchainClientFactory;
import com.gxchain.common.ws.client.GxchainWebSocketClient;
import com.gxchain.common.ws.client.constant.WSConstants;
import com.gxchain.common.ws.client.graphenej.models.DynamicGlobalProperties;
import com.gxchain.common.ws.client.graphenej.objects.*;
import com.gxchain.common.ws.client.graphenej.operations.BaseOperation;
import com.gxchain.common.ws.client.graphenej.operations.TransferOperation;
import com.gxchain.common.ws.client.graphenej.operations.TransferOperationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author liruobin
 * @since 2018/7/3 下午2:52
 */
@Slf4j
public class GxchainWebSocketClientImplTest {
    GxchainWebSocketClient client = GxchainClientFactory.getInstance().newWebSocketClient("ws://192.168.1.118:28090");
    GxchainApiRestClient httpClient = GxchainClientFactory.getInstance().newRestCLient("http://192.168.1.118:28090");

    /**
     * 转账交易
     *
     * @throws Exception
     */
    @Test
    public void transferOperation() throws Exception {

        //构建转账交易体
        UserAccount from = new UserAccount("1.2.323");//发起方
        String privateKey = "5K8iH1jMJxn8TKXXgHJHjkf8zGXsbVPvrCLvU2GekDh2nk4ZPSF";//发起方私钥
        UserAccount to = new UserAccount("1.2.21");//接收方

        //转账交易体
        TransferOperation transferOperation =
                new TransferOperationBuilder().
                        setTransferAmount(new AssetAmount(1000, WSConstants.GXS_ASSET_ID)).//交易金额
                        setSource(from).
                        setDestination(to).
                        build();

        ArrayList<BaseOperation> operations = new ArrayList<>();
        operations.add(transferOperation);
        Transaction transaction = new Transaction(privateKey, null, operations);

        transaction.setChainId(httpClient.getChainId());
        DynamicGlobalProperties dynamicProperties = httpClient.getDynamicGlobalProperties();
        long expirationTime = (dynamicProperties.time.getTime() / 1000) + Transaction.DEFAULT_EXPIRATION_TIME;
        String headBlockId = dynamicProperties.head_block_id;
        long headBlockNumber = dynamicProperties.head_block_number;
        //最新的区块信息
        transaction.setBlockData(new BlockData(headBlockNumber, headBlockId, expirationTime));
        //设置交易费用
        transaction.setFees(httpClient.getRequiredFees(transaction.getOperations(), new Asset(WSConstants.GXS_ASSET_ID)));
    }
}