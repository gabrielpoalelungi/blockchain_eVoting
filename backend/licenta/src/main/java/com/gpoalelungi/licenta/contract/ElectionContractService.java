package com.gpoalelungi.licenta.contract;


import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ElectionContractService {

  private final Web3j web3j = Web3j.build(new HttpService("http://localhost:7545"));
  private static final String CONTRACT_ADDRESS = "0x6Daa7D69947eC2A51c4eE90483AE2c0f0Ac9283F";
  private static final String ADMIN_ADDRESS = "0x17987d50dD4439Ed4Cf3975Ef6752D1C7a076cA3"; // replace with your actual admin address
  private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000);
  private static final BigInteger GAS_PRICE = BigInteger.valueOf(1000000000); // 1 Gwei
  private ContractGasProvider contractGasProvider = new DefaultGasProvider();
  private Election election = loadContract();


  private Election loadContract() {
    TransactionManager txManager = new ClientTransactionManager(web3j, ADMIN_ADDRESS);
    return Election.load(CONTRACT_ADDRESS, web3j, txManager, contractGasProvider);
  }

  public BigInteger getVoteCount() throws Exception {
    final Function function = new Function(
        "getAllVotes",
        Collections.emptyList(),
        Arrays.asList(new TypeReference<Uint256>() {}));

    String encodedFunction = FunctionEncoder.encode(function);

    Transaction transaction = Transaction.createEthCallTransaction(
        null, CONTRACT_ADDRESS, encodedFunction);

    EthCall response = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST)
        .sendAsync().get();

    if (response.hasError()) {
      throw new Exception("Error getting vote count: " + response.getError().getMessage());
    }

    String result = response.getValue();
    return new BigInteger(result.substring(2), 16);
  }

  public String endVote() throws Exception {
    Transaction transaction = Transaction.createFunctionCallTransaction(
        ADMIN_ADDRESS, null, GAS_PRICE, GAS_LIMIT, CONTRACT_ADDRESS,
        election.endVote().encodeFunctionCall());

    String transactionHash = web3j.ethSendTransaction(transaction).send().getTransactionHash();

    // Wait for transaction to be mined
    Optional<TransactionReceipt> receiptOptional;
    do {
      TimeUnit.SECONDS.sleep(2);
      receiptOptional = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();
    } while (!receiptOptional.isPresent());

    List<Election.VoteStartedOrFinishedEventResponse> events = Election.getVoteStartedOrFinishedEvents(receiptOptional.get());
    return events.get(0).message;
  }

  public String startVote() throws Exception {
    Transaction transaction = Transaction.createFunctionCallTransaction(
        ADMIN_ADDRESS, null, GAS_PRICE, GAS_LIMIT, CONTRACT_ADDRESS,
        election.startVote().encodeFunctionCall());

    String transactionHash = web3j.ethSendTransaction(transaction).send().getTransactionHash();

    // Wait for transaction to be mined
    Optional<TransactionReceipt> receiptOptional;
    do {
      TimeUnit.SECONDS.sleep(2);
      receiptOptional = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();
    } while (!receiptOptional.isPresent());

    List<Election.VoteStartedOrFinishedEventResponse> events = Election.getVoteStartedOrFinishedEvents(receiptOptional.get());
    return events.get(0).message;
  }

  public Boolean isVoteStarted() throws Exception {
    Transaction transaction = Transaction.createFunctionCallTransaction(
        ADMIN_ADDRESS, null, GAS_PRICE, GAS_LIMIT, CONTRACT_ADDRESS,
        election.isVoteStarted().encodeFunctionCall());

    EthCall response = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
    String value = response.getValue();
    return ((Bool) election.isVoteStarted().decodeFunctionResponse(value).get(0)).getValue();
  }

  public Boolean isVoteFinished() throws Exception {
    Transaction transaction = Transaction.createFunctionCallTransaction(
        ADMIN_ADDRESS, null, GAS_PRICE, GAS_LIMIT, CONTRACT_ADDRESS,
        election.isVoteFinished().encodeFunctionCall());

    EthCall response = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
    String value = response.getValue();
    return ((Bool) election.isVoteFinished().decodeFunctionResponse(value).get(0)).getValue();
  }
}
