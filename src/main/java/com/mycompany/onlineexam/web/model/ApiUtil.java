package com.mycompany.onlineexam.web.model;

import com.mycompany.onlineexam.domain.constants.Constants;
import org.springframework.util.StringUtils;

import java.util.Random;

public class ApiUtil {

    public static ServiceResult getServiceResult(Object data,String message ,  Integer statusCode) {
        return new ServiceResult(data,message ,  statusCode);
    }

    public static String generateRandomCode(String maserCode, Integer codeLength) {
        StringBuilder preCode =
                (StringUtils.isEmpty(maserCode) ? new StringBuilder("") : new StringBuilder(maserCode + Constants.DASH));
        for (int i = 0; i < codeLength; i++) {
            preCode.append(new Random().nextInt(10));
        }
        return preCode.toString();
    }

}
