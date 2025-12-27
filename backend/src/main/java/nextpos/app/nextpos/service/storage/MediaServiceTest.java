// package nextpos.app.nextpos.service.storage;

// import nextpos.app.nextpos.service.impl.MediaServiceImpl;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.test.context.ActiveProfiles;

// import static org.junit.jupiter.api.Assertions.assertNotNull;

// @SpringBootTest
// @ActiveProfiles("test")
// class MediaServiceTest {

//     @Autowired
//     private MediaServiceImpl mediaService;

//     @Test
//     void testUploadFile() throws Exception {
//         MockMultipartFile file = new MockMultipartFile(
//                 "file",
//                 "test-image.jpg",
//                 "image/jpeg",
//                 "test image content".getBytes());

//         // Add test logic here
//         assertNotNull(mediaService);
//     }
// }