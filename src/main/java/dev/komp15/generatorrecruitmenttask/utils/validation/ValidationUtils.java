package dev.komp15.generatorrecruitmenttask.utils.validation;

public class ValidationUtils {

    public static long getMaxJobSize(Character[] chars){
        int result = 1;
        for(int i = 0; i < chars.length; i++){
            result *= i;
        }
        return result;
    }
}
