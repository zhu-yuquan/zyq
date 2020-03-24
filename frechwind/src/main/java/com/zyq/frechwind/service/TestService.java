package com.zyq.frechwind.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    public List<String> numberList(){
        List<String> numberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numberList.add(String.valueOf(i));
        }
        return numberList;
    }
}
