package com.Bleedy;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userIdentification;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> blackListTags= new ArrayList<String>();
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> blackListChanel= new ArrayList<String>();

    protected User (){}

    public User(Long userIdentification){
        this.userIdentification = userIdentification;
    }


    public void addTagInBlackList(String tag) {
        blackListTags.add(tag);
    }

    public void addChanelInBlackList(String chanel) {
        blackListChanel.add(chanel);
    }

    public Long getUserIdentification() {
        return userIdentification;
    }

    public void setUserIdentification(Long userIdentification) {
        this.userIdentification = userIdentification;
    }


    public List<String> getBlackListTags() {
        return blackListTags;
    }

    public void setBlackListTags(List<String> blackListTags) {
        this.blackListTags = blackListTags;
    }


    public List<String> getBlackListChanel() {
        return blackListChanel;
    }

    public void setBlackListChanel(List<String> blackListChanel) {
        this.blackListChanel = blackListChanel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userIdentification.equals(user.userIdentification);
    }
    @Override
    public String toString() {
        return String.format(
                "User [id=%d, userID='%s']",
                id, userIdentification);
    }
}