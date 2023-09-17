package com.example.week5_2;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    protected Word words;


    public WordPublisher() {
        this.words = new Word();
        this.words.badwords.add("fuck");
        this.words.badwords.add("olo");
        this.words.goodwords.add("happy");
        this.words.goodwords.add("enjoy");
        this.words.goodwords.add("like");
    }

    @RequestMapping(value = "/addBad/{word}" , method = RequestMethod.GET)
    public ArrayList<String> addBadWord(@PathVariable("word") String word){
        this.words.badwords.add(word);
        return this.words.badwords;
    }

    @RequestMapping(value = "/delBad/{word}" , method = RequestMethod.GET)
    public ArrayList<String>  deleteBadWord(@PathVariable("word") String word){
        for(int i = 0; i < this.words.badwords.size(); i++){
            if(this.words.badwords.get(i).equals(word)){
                this.words.badwords.remove(i);
            }
        }
        return this.words.badwords;
    }

    @RequestMapping(value = "/addGood/{word}" , method = RequestMethod.GET)
    public ArrayList<String> addGoodWord(@PathVariable("word") String word){
        this.words.goodwords.add(word);
        return this.words.goodwords;
    }

    @RequestMapping(value = "/delGood/{word}" , method = RequestMethod.GET)
    public ArrayList<String>  deleteGoodWord(@PathVariable("word") String word){
        for(int i = 0; i < this.words.goodwords.size(); i++){
            if(this.words.goodwords.get(i).equals(word)){
                this.words.goodwords.remove(i);
            }
        }
        return this.words.goodwords;
    }


    @RequestMapping(value ="/proof/{sentence}", method = RequestMethod.GET)
    public String proofSentence(@PathVariable("sentence") String sentence){

        boolean findGoodWord = false;
        boolean findBadWord = false;

        //'indexOf' จะคืนค่าเฉพาะหนึ่งครั้งต่อครั้งเมื่อพบคำที่ตรงกัน จะไม่ตรวจสอบทุกคำเหมือน'contains'
        //'contains' ที่คุณใช้ในตอนแรกมีประสิทธิภาพกว่าเนื่องจากมันจะค้นหาคำทั้งหมดในประโยค sentence

        for(int i=0; i < this.words.goodwords.size(); i++){
            if(sentence.contains(this.words.goodwords.get(i))){
                findGoodWord = true;
                break;
            }
        }

        for(int i=0; i < this.words.badwords.size(); i++){
            if(sentence.contains(this.words.badwords.get(i))){
                findBadWord = true;
                break;
            }
        }

        if(findGoodWord&findBadWord){
            rabbitTemplate.convertAndSend("Fanout","",sentence);
            return "Found Bad & Good Word";
        }
        else if(findGoodWord){
                rabbitTemplate.convertAndSend("Direct", "good",sentence);
            return "Found Good Word";
        }
        else if(findBadWord) {
            rabbitTemplate.convertAndSend("Direct", "bad", sentence);
            return "Found Bad Word";
        }
        return "Not Found";
    }


    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        return (Sentence) rabbitTemplate.convertSendAndReceive("Direct", "", "");
    }
}
