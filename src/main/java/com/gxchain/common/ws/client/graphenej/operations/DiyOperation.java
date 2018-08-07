package com.gxchain.common.ws.client.graphenej.operations;

import com.google.common.primitives.Bytes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gxchain.common.ws.client.graphenej.Util;
import com.gxchain.common.ws.client.graphenej.Varint;
import com.gxchain.common.ws.client.graphenej.enums.OperationType;
import com.gxchain.common.ws.client.graphenej.objects.AssetAmount;
import com.gxchain.common.ws.client.graphenej.objects.Extensions;
import com.gxchain.common.ws.client.graphenej.objects.UserAccount;
import lombok.Data;

/**
 * @Description
 * @Author Hanawa
 * @Date 2018/8/7
 * @Version 1.0
 */
@Data
public class DiyOperation extends BaseOperation {

    private AssetAmount fee;
    private UserAccount payer;
    private Extensions requiredAuths;
    private int d;
    private String data;

    public DiyOperation() {
        super(OperationType.DIY_OPERATION);
    }

    @Override
    public byte[] toBytes() {
        byte[] feeBytes = fee.toBytes();
        byte[] payerBytes = payer.toBytes();
        byte[] requireAuthsBytes = requiredAuths.toBytes();
        byte[] idBytes = Varint.writeUnsignedSize(d);
        byte[] dataPrefix = new byte[]{(byte) data.getBytes().length};
        byte[] dataBytes = data.getBytes();
        System.out.println();
        System.out.println("feeBytes:" + Util.bytesToHex(feeBytes));
        System.out.println("payerBytes:" + Util.bytesToHex(payerBytes));
        System.out.println("requireAuthBytes:" + Util.bytesToHex(requireAuthsBytes));
        System.out.println("idBytes:" + Util.bytesToHex(idBytes));
        System.out.println("dataPrefix:" + Util.bytesToHex(dataPrefix));
        System.out.println("dataBytes:" + Util.bytesToHex(dataBytes));
        System.out.println();
        return Bytes.concat(feeBytes, payerBytes, requireAuthsBytes, idBytes, dataPrefix, dataBytes);
    }

    @Override
    public String toJsonString() {
        return toJsonObject().toString();
    }

    @Override
    public JsonElement toJsonObject() {
        JsonArray array = new JsonArray();
        array.add(this.getId());
        JsonObject jsonObject = new JsonObject();
        if (fee != null)
            jsonObject.add("fee", fee.toJsonObject());
        jsonObject.addProperty("payer", payer.getObjectId());
        jsonObject.add("required_auths", new JsonArray());
        jsonObject.addProperty("id", d);
        jsonObject.addProperty("data", Util.bytesToHex(data.getBytes()));
        array.add(jsonObject);
        return array;
    }

}
