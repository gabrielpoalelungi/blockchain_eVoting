package com.gpoalelungi.licenta.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class Election extends Contract {
    public static final String BINARY = "6080604052600080556003805462010000600160b01b0319167517987d50dd4439ed4cf3975ef6752d1c7a076ca3000017905534801561003e57600080fd5b506003805461ffff19169055610dbb806100596000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063aca848921161005b578063aca84892146100c8578063b8ee0296146100db578063b9223946146100ee578063ba7faf49146100f657600080fd5b80634c0a6af0146100825780636826294c1461008c578063851b6ef2146100b3575b600080fd5b61008a610103565b005b60035461009e90610100900460ff1681565b60405190151581526020015b60405180910390f35b6100bb6101a6565b6040516100aa9190610949565b61008a6100d6366004610a8d565b61049a565b61008a6100e9366004610aca565b61064c565b61008a61081d565b60035461009e9060ff1681565b6003546201000090046001600160a01b0316331461013c5760405162461bcd60e51b815260040161013390610b52565b60405180910390fd5b6003805460ff191660011790556040517f46dc84b3735f5bde3eda04ed0139966e2cc450589305d827dae93f366be8dc599061019c9060208082526010908201526f159bdd19481a185cc81cdd185c9d195960821b604082015260600190565b60405180910390a1565b60035460609060ff6101009091041615156001146102065760405162461bcd60e51b815260206004820152601f60248201527f566f74696e672073657373696f6e206e6f742066696e697368656420796574006044820152606401610133565b6000805467ffffffffffffffff811115610222576102226109ea565b60405190808252806020026020018201604052801561027757816020015b61026460405180606001604052806060815260200160608152602001606081525090565b8152602001906001900390816102405790505b50905060005b60005481101561049457600160006102958383610b95565b81526020019081526020016000206040518060600160405290816000820180546102be90610bae565b80601f01602080910402602001604051908101604052809291908181526020018280546102ea90610bae565b80156103375780601f1061030c57610100808354040283529160200191610337565b820191906000526020600020905b81548152906001019060200180831161031a57829003601f168201915b5050505050815260200160018201805461035090610bae565b80601f016020809104026020016040519081016040528092919081815260200182805461037c90610bae565b80156103c95780601f1061039e576101008083540402835291602001916103c9565b820191906000526020600020905b8154815290600101906020018083116103ac57829003601f168201915b505050505081526020016002820180546103e290610bae565b80601f016020809104026020016040519081016040528092919081815260200182805461040e90610bae565b801561045b5780601f106104305761010080835404028352916020019161045b565b820191906000526020600020905b81548152906001019060200180831161043e57829003601f168201915b50505050508152505082828151811061047657610476610be8565b6020026020010181905250808061048c90610bfe565b91505061027d565b50905090565b6003546201000090046001600160a01b031633146104ca5760405162461bcd60e51b815260040161013390610b52565b600354610100900460ff161580156104e5575060035460ff16155b61053c5760405162461bcd60e51b815260206004820152602260248201527f566f74696e672073657373696f6e20686173207374617274656420616c726561604482015261647960f01b6064820152608401610133565b60008160405160200161054f9190610c17565b6040516020818303038152906040528051906020012090506000610572826108b0565b8051909150156105c45760405162461bcd60e51b815260206004820152601860248201527f566f74657220616c7265616479207265676973746572656400000000000000006044820152606401610133565b6040805180820182526001808252600060208084018281528783526002825285832094518554915161ffff1990921690151561ff0019161761010091151591909102179093558351868152928301919091528183015290517fd51e23eef9e809d9a747dea3a57de48529a943997b52bcea5e136b5a4a80a412916060908290030190a1505050565b600354610100900460ff1615801561066b575060035460ff1615156001145b6106b75760405162461bcd60e51b815260206004820152601e60248201527f566f74696e672073657373696f6e206e6f7420696e2070726f677265737300006044820152606401610133565b6000836040516020016106ca9190610c17565b60405160208183030381529060405280519060200120905060006106ed826108b0565b60208101519091501580156107055750805115156001145b6107465760405162461bcd60e51b81526020600482015260126024820152712ab730baba3437b934bd32b2103b37ba32b960711b6044820152606401610133565b60008054908061075583610bfe565b9091555050604080516060810182528681526020808201879052818301869052600080548152600190915291909120815181906107929082610c82565b50602082015160018201906107a79082610c82565b50604082015160028201906107bc9082610c82565b50505060008281526002602052604090819020805461ff001916610100179055517f55d6abdae2902a81f7b08dea88ada88d6ce858ae8ce1754c62b7dd2d8332d7229061080e90879087908790610d42565b60405180910390a15050505050565b6003546201000090046001600160a01b0316331461084d5760405162461bcd60e51b815260040161013390610b52565b6003805461ff0019166101001790556040517f46dc84b3735f5bde3eda04ed0139966e2cc450589305d827dae93f366be8dc599061019c90602080825260119082015270159bdd19481a185cc8199a5b9a5cda1959607a1b604082015260600190565b60408051808201909152600080825260208201525060009081526002602090815260409182902082518084019093525460ff808216151584526101009091041615159082015290565b60005b838110156109145781810151838201526020016108fc565b50506000910152565b600081518084526109358160208601602086016108f9565b601f01601f19169290920160200192915050565b60006020808301818452808551808352604092508286019150828160051b87010184880160005b838110156109dc57603f198984030185528151606081518186526109968287018261091d565b915050888201518582038a8701526109ae828261091d565b915050878201519150848103888601526109c8818361091d565b968901969450505090860190600101610970565b509098975050505050505050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112610a1157600080fd5b813567ffffffffffffffff80821115610a2c57610a2c6109ea565b604051601f8301601f19908116603f01168101908282118183101715610a5457610a546109ea565b81604052838152866020858801011115610a6d57600080fd5b836020870160208301376000602085830101528094505050505092915050565b600060208284031215610a9f57600080fd5b813567ffffffffffffffff811115610ab657600080fd5b610ac284828501610a00565b949350505050565b600080600060608486031215610adf57600080fd5b833567ffffffffffffffff80821115610af757600080fd5b610b0387838801610a00565b94506020860135915080821115610b1957600080fd5b610b2587838801610a00565b93506040860135915080821115610b3b57600080fd5b50610b4886828701610a00565b9150509250925092565b6020808252601390820152722ab730baba3437b934bd32b21031b0b63632b960691b604082015260600190565b634e487b7160e01b600052601160045260246000fd5b80820180821115610ba857610ba8610b7f565b92915050565b600181811c90821680610bc257607f821691505b602082108103610be257634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052603260045260246000fd5b600060018201610c1057610c10610b7f565b5060010190565b60008251610c298184602087016108f9565b9190910192915050565b601f821115610c7d57600081815260208120601f850160051c81016020861015610c5a5750805b601f850160051c820191505b81811015610c7957828155600101610c66565b5050505b505050565b815167ffffffffffffffff811115610c9c57610c9c6109ea565b610cb081610caa8454610bae565b84610c33565b602080601f831160018114610ce55760008415610ccd5750858301515b600019600386901b1c1916600185901b178555610c79565b600085815260208120601f198616915b82811015610d1457888601518255948401946001909101908401610cf5565b5085821015610d325787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b606081526000610d55606083018661091d565b8281036020840152610d67818661091d565b90508281036040840152610d7b818561091d565b969550505050505056fea26469706673582212209f0888e03caaf3813d0642a3d2d716c933adfce9fc34b136b4eeea509158e44164736f6c63430008130033";

    public static final String FUNC_ADDVOTE = "addVote";

    public static final String FUNC_ADDVOTER = "addVoter";

    public static final String FUNC_ENDVOTE = "endVote";

    public static final String FUNC_GETALLVOTES = "getAllVotes";

    public static final String FUNC_ISVOTEFINISHED = "isVoteFinished";

    public static final String FUNC_ISVOTESTARTED = "isVoteStarted";

    public static final String FUNC_STARTVOTE = "startVote";

    public static final Event VOTECASTED_EVENT = new Event("VoteCasted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTESTARTEDORFINISHED_EVENT = new Event("VoteStartedOrFinished", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTERADDED_EVENT = new Event("VoterAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected Election(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Election(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Election(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Election(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<VoteCastedEventResponse> getVoteCastedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTECASTED_EVENT, transactionReceipt);
        ArrayList<VoteCastedEventResponse> responses = new ArrayList<VoteCastedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteCastedEventResponse typedResponse = new VoteCastedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.publicKey = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.encryptedVote = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.signature = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteCastedEventResponse> voteCastedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteCastedEventResponse>() {
            @Override
            public VoteCastedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTECASTED_EVENT, log);
                VoteCastedEventResponse typedResponse = new VoteCastedEventResponse();
                typedResponse.log = log;
                typedResponse.publicKey = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.encryptedVote = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.signature = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteCastedEventResponse> voteCastedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTECASTED_EVENT));
        return voteCastedEventFlowable(filter);
    }

    public static List<VoteStartedOrFinishedEventResponse> getVoteStartedOrFinishedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTESTARTEDORFINISHED_EVENT, transactionReceipt);
        ArrayList<VoteStartedOrFinishedEventResponse> responses = new ArrayList<VoteStartedOrFinishedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteStartedOrFinishedEventResponse typedResponse = new VoteStartedOrFinishedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteStartedOrFinishedEventResponse> voteStartedOrFinishedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteStartedOrFinishedEventResponse>() {
            @Override
            public VoteStartedOrFinishedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTESTARTEDORFINISHED_EVENT, log);
                VoteStartedOrFinishedEventResponse typedResponse = new VoteStartedOrFinishedEventResponse();
                typedResponse.log = log;
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteStartedOrFinishedEventResponse> voteStartedOrFinishedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTESTARTEDORFINISHED_EVENT));
        return voteStartedOrFinishedEventFlowable(filter);
    }

    public static List<VoterAddedEventResponse> getVoterAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTERADDED_EVENT, transactionReceipt);
        ArrayList<VoterAddedEventResponse> responses = new ArrayList<VoterAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoterAddedEventResponse typedResponse = new VoterAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hashedPublicKey = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.isRegistered = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.hasVoted = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoterAddedEventResponse> voterAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoterAddedEventResponse>() {
            @Override
            public VoterAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTERADDED_EVENT, log);
                VoterAddedEventResponse typedResponse = new VoterAddedEventResponse();
                typedResponse.log = log;
                typedResponse.hashedPublicKey = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.isRegistered = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.hasVoted = (Boolean) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoterAddedEventResponse> voterAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTERADDED_EVENT));
        return voterAddedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addVote(String _publicKey, String _encryptedVote, String _signature) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_publicKey), 
                new org.web3j.abi.datatypes.Utf8String(_encryptedVote), 
                new org.web3j.abi.datatypes.Utf8String(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addVoter(String _publicKey) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDVOTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_publicKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> endVote() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ENDVOTE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getAllVotes() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETALLVOTES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Vote>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isVoteFinished() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISVOTEFINISHED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isVoteStarted() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISVOTESTARTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> startVote() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STARTVOTE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Election load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Election(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Election load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Election(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Election load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Election(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Election load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Election(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Election> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Election.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Election> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Election.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Election> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Election.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Election> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Election.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class Vote extends DynamicStruct {
        public String publicKey;

        public String encryptedVote;

        public String signature;

        public Vote(String publicKey, String encryptedVote, String signature) {
            super(new org.web3j.abi.datatypes.Utf8String(publicKey), 
                    new org.web3j.abi.datatypes.Utf8String(encryptedVote), 
                    new org.web3j.abi.datatypes.Utf8String(signature));
            this.publicKey = publicKey;
            this.encryptedVote = encryptedVote;
            this.signature = signature;
        }

        public Vote(Utf8String publicKey, Utf8String encryptedVote, Utf8String signature) {
            super(publicKey, encryptedVote, signature);
            this.publicKey = publicKey.getValue();
            this.encryptedVote = encryptedVote.getValue();
            this.signature = signature.getValue();
        }

        public String toString() {
            return "Vote [" +
                "publicKey=" + publicKey + ", " +
                "encryptedVote=" + encryptedVote + ", " +
                "signature=" + signature + "]";
        }
    }

    public static class VoteCastedEventResponse extends BaseEventResponse {
        public String publicKey;

        public String encryptedVote;

        public String signature;
    }

    public static class VoteStartedOrFinishedEventResponse extends BaseEventResponse {
        public String message;
    }

    public static class VoterAddedEventResponse extends BaseEventResponse {
        public byte[] hashedPublicKey;

        public Boolean isRegistered;

        public Boolean hasVoted;
    }
}
