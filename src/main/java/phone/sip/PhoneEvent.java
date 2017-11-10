package phone.sip;

import net.sourceforge.peers.Config;
import net.sourceforge.peers.Logger;
import net.sourceforge.peers.media.AbstractSoundManager;
import net.sourceforge.peers.media.AbstractSoundManagerFactory;
import net.sourceforge.peers.media.MediaManager;
import net.sourceforge.peers.media.SoundSource;
import net.sourceforge.peers.media.javaxsound.JavaxSoundManager;
import net.sourceforge.peers.rtp.RFC4733;
import net.sourceforge.peers.sip.Utils;
import net.sourceforge.peers.sip.core.useragent.SipListener;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;
import org.springframework.stereotype.Service;
import phone.config.PhoneConfig;
import phone.logs.phoneLogger;

import java.net.SocketException;
import java.text.DecimalFormat;

/**
 * Created by wujl on 2017/11/10.
 */
@Service
public class PhoneEvent implements SipListener,AbstractSoundManagerFactory{
    private UserAgent userAgent;
    private SipRequest sipRequest;
    private AbstractSoundManager soundManager;
    private Logger logger = new phoneLogger(null);

    private static final DecimalFormat SESSION_TIME_FORMAT = new DecimalFormat("0.00");
    private long sessionStartTime = 0;
    private long sessionTime = 0;
    private String callSecond = "0";
    private volatile boolean CALL_FLAG = false;

    public PhoneEvent() {
        try {
            Config config = new PhoneConfig();
            soundManager = new JavaxSoundManager(false,logger,null);
            userAgent = new UserAgent(this,config,logger);
            userAgent.register();

        } catch (SocketException | SipUriSyntaxException e) {
            logger.info("PhoneEvent() init SocketException: " + e.getMessage());
        }
    }

    //begin to call with useAgent
    public void call(String callee) {
        try {
            String callId = Utils.generateCallID(userAgent.getConfig().getLocalInetAddress());
            sipRequest = userAgent.invite(callee,callId);  //(String requestUri, String callId)

            MediaManager mediaManager = userAgent.getMediaManager();
            mediaManager.waitConnected();
            SoundSource soundSource = mediaManager.getSoundSource();
            soundSource.waitFinished();
            mediaManager.waitFinishedSending();
//            mediaManager.stopSession();
            userAgent.terminate(sipRequest);
            //set callSecond and callFlag
            setCallSecondAndCallFlag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registering(SipRequest sipRequest) { };

    @Override
    public void registerSuccessful(SipResponse sipResponse) { logger.info("register successful"); };

    @Override
    public void registerFailed(SipResponse sipResponse) { logger.info("register failed"); };

    @Override
    public void incomingCall(SipRequest sipRequest, SipResponse sipResponse) {
    };

    @Override
    public void remoteHangup(SipRequest sipRequest) {
        logger.info("remote hangup");
        setCallSecondAndCallFlag();
    };

    @Override
    public void ringing(SipResponse sipResponse) { };

    @Override
    public void calleePickup(SipResponse sipResponse) {
        logger.info("user pick up phone");
        sessionStartTime = System.currentTimeMillis();
    };

    @Override
    public void error(SipResponse sipResponse) {
        logger.info("code: " + sipResponse.getStatusCode());
        logger.info("reson: " + sipResponse.getReasonPhrase());
        logger.info("msg: " + sipResponse.toString());

        setCallSecondAndCallFlag();
    };

    @Override
    public void dtmfEvent(RFC4733.DTMFEvent var1, int var2) {
        logger.info("dtmfEvent" + var1.toString());
    };

    @Override
    public AbstractSoundManager getSoundManager() {
        return this.soundManager;
    }

    //get session time and end thread
    private void setCallSecondAndCallFlag() {
        sessionTime = (System.currentTimeMillis() - sessionStartTime) / 1000;
        CALL_FLAG = true;
    }

    //set session time
    private void setSessionTime() {
        callSecond = sessionStartTime == 0  ? "0" : SESSION_TIME_FORMAT.format(sessionTime);
    }
    //param init
    private void initParams() {
        sessionStartTime = 0;
        sessionTime = 0;
        callSecond = "0";
        CALL_FLAG = false;
    }

    public String callPhone( String phoneNumber) {
        initParams();

        //call phone
        new Thread() {
            @Override
            public void run() {
                call(phoneNumber);
            }
        }.start();

        // wait call finished
        while (!CALL_FLAG) {
        }
        setSessionTime();
        return callSecond;
    }

}
