package it.unisa.KryptoAuth.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
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
    public static final String BINARY = "60806040523480156200001157600080fd5b50604051620013b8380380620013b8833981016040819052620000349162000170565b6200004160008262000075565b6200006e7f2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96600062000085565b50620001a2565b620000818282620000d0565b5050565b600082815260208190526040808220600101805490849055905190918391839186917fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff9190a4505050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff1662000081576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556200012c3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b6000602082840312156200018357600080fd5b81516001600160a01b03811681146200019b57600080fd5b9392505050565b61120680620001b26000396000f3fe608060405234801561001057600080fd5b50600436106101425760003560e01c8063421b2d8b116100b857806391d148541161007c57806391d14854146102fd578063985751881461031057806398711c1714610323578063a217fddf14610352578063d547741f1461035a578063db0c87c91461036d57600080fd5b8063421b2d8b1461028a5780634ad407a31461029d578063645cf80f146102b057806370480275146102e25780638bad0c0a146102f557600080fd5b806324d7806c1161010a57806324d7806c146101fc578063297f7b391461020f5780632f2ff15d1461023e57806336568abe1461025157806340f1128b146102645780634209fff11461027757600080fd5b806301ffc9a71461014757806306f22b041461016f57806309df50601461018257806313119161146101b6578063248a9ca3146101d9575b600080fd5b61015a610155366004610c98565b610380565b60405190151581526020015b60405180910390f35b61015a61017d366004610d81565b6103b7565b6101b4610190366004610df5565b6001600160a01b03166000908152600160205260409020600301805460ff19169055565b005b6101cb6000805160206111b183398151915281565b604051908152602001610166565b6101cb6101e7366004610e10565b60009081526020819052604090206001015490565b61015a61020a366004610df5565b6104ab565b61015a61021d366004610df5565b6001600160a01b031660009081526001602052604090206003015460ff1690565b6101b461024c366004610e29565b6104b7565b6101b461025f366004610e29565b6104e1565b61015a610272366004610d81565b610564565b61015a610285366004610df5565b61065a565b6101b4610298366004610df5565b610674565b61015a6102ab366004610d81565b6106e5565b6101b46102be366004610df5565b6001600160a01b03166000908152600260205260409020600301805460ff19169055565b6101b46102f0366004610df5565b6107db565b6101b461081a565b61015a61030b366004610e29565b610827565b6101b461031e366004610df5565b610850565b61015a610331366004610df5565b6001600160a01b031660009081526002602052604090206003015460ff1690565b6101cb600081565b6101b4610368366004610e29565b610889565b61015a61037b366004610d81565b6108ae565b60006001600160e01b03198216637965db0b60e01b14806103b157506301ffc9a760e01b6001600160e01b03198316145b92915050565b6000816040516020016103ca9190610e85565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600190935291206002015414801561046d5750826040516020016104159190610e85565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526001808452939020909361045593919091019101610edb565b60405160208183030381529060405280519060200120145b156104a057506001600160a01b0383166000908152600160208190526040909120600301805460ff1916821790556104a4565b5060005b9392505050565b60006103b18183610827565b6000828152602081905260409020600101546104d2816109a5565b6104dc83836109af565b505050565b6001600160a01b03811633146105565760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b60648201526084015b60405180910390fd5b6105608282610a33565b5050565b6000816040516020016105779190610e85565b6040516020818303038152906040528051906020012060026000866001600160a01b03166001600160a01b03168152602001908152602001600020600201541480156106265750826040516020016105cf9190610e85565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526002835292909220919261060e926001019101610edb565b60405160208183030381529060405280519060200120145b156104a057506001600160a01b0383166000908152600260205260409020600301805460ff191660019081179091556104a4565b60006103b16000805160206111b183398151915283610827565b61067d336104ab565b6106995760405162461bcd60e51b815260040161054d90610f51565b6106a4600082610827565b15806106c557506106c36000805160206111b183398151915282610827565b155b156106e2576106e26000805160206111b1833981519152826104b7565b50565b6001600160a01b03838116600090815260026020526040812054909116156107445760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b604482015260640161054d565b6001600160a01b038416600081815260026020526040902080546001600160a01b031916909117815560010161077a8482610fce565b508160405160200161078c9190610e85565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526002938490529190912091820155600301805460ff1916600190811790915590509392505050565b6107e4336104ab565b6108005760405162461bcd60e51b815260040161054d90610f51565b61080b600082610827565b6106e2576106e26000826104b7565b6108256000336104e1565b565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b610859336104ab565b6108755760405162461bcd60e51b815260040161054d90610f51565b6106e26000805160206111b1833981519152825b6000828152602081905260409020600101546108a4816109a5565b6104dc8383610a33565b6001600160a01b038381166000908152600160205260408120549091161561090d5760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b604482015260640161054d565b6001600160a01b038416600081815260016020819052604090912080546001600160a01b0319169092178255016109448482610fce565b50816040516020016109569190610e85565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600193849052919091206002810191909155600301805460ff19168217905590509392505050565b6106e28133610a98565b6109b98282610827565b610560576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556109ef3390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b610a3d8282610827565b15610560576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b610aa28282610827565b61056057610aba816001600160a01b03166014610afc565b610ac5836020610afc565b604051602001610ad692919061108e565b60408051601f198184030181529082905262461bcd60e51b825261054d91600401611103565b60606000610b0b83600261114c565b610b1690600261116b565b67ffffffffffffffff811115610b2e57610b2e610cde565b6040519080825280601f01601f191660200182016040528015610b58576020820181803683370190505b509050600360fc1b81600081518110610b7357610b73611183565b60200101906001600160f81b031916908160001a905350600f60fb1b81600181518110610ba257610ba2611183565b60200101906001600160f81b031916908160001a9053506000610bc684600261114c565b610bd190600161116b565b90505b6001811115610c49576f181899199a1a9b1b9c1cb0b131b232b360811b85600f1660108110610c0557610c05611183565b1a60f81b828281518110610c1b57610c1b611183565b60200101906001600160f81b031916908160001a90535060049490941c93610c4281611199565b9050610bd4565b5083156104a45760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e74604482015260640161054d565b600060208284031215610caa57600080fd5b81356001600160e01b0319811681146104a457600080fd5b80356001600160a01b0381168114610cd957600080fd5b919050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112610d0557600080fd5b813567ffffffffffffffff80821115610d2057610d20610cde565b604051601f8301601f19908116603f01168101908282118183101715610d4857610d48610cde565b81604052838152866020858801011115610d6157600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600060608486031215610d9657600080fd5b610d9f84610cc2565b9250602084013567ffffffffffffffff80821115610dbc57600080fd5b610dc887838801610cf4565b93506040860135915080821115610dde57600080fd5b50610deb86828701610cf4565b9150509250925092565b600060208284031215610e0757600080fd5b6104a482610cc2565b600060208284031215610e2257600080fd5b5035919050565b60008060408385031215610e3c57600080fd5b82359150610e4c60208401610cc2565b90509250929050565b60005b83811015610e70578181015183820152602001610e58565b83811115610e7f576000848401525b50505050565b60008251610e97818460208701610e55565b9190910192915050565b600181811c90821680610eb557607f821691505b602082108103610ed557634e487b7160e01b600052602260045260246000fd5b50919050565b6000808354610ee981610ea1565b60018281168015610f015760018114610f1657610f45565b60ff1984168752821515830287019450610f45565b8760005260208060002060005b85811015610f3c5781548a820152908401908201610f23565b50505082870194505b50929695505050505050565b6020808252601590820152742932b9ba3934b1ba32b2103a379030b236b4b7399760591b604082015260600190565b601f8211156104dc57600081815260208120601f850160051c81016020861015610fa75750805b601f850160051c820191505b81811015610fc657828155600101610fb3565b505050505050565b815167ffffffffffffffff811115610fe857610fe8610cde565b610ffc81610ff68454610ea1565b84610f80565b602080601f83116001811461103157600084156110195750858301515b600019600386901b1c1916600185901b178555610fc6565b600085815260208120601f198616915b8281101561106057888601518255948401946001909101908401611041565b508582101561107e5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b7f416363657373436f6e74726f6c3a206163636f756e74200000000000000000008152600083516110c6816017850160208801610e55565b7001034b99036b4b9b9b4b733903937b6329607d1b60179184019182015283516110f7816028840160208801610e55565b01602801949350505050565b6020815260008251806020840152611122816040850160208701610e55565b601f01601f19169190910160400192915050565b634e487b7160e01b600052601160045260246000fd5b600081600019048311821515161561116657611166611136565b500290565b6000821982111561117e5761117e611136565b500190565b634e487b7160e01b600052603260045260246000fd5b6000816111a8576111a8611136565b50600019019056fe2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96a2646970667358221220944ae25792999bfaedb0e0499e976b3cd07670abf8d11f6274f0ddcf8175382664736f6c634300080f0033";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_USER_ROLE = "USER_ROLE";

    public static final String FUNC_ADDADMIN = "addAdmin";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC_CHECKISADMINLOGGED = "checkIsAdminLogged";

    public static final String FUNC_CHECKISUSERLOGGED = "checkIsUserLogged";

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

    public static RemoteCall<Authentication> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String root) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, root)));
        return deployRemoteCall(Authentication.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Authentication> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String root) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, root)));
        return deployRemoteCall(Authentication.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Authentication> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String root) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, root)));
        return deployRemoteCall(Authentication.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Authentication> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String root) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, root)));
        return deployRemoteCall(Authentication.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
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
