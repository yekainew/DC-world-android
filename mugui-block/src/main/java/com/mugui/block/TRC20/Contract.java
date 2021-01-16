package com.mugui.block.TRC20;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.DefaultFunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.util.List;
import java.util.stream.Collectors;

public class Contract {
    protected TronApi api;
    protected Credential credential;
    protected String contractAddress;
    protected DefaultFunctionEncoder encoder;
    protected DefaultFunctionReturnDecoder decoder;

    protected Logger logger;

    public Contract(TronApi api, Credential credential, String contractAddress) {
        this.api = api;
        this.credential = credential;
        this.contractAddress = contractAddress;
        this.encoder = new DefaultFunctionEncoder();
        this.decoder = new DefaultFunctionReturnDecoder();

        this.logger = LoggerFactory.getLogger(Contract.class);
    }


    @Deprecated
    public void test() throws Exception {
//    String _owner = Address.decode("TDN3QY85Jft3RwgyatjRNmrwRmwkn8qwqx");
//    Function function = new Function("balanceOf",
//            Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(168,_owner)),
//            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
//    System.out.println(getFunctionSelector(function));
    }

    private String getFunctionSelector(Function function) {
        String methodName = function.getName();
        List<Type> parameters = function.getInputParameters();

        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");

        String params ="";
        if(!parameters.isEmpty()){
            boolean bool=false;
            for (Type parameter : parameters) {
                if(!bool) {
                    params+= parameter.getTypeAsString();
                    bool=true;
                }else {
                    params+=","+ parameter.getTypeAsString();
                }
            }
        }
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public TransactionResult transact(Function function) throws Exception {
        String functionSelector = getFunctionSelector(function);
        String data = encoder.encodeParameters(function.getInputParameters());
        TriggerContractResponse rsp =
                api.triggerSmartContract(contractAddress, functionSelector, data, credential.getAddress().base58);
        String sig = credential.sign(rsp.transaction.txId);
        rsp.transaction.signature = new String[]{sig};
        logger.info("signature: {}", new Object[]{sig});
        ApiResult state = api.broadcastTransaction(rsp.transaction);
        return new TransactionResult(state.txid, state.result, state.message);
    }

    public List<Type> call(Function function) throws Exception {
        String functionSelector = getFunctionSelector(function);
        String data = encoder.encodeParameters(function.getInputParameters());
        TriggerContractResponse rsp =
                api.triggerConstantContract(contractAddress, functionSelector, data, credential.getAddress().base58);
        List<Type> result = decoder.decodeFunctionResult(rsp.constantResult[0], function.getOutputParameters());
        return result;
    }

    public <T> T callWithSingleReturn(Class<T> clazz, Function function) throws Exception {
        List<Type> result = call(function);
        return (T) result.get(0).getValue();
    }

    public List<ContractEvent> events(long block_number) throws Exception {
        return api.getBlockEvents(block_number);
    }
}