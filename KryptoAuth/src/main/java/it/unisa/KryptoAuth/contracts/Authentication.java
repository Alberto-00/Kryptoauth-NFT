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
    public static final String BINARY = "608060405234801561001057600080fd5b5061001c600033610021565b6100cd565b61002b828261002f565b5050565b6000828152602081815260408083206001600160a01b038516845290915290205460ff1661002b576000828152602081815260408083206001600160a01b03851684529091529020805460ff191660011790556100893390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b6112bb806100dc6000396000f3fe608060405234801561001057600080fd5b506004361061014d5760003560e01c80634209fff1116100c357806391d148541161007c57806391d1485414610321578063985751881461033457806398711c1714610347578063a217fddf14610376578063d547741f1461037e578063db0c87c91461039157600080fd5b80634209fff114610290578063421b2d8b146102a35780634ad407a3146102b65780635e1fab0f146102c9578063645cf80f146102dc578063704802751461030e57600080fd5b806324d7806c1161011557806324d7806c14610207578063297f7b391461021a5780632f2ff15d1461024957806336568abe1461025c57806338cc48311461026f57806340f1128b1461027d57600080fd5b806301ffc9a71461015257806306f22b041461017a57806309df50601461018d57806313119161146101c1578063248a9ca3146101e4575b600080fd5b610165610160366004610d52565b6103a4565b60405190151581526020015b60405180910390f35b610165610188366004610e36565b6103db565b6101bf61019b366004610eaa565b6001600160a01b03166000908152600160205260409020600301805460ff19169055565b005b6101d660008051602061126683398151915281565b604051908152602001610171565b6101d66101f2366004610ec5565b60009081526020819052604090206001015490565b610165610215366004610eaa565b6104e0565b610165610228366004610eaa565b6001600160a01b031660009081526001602052604090206003015460ff1690565b6101bf610257366004610ede565b6104ec565b6101bf61026a366004610ede565b610516565b604051338152602001610171565b61016561028b366004610e36565b610599565b61016561029e366004610eaa565b6106a0565b6101656102b1366004610eaa565b6106ba565b6101656102c4366004610e36565b61073c565b6101656102d7366004610eaa565b610832565b6101bf6102ea366004610eaa565b6001600160a01b03166000908152600260205260409020600301805460ff19169055565b61016561031c366004610eaa565b610874565b61016561032f366004610ede565b6108bf565b610165610342366004610eaa565b6108e8565b610165610355366004610eaa565b6001600160a01b031660009081526002602052604090206003015460ff1690565b6101d6600081565b6101bf61038c366004610ede565b610940565b61016561039f366004610e36565b610965565b60006001600160e01b03198216637965db0b60e01b14806103d557506301ffc9a760e01b6001600160e01b03198316145b92915050565b60006103e6846106a0565b80156104325750816040516020016103fe9190610f3a565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526001909352912060020154145b80156104a257508260405160200161044a9190610f3a565b60408051601f1981840301815282825280516020918201206001600160a01b03881660009081526001808452939020909361048a93919091019101610f90565b60405160208183030381529060405280519060200120145b156104d557506001600160a01b0383166000908152600160208190526040909120600301805460ff1916821790556104d9565b5060005b9392505050565b60006103d581836108bf565b60008281526020819052604090206001015461050781610a5c565b6105118383610a69565b505050565b6001600160a01b038116331461058b5760405162461bcd60e51b815260206004820152602f60248201527f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560448201526e103937b632b9903337b91039b2b63360891b60648201526084015b60405180910390fd5b6105958282610aed565b5050565b60006105a4846104e0565b80156105fd5750816040516020016105bc9190610f3a565b6040516020818303038152906040528051906020012060026000866001600160a01b03166001600160a01b0316815260200190815260200160002060020154145b801561066c5750826040516020016106159190610f3a565b60408051601f1981840301815282825280516020918201206001600160a01b038816600090815260028352929092209192610654926001019101610f90565b60405160208183030381529060405280519060200120145b156104d557506001600160a01b0383166000908152600260205260409020600301805460ff191660019081179091556104d9565b60006103d5600080516020611266833981519152836108bf565b60006106c5336104e0565b6106e15760405162461bcd60e51b815260040161058290611006565b6106f9600080516020611266833981519152836108bf565b15801561070e575061070c6000836108bf565b155b156107335761072b600080516020611266833981519152836104ec565b506001919050565b5060005b919050565b6001600160a01b038381166000908152600260205260408120549091161561079b5760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610582565b6001600160a01b038416600081815260026020526040902080546001600160a01b03191690911781556001016107d18482611083565b50816040516020016107e39190610f3a565b60408051601f1981840301815291815281516020928301206001600160a01b03871660009081526002938490529190912091820155600301805460ff1916600190811790915590509392505050565b600061083d336104e0565b6108595760405162461bcd60e51b815260040161058290611006565b6108646000836108bf565b156107335761072b600083610516565b600061087f336104e0565b61089b5760405162461bcd60e51b815260040161058290611006565b6108a66000836108bf565b610733576108b3826108e8565b5061072b6000836104ec565b6000918252602082815260408084206001600160a01b0393909316845291905290205460ff1690565b60006108f3336104e0565b61090f5760405162461bcd60e51b815260040161058290611006565b610927600080516020611266833981519152836108bf565b156107335761072b600080516020611266833981519152835b60008281526020819052604090206001015461095b81610a5c565b6105118383610aed565b6001600160a01b03838116600090815260016020526040812054909116156109c45760405162461bcd60e51b8152602060048201526012602482015271185b1c9958591e481c9959da5cdd195c995960721b6044820152606401610582565b6001600160a01b038416600081815260016020819052604090912080546001600160a01b0319169092178255016109fb8482611083565b5081604051602001610a0d9190610f3a565b60408051601f1981840301815291815281516020928301206001600160a01b0387166000908152600193849052919091206002810191909155600301805460ff19168217905590509392505050565b610a668133610b52565b50565b610a7382826108bf565b610595576000828152602081815260408083206001600160a01b03851684529091529020805460ff19166001179055610aa93390565b6001600160a01b0316816001600160a01b0316837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45050565b610af782826108bf565b15610595576000828152602081815260408083206001600160a01b0385168085529252808320805460ff1916905551339285917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45050565b610b5c82826108bf565b61059557610b74816001600160a01b03166014610bb6565b610b7f836020610bb6565b604051602001610b90929190611143565b60408051601f198184030181529082905262461bcd60e51b8252610582916004016111b8565b60606000610bc5836002611201565b610bd0906002611220565b67ffffffffffffffff811115610be857610be8610d93565b6040519080825280601f01601f191660200182016040528015610c12576020820181803683370190505b509050600360fc1b81600081518110610c2d57610c2d611238565b60200101906001600160f81b031916908160001a905350600f60fb1b81600181518110610c5c57610c5c611238565b60200101906001600160f81b031916908160001a9053506000610c80846002611201565b610c8b906001611220565b90505b6001811115610d03576f181899199a1a9b1b9c1cb0b131b232b360811b85600f1660108110610cbf57610cbf611238565b1a60f81b828281518110610cd557610cd5611238565b60200101906001600160f81b031916908160001a90535060049490941c93610cfc8161124e565b9050610c8e565b5083156104d95760405162461bcd60e51b815260206004820181905260248201527f537472696e67733a20686578206c656e67746820696e73756666696369656e746044820152606401610582565b600060208284031215610d6457600080fd5b81356001600160e01b0319811681146104d957600080fd5b80356001600160a01b038116811461073757600080fd5b634e487b7160e01b600052604160045260246000fd5b600082601f830112610dba57600080fd5b813567ffffffffffffffff80821115610dd557610dd5610d93565b604051601f8301601f19908116603f01168101908282118183101715610dfd57610dfd610d93565b81604052838152866020858801011115610e1657600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600060608486031215610e4b57600080fd5b610e5484610d7c565b9250602084013567ffffffffffffffff80821115610e7157600080fd5b610e7d87838801610da9565b93506040860135915080821115610e9357600080fd5b50610ea086828701610da9565b9150509250925092565b600060208284031215610ebc57600080fd5b6104d982610d7c565b600060208284031215610ed757600080fd5b5035919050565b60008060408385031215610ef157600080fd5b82359150610f0160208401610d7c565b90509250929050565b60005b83811015610f25578181015183820152602001610f0d565b83811115610f34576000848401525b50505050565b60008251610f4c818460208701610f0a565b9190910192915050565b600181811c90821680610f6a57607f821691505b602082108103610f8a57634e487b7160e01b600052602260045260246000fd5b50919050565b6000808354610f9e81610f56565b60018281168015610fb65760018114610fcb57610ffa565b60ff1984168752821515830287019450610ffa565b8760005260208060002060005b85811015610ff15781548a820152908401908201610fd8565b50505082870194505b50929695505050505050565b6020808252601590820152742932b9ba3934b1ba32b2103a379030b236b4b7399760591b604082015260600190565b601f82111561051157600081815260208120601f850160051c8101602086101561105c5750805b601f850160051c820191505b8181101561107b57828155600101611068565b505050505050565b815167ffffffffffffffff81111561109d5761109d610d93565b6110b1816110ab8454610f56565b84611035565b602080601f8311600181146110e657600084156110ce5750858301515b600019600386901b1c1916600185901b17855561107b565b600085815260208120601f198616915b82811015611115578886015182559484019460019091019084016110f6565b50858210156111335787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b7f416363657373436f6e74726f6c3a206163636f756e742000000000000000000081526000835161117b816017850160208801610f0a565b7001034b99036b4b9b9b4b733903937b6329607d1b60179184019182015283516111ac816028840160208801610f0a565b01602801949350505050565b60208152600082518060208401526111d7816040850160208701610f0a565b601f01601f19169190910160400192915050565b634e487b7160e01b600052601160045260246000fd5b600081600019048311821515161561121b5761121b6111eb565b500290565b60008219821115611233576112336111eb565b500190565b634e487b7160e01b600052603260045260246000fd5b60008161125d5761125d6111eb565b50600019019056fe2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96a264697066735822122021cb316c5b40c4dd93b750a46930ba3082e7d9a07f39619c304ab21a16ed3c9064736f6c634300080f0033";

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
