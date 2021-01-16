package com.mugui.block.TRC20;

public class TransactionResult {
    public String txId;
    public Boolean state;

    public String message;

    public TransactionResult(String txId, Boolean state, String message) {
        this.txId = txId;
        this.state = state;
        this.message = message;
    }

    public String toString() {
        return String.format("{txId:%s,state:%b,message:%s}", txId, state,message);
    }
}