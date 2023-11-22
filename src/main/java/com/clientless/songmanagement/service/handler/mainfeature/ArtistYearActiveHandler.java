package com.clientless.songmanagement.service.handler.mainfeature;

import java.time.LocalDate;

public class ArtistYearActiveHandler {
    public LocalDate handlerYear(String releasedYearRequest){
        String[] requestToArray = releasedYearRequest.split("-");
        return LocalDate.of(
                Integer.parseInt(requestToArray[0]),
                Integer.parseInt(requestToArray[1]),
                Integer.parseInt(requestToArray[2])
        );
    }
}
