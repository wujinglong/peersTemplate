package phone.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import phone.Service.PhoneService;
import phone.common.RestResponse;
import phone.eneity.PhoneEntity;

/**
 * Created by wujl on 2017/11/10.
 */
@RestController
@RequestMapping("api")
@Slf4j
public class MainController {
    private static final int TYPE_PHONE_NUM = 3;

    @Autowired
    private PhoneService phoneService;


    @RequestMapping( value = "phone", method = RequestMethod.POST)
    public RestResponse<Integer> call(@RequestBody PhoneEntity phoneEntity, @RequestHeader("Authorization") String token) {

        RestResponse<Integer> PhoneResult = new RestResponse<>();
        if ( phoneEntity.getType() == TYPE_PHONE_NUM ) {
            log.info("[phone API] Type error,type only contain 0 or 3");
            PhoneResult.setCode(HttpStatus.BAD_REQUEST.value());
            PhoneResult.setMessage("[phone API] Type error,type only be use is 3, your Type is: " + phoneEntity.getType());
            return PhoneResult;
        }

        PhoneResult.setCode(HttpStatus.OK.value());
        try {
            phoneService.callPhone(phoneEntity);
        } catch (Exception e) {
            PhoneResult.setCode(HttpStatus.BAD_REQUEST.value());
            PhoneResult.setMessage(e.getMessage());
        }
        return PhoneResult;
    }
}
