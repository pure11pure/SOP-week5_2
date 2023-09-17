package com.example.week5_2;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {

    //ชนิดข้อมูลแบบ ArrayList และมีการทำ Generic เป็น String
    public ArrayList<String> badwords =  new ArrayList<String>();
    public ArrayList<String> goodwords =  new ArrayList<String>();
}
