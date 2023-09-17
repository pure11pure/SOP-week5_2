package com.example.week5_2;

import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable {

    //ชนิดข้อมูลแบบ ArrayList และมีการทำ Generic เป็น String
    public ArrayList<String> badsentences =  new ArrayList<String>();
    public ArrayList<String> goodsentences =  new ArrayList<String>();
}
