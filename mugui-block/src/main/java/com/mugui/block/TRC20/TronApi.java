package com.mugui.block.TRC20;


import com.mugui.base.base.Component;
import com.mugui.base.util.Other;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class TronApi {
    private NodeClient fullNode;
    private NodeClient solidityNode;
    private NodeClient eventNode;
    private NodeClient dataNode;

    public TronApi() {

//        this("https://block.dragonschain.cn");//main
        this("https://trx.mytokenpocket.vip");//main
//       this("https://api.shasta.trongrid.io");
    }

    public TronApi(String url) {
        this(url, url, url);
    }

    public TronApi(String fullNodeUrl, String solidityNodeUrl, String eventNodeUrl) {

        fullNode = new NodeClient(fullNodeUrl);
        solidityNode = new NodeClient(solidityNodeUrl);
        eventNode = new NodeClient(eventNodeUrl);
        dataNode = new NodeClient("https://trx.tokenview.com");

        System.out.println("耗时：" + TronApi.class);
    }


//    public static TronApi mainNet() {
//        return new TronApi("https://block.dragonschain.cn");
////        return new TronApi("http://134.122.133.40:8091");
//    }

//    public static TronApi testNet() {
//        return new TronApi("https://api.shasta.trongrid.io");
//    }

    public String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    public TransferTransaction createTransaction(String to, long amount, String owner) throws Exception {
        CreateTransactionRequest req = new CreateTransactionRequest(
                Address.decode(to), amount, Address.decode(owner));
        return fullNode.post(TransferTransaction.class, "wallet/createtransaction", req);
    }

    public Account getAccount(String address) throws Exception {
        AccountRequest req = new AccountRequest(Address.decode(address));
        return fullNode.post(Account.class, "wallet/getaccount", req);
    }

    public ApiResult broadcastTransaction(Object tx) throws Exception {
        ApiResult ret = fullNode.post(ApiResult.class, "wallet/broadcasttransaction", tx);
        if (ret.message != null) {
            ret.message = hexStringToString(ret.message);
        }
        return ret;
    }

    public TriggerContractResponse triggerConstantContract(String contractAddress, String functionSelector, String parameter, String ownerAddress) throws Exception {
        TriggerContractRequest req = new TriggerContractRequest(
                Address.decode(contractAddress), functionSelector, parameter, Address.decode(ownerAddress));
        return fullNode.post(TriggerContractResponse.class, "wallet/triggerconstantcontract", req);
    }

    public TriggerContractResponse triggerSmartContract(String contractAddress, String functionSelector, String parameter, String ownerAddress) throws Exception {
        TriggerContractRequest req = new TriggerContractRequest(
                Address.decode(contractAddress), functionSelector, parameter, Address.decode(ownerAddress));
        return fullNode.post(TriggerContractResponse.class, "wallet/triggersmartcontract", req);
    }

    public List<ContractEvent> getBlockEvents(long block_number) throws Exception {
        String api = String.format("v1/blocks/%d/events", block_number);
        Map params = new HashMap<String, String>();
        params.put("sort", "block_timestamp");
        ContractEventResult result = eventNode.get(ContractEventResult.class, api, params);
        ;
        List<ContractEvent> li = result.contractEvents;
        for (ContractEvent e : li) {
            for (String key : e.result.keySet()) {
                if (key.equals("_from") || key.equals("_to") || key.equals("from") || key.equals("to")) {
                    String hexAddress = e.result.get(key).toString();
                    String address = hexAddress.substring(2, hexAddress.length());

                    address = "41" + address;

                    hexAddress = "0x" + address;

                    String base58Address = Address.encode(hexAddress);
                    e.result.put(key, base58Address);
                }
            }
        }
        return result.contractEvents;
    }

    public Long getLastNlock() throws Exception {

        Map map = dataNode.get(Map.class, "api/blocks/trx/1/1", new HashMap<>());
        List header = (List) map.get("data");
        Map rawData = (Map) header.get(0);
        BigDecimal number = new BigDecimal(rawData.get("block_no").toString());
        return number.longValue();
    }

    public List<TRXTransform> getTransformsByBlockNumber(long number) throws Exception {
        int pageSize = 50;
        int page = 1;
        int resultSize = 0;
        List<TRXTransform> trxTransforms = new ArrayList<>();
        do {
            String url = String.format("api/tx/trx/%d/%d/%d", number, page, pageSize);
            System.out.println(url);
            TrxTransactionResult data = dataNode.get(TrxTransactionResult.class, url, new HashMap<>());
            if (data.code == 1) {
                resultSize = data.trxTransformList.size();
                trxTransforms.addAll(data.trxTransformList.stream().filter(x -> x.tType.equals("Transfer")).collect(Collectors.toList()));

            } else {
                System.out.println(url);
                break;
            }
            page++;
        } while (resultSize >= pageSize);

        return trxTransforms;
    }

    public Map getTransformById(String hash) throws Exception {
        String url = "wallet/gettransactionbyid";
        Map params = new HashMap<String, String>();
        params.put("value", hash);
        return fullNode.post(Map.class, url, params);
    }

    /**
     * 合约交易
     *
     * @param privateKey
     * @param toAddress
     * @param amount
     * @param contractAddress
     * @return
     * @throws Exception
     */
    public TransactionResult transformToken(String privateKey, String toAddress, BigDecimal amount, String contractAddress) throws Exception {
        TronKit kit = new TronKit(
                this,
                Credential.fromPrivateKey(privateKey)
        );
        Trc20 token = kit.trc20(contractAddress);
        BigInteger decimals = token.decimals();
        TransactionResult ret = token.transfer(toAddress, amount.multiply(new BigDecimal(Math.pow(10, decimals.longValue()))).toBigInteger());
        return ret;
    }

    /**
     * 非合约交易
     *
     * @param privateKey
     * @param toAddress
     * @param amount
     * @return
     * @throws Exception
     */
    public TransactionResult transformTrx(String privateKey, String toAddress, long amount) throws Exception {
        TronKit kit = new TronKit(
                this,
                Credential.fromPrivateKey(privateKey)
        );

        Credential credential = Credential.fromPrivateKey(privateKey);
        Address address = credential.getAddress();


        System.out.println("get trx balance before transfer...");
        long b1 = kit.getTrxBalance(address.toString());
        if (b1 < amount) {
            return new TransactionResult("", false, "Trx balance is not enough");
        }
        TransactionResult ret = kit.sendTrx(toAddress, amount);
        return ret;
    }


    private TronKit default_kit = null;

    public BigDecimal getBalance(String address, String contractAddress) throws Exception {
        if (default_kit == null) {
            default_kit = new TronKit(
                    this,
                    Credential.fromPrivateKey("8D9142B97B38F992B4ADF9FB3D0DD527B1F47BE113C6D0B5C32A0571EF1E7B5F")
            );
        }
        if (StringUtils.isEmpty(contractAddress)) {
            return BigDecimal.valueOf(default_kit.getTrxBalance(address)).divide(new BigDecimal("1000000"));
        } else if (Other.isLong(contractAddress)) {
            Account account = default_kit.getAccount(address);
            Account.KeyValue[] assetV2 = account.assetV2;
            for (Account.KeyValue keyValue : assetV2) {
                if (keyValue.key.equals(contractAddress)) {
                    return BigDecimal.valueOf(keyValue.value).divide(new BigDecimal("1000000"));
                }
            }
            return BigDecimal.ZERO;
        }
        Trc20 token = default_kit.trc20(contractAddress);
        BigInteger value = token.balanceOf(address);
        BigDecimal decimal = BigDecimal.valueOf(token.decimals().longValue());
        BigDecimal result = new BigDecimal(value.toString()).divide(BigDecimal.valueOf(Math.pow(10, decimal.doubleValue())), 25, RoundingMode.DOWN
        );
        return result;


    }
}