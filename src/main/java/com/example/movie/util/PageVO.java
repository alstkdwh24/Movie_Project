package com.example.movie.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVO {
    private int First;
    private int End;
    private boolean Prev;
    private boolean Next;

    private int page;
    private int amount;
    private  int total;
    private int RealEnd;
    private Criteria cri;

    private List<Integer> pagelist;
    public PageVO(Criteria cri, int total){
        this.page= cri.getPage();
        this.amount=cri.getAmount();
        this.total=total;
        this.cri=cri;

        this.End=(int)(Math.ceil(this.page/5.0)*5);

        this.First=this.End-5+1;

        this.RealEnd=(int)(Math.ceil(this.total/(double)this.amount));

        if(this.End>this.RealEnd){
            this.End=this.RealEnd;
        }

        this.Prev=this.First>1;

        this.Next=this.RealEnd>this.End;

        this.pagelist= IntStream.rangeClosed(this.First , this.End).boxed().collect(Collectors.toList());
    }

}
