package com.example.spot.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ReturnMessage {
    BAD_WORD,
    CAN,
    CANT;

    static public Map<String,String> apiReturnMessageSetting(ReturnMessage returnMessage){
        Map<String, String> result = new HashMap<>();
        result.put("message",returnMessage.name());

        return result;
    }
}
