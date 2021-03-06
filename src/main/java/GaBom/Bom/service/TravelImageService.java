package GaBom.Bom.service;

import GaBom.Bom.advice.exception.CImageNotFoundException;
import GaBom.Bom.advice.exception.CNotSameUserException;
import GaBom.Bom.advice.exception.CUserNotFoundException;
import GaBom.Bom.dto.PinDto;
import GaBom.Bom.dto.TravelImageDto;
import GaBom.Bom.entity.*;
import GaBom.Bom.repository.PinRepository;
import GaBom.Bom.repository.TravelImageRepository;
import GaBom.Bom.repository.TravelRepository;
import GaBom.Bom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getFullPath;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelImageService {
    private final TravelImageRepository travelImageRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    @Value("/dynamic/travelImage/")
//    @Value("C:\\Users\\psg98\\GaBom\\travelImage\\")
//    @Value("C:\\Users\\sion\\Desktop\\") //변경가능
    private String imageDir;

    public String getFullPath(String fileName) {
        return imageDir + fileName;
    } //저장되는곳.

    @Transactional
    public void createPin(Pin pin, List<TravelImage> base64Images) throws IOException {
        //이미지를 실제로 저장하고 이미지객체를 아이템과 연관시키기
        for (TravelImage base64Image : base64Images) {
            TravelImage image3 = travelImage(base64Image);
            System.out.println("!! base64Image = " + base64Image.getFileName());
            String[] filetype = base64Image.getFileName().split("\\.");
            if (filetype.length > 0) base64Image.setIdentifier(filetype[filetype.length - 1]);
            base64Image.setTravelFileName(image3.getTravelFileName());
            pin.setTravelImage(base64Image);
        }
    }

//    public List<TravelImage> readImage(Pin pin) throws IOException {
//        log.info("ImageService : readImage");
//        List<TravelImage> travelImageList = new ArrayList<>();
//
//        for (TravelImage image : pin.getImages()) {
//            File file = new File(getFullPath(image.getStoreImageName()));
//            InputStream ins = new FileInputStream(file);
//            String base64Image = Base64.getEncoder().encodeToString(StreamUtils.copyToByteArray(ins));
//            imageDtoList.add(ImageDto.builder().base64Image(base64Image).imageName(image.getUploadImageName()).build());
//        }
//
//        return imageDtoList;
//    }


    public TravelImage travelImage(TravelImage image) throws IOException {
        log.info("Service : travelImage");
        String travelFileName = createTravelFileName(image.getFileName()); //파일이름
        //File imagefile = new File(getFullPath(travelFileName));
        byte[] decodeBytes = Base64.getDecoder().decode(image.getBase64Image().getBytes());
        //FileOutputStream fos = new FileOutputStream(imagefile);
        //fos.write(decodeBytes);
        //fos.close();
        String path = getFullPath(travelFileName);
        log.info("Service : travelImage2");
        return TravelImage.builder()
                .travelFileName(path)
                .build();
    }

    private String createTravelFileName(String filename) {
        System.out.println("filename = " + filename);
        String[] words = filename.split("\\.");
        int last = words.length;
        if (last <= 0) {
            log.info("사진 파일이 들어오지 않았습니다!!");
            return "Error\n";
        }
        String filetype = words[last - 1];
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + filetype;
    }
}
