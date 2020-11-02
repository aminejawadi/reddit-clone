package com.example.reddit.mapper;


import com.example.reddit.DTO.SubredditDto;
import com.example.reddit.Models.Subreddit;

public interface SubredditMapper {


    <R> R mapSubredditToDto(Subreddit subreddit);

    Object mapDtoToSubreddit(SubredditDto subredditDto);
}