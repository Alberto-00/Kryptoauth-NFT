package it.unisa.KryptoAuth.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
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
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class Authentication extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5061001c60003361004c565b6100477f2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96600061005a565b610143565b61005682826100a5565b5050565b600082815260208190526040808220600101805490849055905190918391839186917fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff9190a4505050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff16610056576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556100ff3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b610f70806101526000396000f3fe608060405234801561001057600080fd5b50600436106101165760003560e01c80634209fff1116100a257806391d148541161007157806391d14854146102445780639857518814610257578063a217fddf1461026a578063d547741f14610272578063db0c87c91461028557600080fd5b80634209fff1146101f8578063421b2d8b1461020b5780635e1fab0f1461021e578063704802751461023157600080fd5b806324d7806c116100e957806324d7806c1461019c5780632f2ff15d146101af57806336568abe146101c457806338cc4831146101d757806340f1128b146101e557600080fd5b806301ffc9a71461011b57806306f22b04146101435780631311916114610156578063248a9ca314610179575b600080fd5b61012e610129366004610a07565b610298565b60405190151581526020015b60405180910390f35b61012e610151366004610aeb565b6102cf565b61016b600080516020610f1b83398151915281565b60405190815260200161013a565b61016b610187366004610b5f565b60009081526020819052604090206001015490565b61012e6101aa366004610b78565b6103ae565b6101c26101bd366004610b93565b6103ba565b005b6101c26101d2366004610b93565b6103e4565b60405133815260200161013a565b61012e6101f3366004610aeb565b610467565b61012e610206366004610b78565b610472565b61012e610219366004610b78565b61048c565b61012e61022c366004610b78565b6104f8565b61012e61023f366004610b78565b61053a565b61012e610252366004610b93565b610585565b61012e610265366004610b78565b6105ae565b61016b600081565b6101c2610280366004610b93565b610606565b61012e610293366004610aeb565b61062b565b60006001600160e01b03198216637965db0b60e01b14806102c957506301ffc9a760e01b6001600160e01b03198316145b92915050565b60006102da84610472565b80156103265750816040516020016102f29190610bef565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526001909352912060020154145b801561039657508260405160200161033e9190610bef565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526001808452939020909361037e93919091019101610c45565b60405160208183030381529060405280519060200120145b156103a3575060016103a7565b5060005b9392505050565b60006102c98183610585565b6000828152602081905260409020600101546103d581610711565b6103df838361071e565b505050565b6001600160a01b03811633146104595760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b60648201526084015b60405180910390fd5b61046382826107a2565b5050565b60006102da846103ae565b60006102c9600080516020610f1b83398151915283610585565b6000610497336103ae565b6104b35760405162461bcd60e51b815260040161045090610cbb565b6104cb600080516020610f1b83398151915283610585565b6104ef576104e7600080516020610f1b833981519152836103ba565b506001919050565b5060005b919050565b6000610503336103ae565b61051f5760405162461bcd60e51b815260040161045090610cbb565b61052a600083610585565b156104ef576104e76000836103e4565b6000610545336103ae565b6105615760405162461bcd60e51b815260040161045090610cbb565b61056c600083610585565b6104ef57610579826105ae565b506104e76000836103ba565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b60006105b9336103ae565b6105d55760405162461bcd60e51b815260040161045090610cbb565b6105ed600080516020610f1b83398151915283610585565b156104ef576104e7600080516020610f1b833981519152835b60008281526020819052604090206001015461062181610711565b6103df83836107a2565b6001600160a01b038084166000818152600160205260408120549092160361068a5760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610450565b6001600160a01b038416600081815260016020819052604090912080546001600160a01b0319169092178255016106c18482610d38565b50816040516020016106d39190610bef565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600193849052919091206002015590509392505050565b61071b8133610807565b50565b6107288282610585565b610463576000828152602081815260408083206001600160a01b03851684529091529020805460ff1916600117905561075e3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b6107ac8282610585565b15610463576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b6108118282610585565b61046357610829816001600160a01b0316601461086b565b61083483602061086b565b604051602001610845929190610df8565b60408051601f198184030181529082905262461bcd60e51b825261045091600401610e6d565b6060600061087a836002610eb6565b610885906002610ed5565b67ffffffffffffffff81111561089d5761089d610a48565b6040519080825280601f01601f1916602001820160405280156108c7576020820181803683370190505b509050600360fc1b816000815181106108e2576108e2610eed565b60200101906001600160f81b031916908160001a905350600f60fb1b8160018151811061091157610911610eed565b60200101906001600160f81b031916908160001a9053506000610935846002610eb6565b610940906001610ed5565b90505b60018111156109b8576f181899199a1a9b1b9c1cb0b131b232b360811b85600f166010811061097457610974610eed565b1a60f81b82828151811061098a5761098a610eed565b60200101906001600160f81b031916908160001a90535060049490941c936109b181610f03565b9050610943565b5083156103a75760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e746044820152606401610450565b600060208284031215610a1957600080fd5b81356001600160e01b0319811681146103a757600080fd5b80356001600160a01b03811681146104f357600080fd5b634e487b7160e01b600052604160045260246000fd5b600082601f830112610a6f57600080fd5b813567ffffffffffffffff80821115610a8a57610a8a610a48565b604051601f8301601f19908116603f01168101908282118183101715610ab257610ab2610a48565b81604052838152866020858801011115610acb57600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600060608486031215610b0057600080fd5b610b0984610a31565b9250602084013567ffffffffffffffff80821115610b2657600080fd5b610b3287838801610a5e565b93506040860135915080821115610b4857600080fd5b50610b5586828701610a5e565b9150509250925092565b600060208284031215610b7157600080fd5b5035919050565b600060208284031215610b8a57600080fd5b6103a782610a31565b60008060408385031215610ba657600080fd5b82359150610bb660208401610a31565b90509250929050565b60005b83811015610bda578181015183820152602001610bc2565b83811115610be9576000848401525b50505050565b60008251610c01818460208701610bbf565b9190910192915050565b600181811c90821680610c1f57607f821691505b602082108103610c3f57634e487b7160e01b600052602260045260246000fd5b50919050565b6000808354610c5381610c0b565b60018281168015610c6b5760018114610c8057610caf565b60ff1984168752821515830287019450610caf565b8760005260208060002060005b85811015610ca65781548a820152908401908201610c8d565b50505082870194505b50929695505050505050565b6020808252601590820152742932b9ba3934b1ba32b2103a379030b236b4b7399760591b604082015260600190565b601f8211156103df57600081815260208120601f850160051c81016020861015610d115750805b601f850160051c820191505b81811015610d3057828155600101610d1d565b505050505050565b815167ffffffffffffffff811115610d5257610d52610a48565b610d6681610d608454610c0b565b84610cea565b602080601f831160018114610d9b5760008415610d835750858301515b600019600386901b1c1916600185901b178555610d30565b600085815260208120601f198616915b82811015610dca57888601518255948401946001909101908401610dab565b5085821015610de85787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b7f416363657373436f6e74726f6c3a206163636f756e7420000000000000000000815260008351610e30816017850160208801610bbf565b7001034b99036b4b9b9b4b733903937b6329607d1b6017918401918201528351610e61816028840160208801610bbf565b01602801949350505050565b6020815260008251806020840152610e8c816040850160208701610bbf565b601f01601f19169190910160400192915050565b634e487b7160e01b600052601160045260246000fd5b6000816000190483118215151615610ed057610ed0610ea0565b500290565b60008219821115610ee857610ee8610ea0565b500190565b634e487b7160e01b600052603260045260246000fd5b600081610f1257610f12610ea0565b50600019019056fe2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96a26469706673582212205c87cd12f0628dea12aafd16bb53b7441e4449de6a7b544e45fd2d6eee335dfe64736f6c634300080f0033";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_USER_ROLE = "USER_ROLE";

    public static final String FUNC_ADDADMIN = "addAdmin";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_GETADDRESS = "getAddress";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISUSER = "isUser";

    public static final String FUNC_LOGINADMIN = "loginAdmin";

    public static final String FUNC_LOGINUSER = "loginUser";

    public static final String FUNC_REGISTERUSER = "registerUser";

    public static final String FUNC_REMOVEUSER = "removeUser";

    public static final String FUNC_RENOUNCEADMIN = "renounceAdmin";

    public static final String FUNC_RENOUNCEROLE = "renounceRole";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final Event ROLEADMINCHANGED_EVENT = new Event("RoleAdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event ROLEGRANTED_EVENT = new Event("RoleGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ROLEREVOKED_EVENT = new Event("RoleRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected Authentication(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Authentication(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Authentication(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Authentication(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<RoleAdminChangedEventResponse> getRoleAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<RoleAdminChangedEventResponse> responses = new ArrayList<RoleAdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleAdminChangedEventResponse>() {
            @Override
            public RoleAdminChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEADMINCHANGED_EVENT, log);
                RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEADMINCHANGED_EVENT));
        return roleAdminChangedEventFlowable(filter);
    }

    public List<RoleGrantedEventResponse> getRoleGrantedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEGRANTED_EVENT, transactionReceipt);
        ArrayList<RoleGrantedEventResponse> responses = new ArrayList<RoleGrantedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleGrantedEventResponse>() {
            @Override
            public RoleGrantedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEGRANTED_EVENT, log);
                RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEGRANTED_EVENT));
        return roleGrantedEventFlowable(filter);
    }

    public List<RoleRevokedEventResponse> getRoleRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEREVOKED_EVENT, transactionReceipt);
        ArrayList<RoleRevokedEventResponse> responses = new ArrayList<RoleRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleRevokedEventResponse>() {
            @Override
            public RoleRevokedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEREVOKED_EVENT, log);
                RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEREVOKED_EVENT));
        return roleRevokedEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> DEFAULT_ADMIN_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEFAULT_ADMIN_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> USER_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_USER_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> addAdmin(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addUser(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> getRoleAdmin(byte[] role) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROLEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GRANTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> hasRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isAdmin(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isUser(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> loginAdmin(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LOGINADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> loginUser(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LOGINUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerUser(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTERUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeUser(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceAdmin(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REVOKEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(160, account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static Authentication load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Authentication(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Authentication load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Authentication(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Authentication load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Authentication(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Authentication load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Authentication(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Authentication> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Authentication.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Authentication> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Authentication.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Authentication> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Authentication.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Authentication> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Authentication.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class RoleAdminChangedEventResponse extends BaseEventResponse {
        public byte[] role;

        public byte[] previousAdminRole;

        public byte[] newAdminRole;
    }

    public static class RoleGrantedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class RoleRevokedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }
}
