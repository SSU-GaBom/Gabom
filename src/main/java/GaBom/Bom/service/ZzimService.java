package GaBom.Bom.service;

import GaBom.Bom.advice.exception.CTravelNotFoundException;
import GaBom.Bom.advice.exception.CUserNotFoundException;
import GaBom.Bom.entity.Travel;
import GaBom.Bom.entity.User;
import GaBom.Bom.repository.TravelRepository;
import GaBom.Bom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ZzimService {
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;


    //travelId 누른거 찌짐
    @Transactional
    public boolean ZzimTravel (Long travelId) {
        Travel travel = travelRepository.findByTravelId(travelId).orElseThrow(CTravelNotFoundException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        //이미 눌렀던 상황이면 누르지 않게 해야함
        if(loginId=="anonymousUser") {
            log.info("session is ended");
            return false;
        }
        User user = userRepository.findByUserId(loginId).orElseThrow(CUserNotFoundException::new);
        if(!CheckZzim(user,travel)){
            log.info("찜 누름");
            user.addZzimTravel(travel);
            return true;
        }else {
            log.info("찜 못누름");
            return false;
        }
    }

    @Transactional
    public boolean CheckZzim(User user,Travel travel){
        List<Travel> ZzimTravelLists = user.getZzimTravelList();
        boolean Isthere = ZzimTravelLists.contains(travel);
        if(Isthere) return true;
        else return false;
    }


    @Transactional
    public boolean CancelZzimTravel (Long travelId) {
        Travel travel = travelRepository.findByTravelId(travelId).orElseThrow(CTravelNotFoundException::new);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        if(loginId=="anonymousUser") {
            log.info("session is ended");
            return false;
        }

        User user = userRepository.findByUserId(loginId).orElseThrow(CUserNotFoundException::new);
        if(CheckZzim(user,travel)){
            log.info("zzim cancel success");
            user.cancelZzimTravel(travel);
            return true;
        }else {
            log.info("zzim cancel failed");
            return false;
        }
    }

}
