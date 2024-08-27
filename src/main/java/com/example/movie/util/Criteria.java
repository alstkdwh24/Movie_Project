package com.example.movie.util;

import lombok.Data;

@Data
public class Criteria {
    private int page;
    private int amount;
    private Integer free_number;
    private int First;
    private  Integer g_number;
    private String nickname;
    public Criteria(){
        this.page=1;
        this.amount=8;
    }
    public Criteria(int page, int amount){
        super();
        this.page = page;
        this.amount=amount;
    }
public int getpageStart(){
        return (page - 1)*amount;
}

}
