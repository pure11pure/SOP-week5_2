package com.example.week5_2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

//http://localhost:8080/index2
@Route(value = "index2")
public class MyView2 extends FormLayout {

    //ประกาศตัวแปร attribute ทั้งหมด
    private TextField addWord, addSentence;
    private Button btn_addGoodWord, btn_addBadWord, btn_addSentence, btn_showSentence;
    private ComboBox cb_goodWord, cb_badWord;
    private TextArea ta_goodSentence, ta_badSentence;
    private Notification noti;
    private WordPublisher wordPublisher;

    public MyView2(){

        //page1 _ left
        this.addWord = new TextField("Add Word");
        this.addWord.setWidthFull();
        this.btn_addGoodWord = new Button("Add Good Word");
        this.btn_addGoodWord.setWidthFull();
        this.btn_addBadWord = new Button("Add Bad Word");
        this.btn_addBadWord.setWidthFull();

        this.cb_goodWord = new ComboBox("Good Word");
        this.cb_goodWord.setWidthFull();
        this.cb_badWord = new ComboBox("Bad Word");
        this.cb_badWord.setWidthFull();
        // สร้างอ็อบเจ็กต์ WordPublisher และกำหนดให้กับตัวแปร wordPublisher
        this.wordPublisher = new WordPublisher();
        // Set items for the ComboBox cb_goodWord
        this.cb_goodWord.setItems(this.wordPublisher.words.goodwords);
        // Set items for the ComboBox cb_badWord
        this.cb_badWord.setItems(this.wordPublisher.words.badwords);

        VerticalLayout v1 = new VerticalLayout();
        v1.add(addWord,btn_addGoodWord,btn_addBadWord,cb_goodWord,cb_badWord);


        //page2 _ right
        this.addSentence = new TextField("Add Sentence");
        this.addSentence.setWidthFull();
        this.btn_addSentence = new Button("Add Sentence");
        this.btn_addSentence.setWidthFull();

        this.ta_goodSentence = new TextArea("Good Sentence");
        this.ta_goodSentence.setWidthFull();
        this.ta_badSentence = new TextArea("Bad Sentence");
        this.ta_badSentence.setWidthFull();
        this.btn_showSentence = new Button("Show Sentence");
        this.btn_showSentence.setWidthFull();

        VerticalLayout v2 = new VerticalLayout();
        v2.add(addSentence,btn_addSentence, ta_goodSentence, ta_badSentence, btn_showSentence);


        this.add(v1,v2);

        this.noti = new Notification();
        this.noti.setDuration(3000);
//        this.noti.setPosition(Notification.Position.BOTTOM_END);

        this.btn_addGoodWord.addClickListener(event -> {
            ArrayList out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/addGood/" + this.addWord.getValue())
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            //show at ComboBox >>> Good Word
            this.cb_goodWord.setItems(out);
            this.noti.setText("Insert " + this.addWord.getValue() + " to Good Word List Complete");
            this.noti.open();
        });

        this.btn_addBadWord.addClickListener(event -> {
            ArrayList out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/addBad/" + this.addWord.getValue())
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            //show at ComboBox >>> Bad Word
            this.cb_badWord.setItems(out);
            this.noti.setText("Insert " + this.addWord.getValue() + " to Bad Word List Complete");
            this.noti.open();
        });

        this.btn_addSentence.addClickListener(event -> {
            String out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/proof/" + this.addSentence.getValue())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            this.noti.setText(out);
            this.noti.open();
        });

        this.btn_showSentence.addClickListener(event -> {
            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            this.ta_goodSentence.setValue(String.valueOf(out.goodsentences));
            this.ta_badSentence.setValue(String.valueOf(out.badsentences));
        });

    }
}
