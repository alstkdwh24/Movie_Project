package com.example.movie.util;

import lombok.Data;
import lombok.Getter;


@Data
public class Criteria {


    private Integer page;
    private int amount;

    private int First;
    private Integer g_number;
    private String nickname;
    @Getter

    private String username;


    public String setUsername(String username) {
        this.username = username;
        return username;
    }


    public Criteria() {
        this.page = 1;
        this.amount = 8;
    }

    public Criteria(int page, int amount) {
        super();
        this.page = page;
        this.amount = amount;
    }

    public int getpageStart() {
        return (page - 1) * amount;
    }

}

