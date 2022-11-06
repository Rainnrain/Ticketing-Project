package com.cydeo;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication// This includes @Configuartions so we don't need to make a config class
public class TicketingProjectDataApplication {

    public static void main(String[] args) {

        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    @Bean
    public ModelMapper mapper(){
        ModelMapper modelMapper= new ModelMapper();
      //  modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;



    }

}
