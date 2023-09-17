package com.example.week5_2;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {

    protected Sentence sentence;

    public SentenceConsumer(){
        sentence = new Sentence();
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentences(String sentence){
        this.sentence.goodsentences.add(sentence);
//        System.out.println("AddGood : " + sentence);
        System.out.println("In addGoodSentence Methods : " + this.sentence.goodsentences);
    }

    @RabbitListener(queues = "BadWordQueue")
    public void addbadSentences(String sentence){
        this.sentence.badsentences.add(sentence);
//        System.out.println("AddBad : " + sentence);
        System.out.println("In addBadSentence Methods : " + this.sentence.badsentences);
    }

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentence(){
        return this.sentence;
    }
}
