package com.mugui.block.TRC20;



public class TronKit {
    protected TronApi api;
    protected Credential credential;
    protected String defaultAddress;

    public TronKit(TronApi api, Credential credential) {
        this.api = api;
        this.credential = credential;
        this.defaultAddress = credential.getAddress().base58;
    }

    public String getDefaultAddress() {
        return this.defaultAddress;
    }

    public TransactionResult sendTrx(String to, long amount) throws Exception {
        TransferTransaction tx = api.createTransaction(to, amount, defaultAddress);
        String signature = credential.sign(tx.txId);
        tx.signature = new String[]{signature};
        ApiResult state = api.broadcastTransaction(tx);
        return new TransactionResult(state.txid, state.result, state.message);
    }

    public long getTrxBalance(String address) throws Exception {
        Account account = api.getAccount(address);
        return account.balance;
    }
    public Account getAccount(String address) throws Exception {
        return api.getAccount(address);
    }

    public Trc20 trc20(String contractAddress) {
        return new Trc20(api, credential, contractAddress);
    }


}