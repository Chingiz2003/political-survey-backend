package com.politicalsurvey.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final String recipientAddress;

    public BlockchainService(
            @Value("${blockchain.private-key}") String privateKey,
            @Value("${blockchain.recipient-address}") String recipientAddress
    ) {
        this.web3j = Web3j.build(new HttpService("http://127.0.0.1:7545")); // Ganache URL
        this.credentials = Credentials.create(privateKey); // Приватный ключ из .properties
        this.recipientAddress = recipientAddress;
    }

    public String sendToBlockchain(String data) {
        try {
            BigInteger nonce = web3j.ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST
            ).send().getTransactionCount();

            BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L); // = 20 Gwei
            BigInteger gasLimit = BigInteger.valueOf(100_000); // Достаточно для передачи данных

            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    gasLimit,
                    recipientAddress,
                    BigInteger.ZERO,
                    data
            );

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 1337, credentials); // Chain ID = 1337
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction sendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            if (sendTransaction.hasError()) {
                System.out.println("TX Error: " + sendTransaction.getError().getMessage());
                throw new RuntimeException("Ошибка при отправке транзакции: " + sendTransaction.getError().getMessage());
            }

            return sendTransaction.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка отправки в блокчейн", e);
        }
    }
}
