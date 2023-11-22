//package com.clientless.songmanagement.controller;
//
//import com.clientless.songmanagement.controller.mainfeature.GenreController;
//import com.clientless.songmanagement.dto.mainfeature.GenreDto;
//import com.clientless.songmanagement.model.Response;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class GenreControllerTest {
//
//    @Autowired
//    public MockMvc mockMvc;
//
//    @Mock
//    private GenreController genreController;
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void addGenreTest() throws Exception {
//        GenreDto genre = new GenreDto();
//        genre.setGenreType("pop1809");
//
//// Membuat objek ResponseEntity yang valid sesuai dengan tipe kembalian yang diharapkan
//        ResponseEntity<Response> responseEntity = ResponseEntity.status(HttpStatus.CREATED).build();
//
//        when(genreController.addGenre(Mockito.any(GenreDto.class))).thenReturn(responseEntity);
//
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/v1/genre/upload")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(genre))
//        );
//
//        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
//
//    }
//
//    @Test
//    public void listGenreTest() throws Exception {
//        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
//        when(genreController.listGenre()).thenReturn(responseEntity);
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get("/v1/genre/list")
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void editGenreTest() throws Exception {
//        GenreDto genreDto = new GenreDto();
//        genreDto.setGenreType("pop789");
//
//// Membuat objek ResponseEntity yang valid sesuai dengan tipe kembalian yang diharapkan
//        ResponseEntity<Response> responseEntity = ResponseEntity.ok().build();
//
//        when(genreController.editGenre(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
//
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .put("/v1/genre/edit/4")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(genreDto))
//        );
//
//        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
//
//}
