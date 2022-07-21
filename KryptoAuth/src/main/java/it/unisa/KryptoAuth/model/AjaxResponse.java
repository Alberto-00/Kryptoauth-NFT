package it.unisa.KryptoAuth.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class AjaxResponse {

    private final Map<String, String> msgError;

    public AjaxResponse() {
        msgError = new LinkedHashMap<>();
    }

    public void addMsg(String key, String msg){
        this.msgError.put(key, msg);
    }

    public void cleanList(){
        this.msgError.clear();
    }

    public Map<String, String> getMsgError() {
        return msgError;
    }
}
