//package com.clientless.songmanagement.controller;
//
//import com.clientless.songmanagement.controller.mainfeature.ArtistController;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ArtistControllerTest {
//    @Autowired
//    public MockMvc mockMvc;
//
//    @Mock
//    private ArtistController artistController;
//
//    @Test
//    public void getImageByIdTest() throws Exception {
//        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
//        when(artistController.getImageById(Mockito.anyString())).thenReturn(responseEntity);
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.get("/v1/artist/view_photo/MSC1").contentType(MediaType.ALL)
//        );
//        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void listAlbumTest() throws Exception{
//        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
//        when(artistController.listArtist()).thenReturn(responseEntity);
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .get("/v1/artist/list_artist")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//        );
//
//        resultActions.andExpect(
//                MockMvcResultMatchers.status().isOk()
//        );
//    }
//
//    @Test
//    public void deleteAlbumTest() throws Exception{
//        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
//        when(artistController.deleteArtist(Mockito.anyString())).thenReturn(responseEntity);
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .delete("/v1/artist/delete/by/MSC1")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//        );
//
//        resultActions.andExpect(
//                MockMvcResultMatchers.status().isOk()
//        );
//    }
//
//    private byte[] readFileBytes() throws IOException {
//        Path path = Paths.get("src/main/resources/slip.png");
//        return Files.readAllBytes(path);
//    }
//
//    @Test
//    public void uploadAlbumTest() throws  Exception{
//        byte[] fileBytes = readFileBytes();
//        MockMultipartFile file = new MockMultipartFile("file", "image.png", MediaType.IMAGE_PNG_VALUE, fileBytes);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/v1/artist/upload")
//                .file(file)
//                .param("artistName", "LohKok?")
//                .param("yearActive","2021-01-12")
//                .param("genreType","pop")
//                .param("totalAlbum", "12")
//                .contentType(MediaType.MULTIPART_FORM_DATA);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        // Add other assertions to check data or other conditions
//
//    }
//
//}
