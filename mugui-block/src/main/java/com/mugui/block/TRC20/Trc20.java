package com.mugui.block.TRC20;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class Trc20 extends Contract {
    private static int ADDRESS_LENGTH = 168;

    public Trc20(TronApi api, Credential credential, String contractAddress) {
        super(api, credential, contractAddress);
    }

    public TransactionResult transfer(String to, BigInteger value) throws Exception {
        String _to = Address.decode(to);
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return transact(function);
    }

    public TransactionResult transferFrom(String from, String to, BigInteger value) throws Exception {
        String _from = Address.decode(from);
        String _to = Address.decode(to);

        Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _from),
                        new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return transact(function);
    }

    public TransactionResult approve(String spender, BigInteger value) throws Exception {
        String _spender = Address.decode(spender);

        Function function = new Function(
                "approve",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _spender),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return transact(function);
    }

    public BigInteger allowance(String owner, String spender) throws Exception {
        String _owner = Address.decode(owner);
        String _spender = Address.decode(spender);

        Function function = new Function("allowance",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _owner),
                        new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return callWithSingleReturn(BigInteger.class, function);
    }

    public BigInteger balanceOf(String owner) throws Exception {
        String _owner = Address.decode(owner);

        Function function = new Function("balanceOf",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(ADDRESS_LENGTH, _owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        return callWithSingleReturn(BigInteger.class, function);
    }

    public BigInteger totalSupply() throws Exception {
        Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return callWithSingleReturn(BigInteger.class, function);
    }

    public String name() throws Exception {
        Function function = new Function("name",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return callWithSingleReturn(String.class, function);
    }

    public String symbol() throws Exception {
        Function function = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return callWithSingleReturn(String.class, function);
    }

    public BigInteger decimals() throws Exception {
        Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {
                }));
        return callWithSingleReturn(BigInteger.class, function);
    }

}