package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexPhone(String target){
        String regex = "^[0-9]{9,11}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexPassword(String target){
        String lenReg = "[a-zA-Z0-9!@#$%^&*]{8,20}";
        String engReg = "[a-zA-Z]";
        String numReg = "[0-9]";
        String specReg = "[!@#$%^&*]";

        int has =0;
        if(Pattern.matches(lenReg, target)){
            has+=Pattern.compile(engReg).matcher(target).find() ? 1:0;
            has+=Pattern.compile(numReg).matcher(target).find() ? 1:0;
            has+=Pattern.compile(specReg).matcher(target).find() ? 1:0;

            if(has < 2){
                //System.out.println("영문,숫자,특수문자(!@#$%^&*) 중 두 가지 이상이 조합하여 입력하세요");
                return false;
            }else{
              //  System.out.println("적합한 비번입니다");
                return true;
            }
        }else{
            //System.out.println("영문,숫자,특수문자(!@#$%^&*)로 8자에서 20자 입력하세요");
            return false;
        }
    }

    //core 추가
    public static boolean isValidCVCNumber(String str)
    {
        // Regex to check valid CVV number.
        String regex = "^[0-9]{3,4}$"; //0에서 9로 이루어진 3자리인지 확인하는 작업

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null)
        {
            return false;
        }

        // Find match between given string
        // and regular expression
        // using Pattern.matcher()

        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    //core추가 -> 올바른 카드 번호 입력 방식인지 확인
    public static boolean validCardNumber(String number) {
        Pattern pattern = Pattern.compile("\\d{4} \\d{4} \\d{4} \\d{4}");
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    //core 추가 -> 두 자리의 올바른 숫자를 입력했는지 확인
    public static boolean isValidPeriod(String str)
    {
        // Regex to check valid CVV number.
        String regex = "^[0-9]{2}$"; //0에서 9로 이루어진 3자리인지 확인하는 작업

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null)
        {
            return false;
        }

        // Find match between given string
        // and regular expression
        // using Pattern.matcher()

        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

    //core 추가 -> 숫자만 입력했는지 확인
    public static boolean isOnlyNumber(String str)
    {
        String regex = "[0-9]+";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }
}

