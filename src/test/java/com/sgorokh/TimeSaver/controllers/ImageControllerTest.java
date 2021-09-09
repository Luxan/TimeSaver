package com.sgorokh.TimeSaver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgorokh.TimeSaver.TimeSaverApplication;
import com.sgorokh.TimeSaver.controllers.helpers.ByteWrapper;
import com.sgorokh.TimeSaver.domain.servises.ImageService;
import com.sgorokh.TimeSaver.domain.servises.SessionService;
import com.sgorokh.TimeSaver.models.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {TimeSaverApplication.class})
public class ImageControllerTest {

    @Value("${api.path}")
    private String apiPath;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ImageService imageServiceMock;
    @MockBean
    private SessionService sessionServiceMock;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private Image image1;
    private Image image2;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        image1 = Image.builder()
                .id(1L)
                .name("1")
                .originalImage(ByteWrapper.bytesToBytes("1".getBytes(StandardCharsets.UTF_8)))
                .smallImage(ByteWrapper.bytesToBytes("22".getBytes(StandardCharsets.UTF_8)))
                .mediumImage(ByteWrapper.bytesToBytes("333".getBytes(StandardCharsets.UTF_8)))
                .build();

        image2 = Image.builder()
                .id(2L)
                .name("2")
                .originalImage(ByteWrapper.bytesToBytes("4444".getBytes(StandardCharsets.UTF_8)))
                .smallImage(ByteWrapper.bytesToBytes("55555".getBytes(StandardCharsets.UTF_8)))
                .mediumImage(ByteWrapper.bytesToBytes("6666666".getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    @Test
    public void it_should_return_home_images() throws Exception {
//        List<Image> imageList = Arrays.asList(image1, image2);
//        when(imageServiceMock.getHomeImages()).thenReturn(imageList);
//
//        mockMvc.perform(get(apiPath + "images/home")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.images[0].id").value(image1.getId()))
//                .andExpect(jsonPath("$.images[0].bytes", hasSize(image1.getMediumImage().length)))
//                .andExpect(jsonPath("$.images[1].id").value(image2.getId()))
//                .andExpect(jsonPath("$.images[1].bytes", hasSize(image2.getMediumImage().length)));
    }

    @Test
    public void it_should_return_portfolio_images() throws Exception {

    }

    @Test
    public void it_should_download_image_by_id() throws Exception {

    }

    @Test
    public void when_download_image_by_id_it_should_return404_if_image_not_found() throws Exception {

    }

    @Test
    public void it_should_download_images_by_session_id() throws Exception {

    }

    @Test
    public void when_download_image_by_session_it_should_return404_if_session_not_found() throws Exception {

    }

    @Test
    public void it_should_return_image_by_id() throws Exception {

    }

    @Test
    public void when_get_image_by_id_it_should_return404_if_image_not_found() throws Exception {

    }

    @Test
    public void it_should_return_images_by_session_id() throws Exception {

    }

    @Test
    public void when_get_image_by_session_it_should_return404_if_session_not_found() throws Exception {

    }

    @Test
    public void it_should_upload_images() throws Exception {

    }

    @Test
    public void it_should_upload_images_with_session_id_equals_0() throws Exception {

    }

    @Test
    public void when_upload_images_it_should_return400_if_session_not_found() throws Exception {

    }

    @Test
    public void it_should_delete_images() throws Exception {

    }

    @Test
    public void when_delete_images_it_should_return400_if_image_not_found() throws Exception {

    }
}
