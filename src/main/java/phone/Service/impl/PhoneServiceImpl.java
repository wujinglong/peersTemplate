package phone.Service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import phone.Service.PhoneService;
import phone.eneity.PhoneEntity;
import phone.sip.PhoneEvent;

import java.util.List;

/**
 * Created by wujl on 2017/11/10.
 */
@Service(value = "phoneService")
@Slf4j
public class PhoneServiceImpl implements PhoneService{

    private static final String PHONE_ZONE_DEFAULT_SHANGHAI = "上海";

    @Autowired
    private PhoneEvent phoneEvent;

    @Value("${phone.domain}")
    private String HOST;

    @Override
    public void callPhone(PhoneEntity phoneEntity) {
        List<String> mobiles = phoneEntity.getTo();

        for( String phone: mobiles) {
            log.info("mobile is:{}",phone);
            if( phone == null) {
                continue;
            }

            try {
                String callTime = phoneEvent.callPhone(getCallAddress(phone));
                log.info("主叫: {}, 被叫: {}, 通话时长: {}", phoneEntity.getCaller(),phone,callTime);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }


    private String getCallAddress(String phone) {
        String zone = "上海";
        switch (zone) {
            case PHONE_ZONE_DEFAULT_SHANGHAI:
                return  "sip:9" + phone + "@" + HOST;
            default:
                return  "sip:90" + phone + "@" + HOST;
        }
    }
}
