package com.example.movie2.Movie_Information;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("informationService")
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationMapper informationMapper;
}
