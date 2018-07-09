package com.gxchain.common.ws.client.impl;

import com.gxchain.common.ws.client.GxchainApiRestClient;
import com.gxchain.common.ws.client.GxchainClientFactory;
import com.gxchain.common.ws.client.graphenej.models.AccountProperties;
import com.gxchain.common.ws.client.util.WsGsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author liruobin
 * @since 2018/7/5 上午11:20
 */
@Slf4j
public class GxchainApiRestClientImplTest {
    GxchainApiRestClient client = GxchainClientFactory.getInstance().newRestCLient("http://192.168.1.118:28090");
    @Test
    public void getChainId() throws Exception {
        log.info(client.getChainId());
    }

    @Test
    public void getDynamicGlobalProperties() throws Exception {
        log.info(WsGsonUtil.toJson(client.getDynamicGlobalProperties()));
    }

    @Test
    public void getAccountBalance(){
        client.getAccountBalance("1.2.183027", Collections.EMPTY_LIST);
    }
    @Test
    public void getAccountByName(){
        AccountProperties accountProperties = client.getAccountByName("a111111111111");
        System.out.println(accountProperties);
    }
    @Test
    public void getAccount(){
        List<AccountProperties> accountProperties = client.getAccounts(Arrays.asList("1.2.515222"));
        System.out.println(accountProperties.size());
    }
}