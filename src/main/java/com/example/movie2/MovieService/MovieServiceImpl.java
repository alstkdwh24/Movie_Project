package com.example.movie2.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("movieService")
public class MovieServiceImpl implements MovieService{

    @Autowired
    private MovieMapper movieMapper;
}
