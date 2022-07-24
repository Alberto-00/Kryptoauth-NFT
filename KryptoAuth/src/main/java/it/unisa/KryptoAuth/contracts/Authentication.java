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
    public static final String BINARY = "608060405234801561001057600080fd5b5061001c60003361004c565b6100477f2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96600061005a565b610143565b61005682826100a5565b5050565b600082815260208190526040808220600101805490849055905190918391839186917fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff9190a4505050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff16610056576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556100ff3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b61120a806101526000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c80634209fff1116100c357806391d148541161007c57806391d1485414610316578063985751881461032957806398711c171461033c578063a217fddf1461036b578063d547741f14610373578063db0c87c91461038657600080fd5b80634209fff114610290578063421b2d8b146102a35780634ad407a3146102b6578063645cf80f146102c957806370480275146102fb5780638bad0c0a1461030e57600080fd5b806324d7806c1161011557806324d7806c14610207578063297f7b391461021a5780632f2ff15d1461024957806336568abe1461025c57806338cc48311461026f57806340f1128b1461027d57600080fd5b806301ffc9a71461015257806306f22b041461017a57806309df50601461018d57806313119161146101c1578063248a9ca3146101e4575b600080fd5b610165610160366004610c9c565b610399565b60405190151581526020015b60405180910390f35b610165610188366004610d85565b6103d0565b6101bf61019b366004610df9565b6001600160a01b03166000908152600160205260409020600301805460ff19169055565b005b6101d66000805160206111b583398151915281565b604051908152602001610171565b6101d66101f2366004610e14565b60009081526020819052604090206001015490565b610165610215366004610df9565b6104c4565b610165610228366004610df9565b6001600160a01b031660009081526001602052604090206003015460ff1690565b6101bf610257366004610e2d565b6104d0565b6101bf61026a366004610e2d565b6104fa565b604051338152602001610171565b61016561028b366004610d85565b61057d565b61016561029e366004610df9565b610673565b6101bf6102b1366004610df9565b61068d565b6101656102c4366004610d85565b6106e9565b6101bf6102d7366004610df9565b6001600160a01b03166000908152600260205260409020600301805460ff19169055565b6101bf610309366004610df9565b6107df565b6101bf61081e565b610165610324366004610e2d565b61082b565b6101bf610337366004610df9565b610854565b61016561034a366004610df9565b6001600160a01b031660009081526002602052604090206003015460ff1690565b6101d6600081565b6101bf610381366004610e2d565b61088d565b610165610394366004610d85565b6108b2565b60006001600160e01b03198216637965db0b60e01b14806103ca57506301ffc9a760e01b6001600160e01b03198316145b92915050565b6000816040516020016103e39190610e89565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600190935291206002015414801561048657508260405160200161042e9190610e89565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526001808452939020909361046e93919091019101610edf565b60405160208183030381529060405280519060200120145b156104b957506001600160a01b0383166000908152600160208190526040909120600301805460ff1916821790556104bd565b5060005b9392505050565b60006103ca818361082b565b6000828152602081905260409020600101546104eb816109a9565b6104f583836109b3565b505050565b6001600160a01b038116331461056f5760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b60648201526084015b60405180910390fd5b6105798282610a37565b5050565b6000816040516020016105909190610e89565b6040516020818303038152906040528051906020012060026000866001600160a01b03166001600160a01b031681526020019081526020016000206002015414801561063f5750826040516020016105e89190610e89565b60408051601f1981840301815282825280516020918201206001600160a01b038816600090815260028352929092209192610627926001019101610edf565b60405160208183030381529060405280519060200120145b156104b957506001600160a01b0383166000908152600260205260409020600301805460ff191660019081179091556104bd565b60006103ca6000805160206111b58339815191528361082b565b610696336104c4565b6106b25760405162461bcd60e51b815260040161056690610f55565b6106ca6000805160206111b58339815191528261082b565b6106e6576106e66000805160206111b5833981519152826104d0565b50565b6001600160a01b03838116600090815260026020526040812054909116156107485760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610566565b6001600160a01b038416600081815260026020526040902080546001600160a01b031916909117815560010161077e8482610fd2565b50816040516020016107909190610e89565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526002938490529190912091820155600301805460ff1916600190811790915590509392505050565b6107e8336104c4565b6108045760405162461bcd60e51b815260040161056690610f55565b61080f60008261082b565b6106e6576106e66000826104d0565b6108296000336104fa565b565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b61085d336104c4565b6108795760405162461bcd60e51b815260040161056690610f55565b6106e66000805160206111b5833981519152825b6000828152602081905260409020600101546108a8816109a9565b6104f58383610a37565b6001600160a01b03838116600090815260016020526040812054909116156109115760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610566565b6001600160a01b038416600081815260016020819052604090912080546001600160a01b0319169092178255016109488482610fd2565b508160405160200161095a9190610e89565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600193849052919091206002810191909155600301805460ff19168217905590509392505050565b6106e68133610a9c565b6109bd828261082b565b610579576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556109f33390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b610a41828261082b565b15610579576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b610aa6828261082b565b61057957610abe816001600160a01b03166014610b00565b610ac9836020610b00565b604051602001610ada929190611092565b60408051601f198184030181529082905262461bcd60e51b825261056691600401611107565b60606000610b0f836002611150565b610b1a90600261116f565b67ffffffffffffffff811115610b3257610b32610ce2565b6040519080825280601f01601f191660200182016040528015610b5c576020820181803683370190505b509050600360fc1b81600081518110610b7757610b77611187565b60200101906001600160f81b031916908160001a905350600f60fb1b81600181518110610ba657610ba6611187565b60200101906001600160f81b031916908160001a9053506000610bca846002611150565b610bd590600161116f565b90505b6001811115610c4d576f181899199a1a9b1b9c1cb0b131b232b360811b85600f1660108110610c0957610c09611187565b1a60f81b828281518110610c1f57610c1f611187565b60200101906001600160f81b031916908160001a90535060049490941c93610c468161119d565b9050610bd8565b5083156104bd5760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e746044820152606401610566565b600060208284031215610cae57600080fd5b81356001600160e01b0319811681146104bd57600080fd5b80356001600160a01b0381168114610cdd57600080fd5b919050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112610d0957600080fd5b813567ffffffffffffffff80821115610d2457610d24610ce2565b604051601f8301601f19908116603f01168101908282118183101715610d4c57610d4c610ce2565b81604052838152866020858801011115610d6557600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600060608486031215610d9a57600080fd5b610da384610cc6565b9250602084013567ffffffffffffffff80821115610dc057600080fd5b610dcc87838801610cf8565b93506040860135915080821115610de257600080fd5b50610def86828701610cf8565b9150509250925092565b600060208284031215610e0b57600080fd5b6104bd82610cc6565b600060208284031215610e2657600080fd5b5035919050565b60008060408385031215610e4057600080fd5b82359150610e5060208401610cc6565b90509250929050565b60005b83811015610e74578181015183820152602001610e5c565b83811115610e83576000848401525b50505050565b60008251610e9b818460208701610e59565b9190910192915050565b600181811c90821680610eb957607f821691505b602082108103610ed957634e487b7160e01b600052602260045260246000fd5b50919050565b6000808354610eed81610ea5565b60018281168015610f055760018114610f1a57610f49565b60ff1984168752821515830287019450610f49565b8760005260208060002060005b85811015610f405781548a820152908401908201610f27565b50505082870194505b50929695505050505050565b6020808252601590820152742932b9ba3934b1ba32b2103a379030b236b4b7399760591b604082015260600190565b601f8211156104f557600081815260208120601f850160051c81016020861015610fab5750805b601f850160051c820191505b81811015610fca57828155600101610fb7565b505050505050565b815167ffffffffffffffff811115610fec57610fec610ce2565b61100081610ffa8454610ea5565b84610f84565b602080601f831160018114611035576000841561101d5750858301515b600019600386901b1c1916600185901b178555610fca565b600085815260208120601f198616915b8281101561106457888601518255948401946001909101908401611045565b50858210156110825787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b7f416363657373436f6e74726f6c3a206163636f756e74200000000000000000008152600083516110ca816017850160208801610e59565b7001034b99036b4b9b9b4b733903937b6329607d1b60179184019182015283516110fb816028840160208801610e59565b01602801949350505050565b6020815260008251806020840152611126816040850160208701610e59565b601f01601f19169190910160400192915050565b634e487b7160e01b600052601160045260246000fd5b600081600019048311821515161561116a5761116a61113a565b500290565b600082198211156111825761118261113a565b500190565b634e487b7160e01b600052603260045260246000fd5b6000816111ac576111ac61113a565b50600019019056fe2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96a2646970667358221220ae39fcc98963380af019ea715d05bfc1c49da06bfb8ba769461da9076a4e469f64736f6c634300080f0033";

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

    public RemoteFunctionCall<TransactionReceipt> renounceAdmin() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEADMIN, 
                Arrays.<Type>asList(), 
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
