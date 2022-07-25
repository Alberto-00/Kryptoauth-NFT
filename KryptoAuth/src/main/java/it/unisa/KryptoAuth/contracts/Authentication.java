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
    public static final String BINARY = "608060405234801561001057600080fd5b5061001c60003361004c565b6100477f2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96600061005a565b610143565b61005682826100a5565b5050565b600082815260208190526040808220600101805490849055905190918391839186917fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff9190a4505050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff16610056576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556100ff3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b61129b806101526000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c80634209fff1116100c357806391d148541161007c57806391d1485414610321578063985751881461033457806398711c1714610347578063a217fddf14610376578063d547741f1461037e578063db0c87c91461039157600080fd5b80634209fff114610290578063421b2d8b146102a35780634ad407a3146102b65780635e1fab0f146102c9578063645cf80f146102dc578063704802751461030e57600080fd5b806324d7806c1161011557806324d7806c14610207578063297f7b391461021a5780632f2ff15d1461024957806336568abe1461025c57806338cc48311461026f57806340f1128b1461027d57600080fd5b806301ffc9a71461015257806306f22b041461017a57806309df50601461018d57806313119161146101c1578063248a9ca3146101e4575b600080fd5b610165610160366004610d32565b6103a4565b60405190151581526020015b60405180910390f35b610165610188366004610e16565b6103db565b6101bf61019b366004610e8a565b6001600160a01b03166000908152600160205260409020600301805460ff19169055565b005b6101d660008051602061124683398151915281565b604051908152602001610171565b6101d66101f2366004610ea5565b60009081526020819052604090206001015490565b610165610215366004610e8a565b6104e0565b610165610228366004610e8a565b6001600160a01b031660009081526001602052604090206003015460ff1690565b6101bf610257366004610ebe565b6104ec565b6101bf61026a366004610ebe565b610516565b604051338152602001610171565b61016561028b366004610e16565b610599565b61016561029e366004610e8a565b6106a0565b6101656102b1366004610e8a565b6106ba565b6101656102c4366004610e16565b610726565b6101656102d7366004610e8a565b61081c565b6101bf6102ea366004610e8a565b6001600160a01b03166000908152600260205260409020600301805460ff19169055565b61016561031c366004610e8a565b61085e565b61016561032f366004610ebe565b61089f565b610165610342366004610e8a565b6108c8565b610165610355366004610e8a565b6001600160a01b031660009081526002602052604090206003015460ff1690565b6101d6600081565b6101bf61038c366004610ebe565b610920565b61016561039f366004610e16565b610945565b60006001600160e01b03198216637965db0b60e01b14806103d557506301ffc9a760e01b6001600160e01b03198316145b92915050565b60006103e6846106a0565b80156104325750816040516020016103fe9190610f1a565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526001909352912060020154145b80156104a257508260405160200161044a9190610f1a565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526001808452939020909361048a93919091019101610f70565b60405160208183030381529060405280519060200120145b156104d557506001600160a01b0383166000908152600160208190526040909120600301805460ff1916821790556104d9565b5060005b9392505050565b60006103d5818361089f565b60008281526020819052604090206001015461050781610a3c565b6105118383610a49565b505050565b6001600160a01b038116331461058b5760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b60648201526084015b60405180910390fd5b6105958282610acd565b5050565b60006105a4846104e0565b80156105fd5750816040516020016105bc9190610f1a565b6040516020818303038152906040528051906020012060026000866001600160a01b03166001600160a01b0316815260200190815260200160002060020154145b801561066c5750826040516020016106159190610f1a565b60408051601f1981840301815282825280516020918201206001600160a01b038816600090815260028352929092209192610654926001019101610f70565b60405160208183030381529060405280519060200120145b156104d557506001600160a01b0383166000908152600260205260409020600301805460ff191660019081179091556104d9565b60006103d56000805160206112468339815191528361089f565b60006106c5336104e0565b6106e15760405162461bcd60e51b815260040161058290610fe6565b6106f96000805160206112468339815191528361089f565b61071d57610715600080516020611246833981519152836104ec565b506001919050565b5060005b919050565b6001600160a01b03838116600090815260026020526040812054909116156107855760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610582565b6001600160a01b038416600081815260026020526040902080546001600160a01b03191690911781556001016107bb8482611063565b50816040516020016107cd9190610f1a565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526002938490529190912091820155600301805460ff1916600190811790915590509392505050565b6000610827336104e0565b6108435760405162461bcd60e51b815260040161058290610fe6565b61084e60008361089f565b1561071d57610715600083610516565b6000610869336104e0565b6108855760405162461bcd60e51b815260040161058290610fe6565b61089060008361089f565b61071d576107156000836104ec565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b60006108d3336104e0565b6108ef5760405162461bcd60e51b815260040161058290610fe6565b6109076000805160206112468339815191528361089f565b1561071d57610715600080516020611246833981519152835b60008281526020819052604090206001015461093b81610a3c565b6105118383610acd565b6001600160a01b03838116600090815260016020526040812054909116156109a45760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610582565b6001600160a01b038416600081815260016020819052604090912080546001600160a01b0319169092178255016109db8482611063565b50816040516020016109ed9190610f1a565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600193849052919091206002810191909155600301805460ff19168217905590509392505050565b610a468133610b32565b50565b610a53828261089f565b610595576000828152602081815260408083206001600160a01b03851684529091529020805460ff19166001179055610a893390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b610ad7828261089f565b15610595576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b610b3c828261089f565b61059557610b54816001600160a01b03166014610b96565b610b5f836020610b96565b604051602001610b70929190611123565b60408051601f198184030181529082905262461bcd60e51b825261058291600401611198565b60606000610ba58360026111e1565b610bb0906002611200565b67ffffffffffffffff811115610bc857610bc8610d73565b6040519080825280601f01601f191660200182016040528015610bf2576020820181803683370190505b509050600360fc1b81600081518110610c0d57610c0d611218565b60200101906001600160f81b031916908160001a905350600f60fb1b81600181518110610c3c57610c3c611218565b60200101906001600160f81b031916908160001a9053506000610c608460026111e1565b610c6b906001611200565b90505b6001811115610ce3576f181899199a1a9b1b9c1cb0b131b232b360811b85600f1660108110610c9f57610c9f611218565b1a60f81b828281518110610cb557610cb5611218565b60200101906001600160f81b031916908160001a90535060049490941c93610cdc8161122e565b9050610c6e565b5083156104d95760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e746044820152606401610582565b600060208284031215610d4457600080fd5b81356001600160e01b0319811681146104d957600080fd5b80356001600160a01b038116811461072157600080fd5b634e487b7160e01b600052604160045260246000fd5b600082601f830112610d9a57600080fd5b813567ffffffffffffffff80821115610db557610db5610d73565b604051601f8301601f19908116603f01168101908282118183101715610ddd57610ddd610d73565b81604052838152866020858801011115610df657600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600060608486031215610e2b57600080fd5b610e3484610d5c565b9250602084013567ffffffffffffffff80821115610e5157600080fd5b610e5d87838801610d89565b93506040860135915080821115610e7357600080fd5b50610e8086828701610d89565b9150509250925092565b600060208284031215610e9c57600080fd5b6104d982610d5c565b600060208284031215610eb757600080fd5b5035919050565b60008060408385031215610ed157600080fd5b82359150610ee160208401610d5c565b90509250929050565b60005b83811015610f05578181015183820152602001610eed565b83811115610f14576000848401525b50505050565b60008251610f2c818460208701610eea565b9190910192915050565b600181811c90821680610f4a57607f821691505b602082108103610f6a57634e487b7160e01b600052602260045260246000fd5b50919050565b6000808354610f7e81610f36565b60018281168015610f965760018114610fab57610fda565b60ff1984168752821515830287019450610fda565b8760005260208060002060005b85811015610fd15781548a820152908401908201610fb8565b50505082870194505b50929695505050505050565b6020808252601590820152742932b9ba3934b1ba32b2103a379030b236b4b7399760591b604082015260600190565b601f82111561051157600081815260208120601f850160051c8101602086101561103c5750805b601f850160051c820191505b8181101561105b57828155600101611048565b505050505050565b815167ffffffffffffffff81111561107d5761107d610d73565b6110918161108b8454610f36565b84611015565b602080601f8311600181146110c657600084156110ae5750858301515b600019600386901b1c1916600185901b17855561105b565b600085815260208120601f198616915b828110156110f5578886015182559484019460019091019084016110d6565b50858210156111135787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b7f416363657373436f6e74726f6c3a206163636f756e742000000000000000000081526000835161115b816017850160208801610eea565b7001034b99036b4b9b9b4b733903937b6329607d1b601791840191820152835161118c816028840160208801610eea565b01602801949350505050565b60208152600082518060208401526111b7816040850160208701610eea565b601f01601f19169190910160400192915050565b634e487b7160e01b600052601160045260246000fd5b60008160001904831182151516156111fb576111fb6111cb565b500290565b60008219821115611213576112136111cb565b500190565b634e487b7160e01b600052603260045260246000fd5b60008161123d5761123d6111cb565b50600019019056fe2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96a2646970667358221220e77a44f602842cc8096d22e9769251d6d6deef5f0c0c59cd196a98dc03b95fb564736f6c634300080f0033";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_USER_ROLE = "USER_ROLE";

    public static final String FUNC_ADDADMIN = "addAdmin";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_CHECKISADMINLOGGED = "checkIsAdminLogged";

    public static final String FUNC_CHECKISUSERLOGGED = "checkIsUserLogged";

    public static final String FUNC_GETADDRESS = "getAddress";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_ISADMIN = "isAdmin";

    public static final String FUNC_ISUSER = "isUser";

    public static final String FUNC_LOGINADMIN = "loginAdmin";

    public static final String FUNC_LOGINUSER = "loginUser";

    public static final String FUNC_LOGOUTADMIN = "logoutAdmin";

    public static final String FUNC_LOGOUTUSER = "logoutUser";

    public static final String FUNC_REGISTERADMIN = "registerAdmin";

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

    public RemoteFunctionCall<Boolean> checkIsAdminLogged(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKISADMINLOGGED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> checkIsUserLogged(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKISUSERLOGGED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteFunctionCall<TransactionReceipt> loginAdmin(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOGINADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> loginUser(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOGINUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logoutAdmin(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOGOUTADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> logoutUser(String _address) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LOGOUTUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> registerAdmin(String _address, String _name, String _password) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REGISTERADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _address), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_password)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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
