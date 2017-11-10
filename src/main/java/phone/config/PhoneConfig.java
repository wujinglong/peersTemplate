package phone.config;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.peers.Config;
import net.sourceforge.peers.media.MediaMode;
import net.sourceforge.peers.media.SoundSource;
import net.sourceforge.peers.rtp.RFC3551;
import net.sourceforge.peers.rtp.RFC4733;
import net.sourceforge.peers.sdp.Codec;
import net.sourceforge.peers.sip.syntaxencoding.SipURI;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujl on 2017/11/10.
 */
@Slf4j
public class PhoneConfig implements Config{
    private InetAddress publicIpAddress;
    private String mediaFile = "music" + File.separator + "word.wav";
    private String user = "1001";
    private String domain = "***.****.***";
    private String password = "**********";

//    @Value("${phone.user}")
//    private String USER;
//
//    @Value("${phone.domain}")
//    private String DOMAIN;
//
//    @Value("${phone.password}")
//    private String PASSWORD;

//    public PhoneConfig() {}
//
//    public PhoneConfig(String user,String domain, String password) {
//        this.user = user;
//        this.domain = domain;
//        this.password = password;
//    }

    private SoundSource.DataFormat SOUND_DATA_FORMAT = SoundSource.DataFormat.LINEAR_PCM_8KHZ_16BITS_SIGNED_MONO_LITTLE_ENDIAN;
    public InetAddress getLocalInetAddress() {
        InetAddress inetAddress;
        try {
            // if you have only one active network interface, getLocalHost()
            // should be enough
            inetAddress = InetAddress.getLocalHost();
            // if you have several network interfaces like I do,
            // select the right one after running ipconfig or ifconfig
            //inetAddress = InetAddress.getByName("192.168.44.36");
        } catch (UnknownHostException e) {
            log.info(" [UnknownHostException] error is: {}",e.getCause());
            return null;
        }
        return inetAddress;
    }

    @Override public List<Codec> getSupportedCodecs() {
        List<Codec> supportedCodecs = new ArrayList();

        Codec codec = new Codec();
        codec.setPayloadType(RFC3551.PAYLOAD_TYPE_PCMU);
        codec.setName(RFC3551.PCMU);
        supportedCodecs.add(codec);

        codec = new Codec();
        codec.setPayloadType(RFC3551.PAYLOAD_TYPE_PCMA);
        codec.setName(RFC3551.PCMA);
        supportedCodecs.add(codec);

        codec = new Codec();
        codec.setPayloadType(RFC4733.PAYLOAD_TYPE_TELEPHONE_EVENT);
        codec.setName(RFC4733.TELEPHONE_EVENT);
        supportedCodecs.add(codec);

        return supportedCodecs;
    };

    //用到的配置
    @Override public String getUserPart() { return user; }
    @Override public String getDomain() { return domain; }
    @Override public String getPassword() { return password; }
    @Override public int getSipPort() { return 0; }
    @Override public int getRtpPort() { return 0; }
    @Override public InetAddress getPublicInetAddress() { return publicIpAddress; }
    @Override public void setPublicInetAddress(InetAddress inetAddress) {
        publicIpAddress = inetAddress;
    }
    @Override public String getAuthorizationUsername() { return getUserPart(); }
    //媒体接受格式
    @Override public MediaMode getMediaMode() { return MediaMode.file; }
    @Override public SoundSource.DataFormat getMediaFileDataFormat() { return SOUND_DATA_FORMAT;}
    //音乐文件位置
    @Override public String getMediaFile() { return mediaFile; }
    @Override public void setMediaFile(String mediaFile) { this.mediaFile = mediaFile; }

    //没用到的配置
    @Override public SipURI getOutboundProxy() { return null; }
    @Override public boolean isMediaDebug() { return false; }
    @Override public void setLocalInetAddress(InetAddress inetAddress) { }
    @Override public void setUserPart(String userPart) { }
    @Override public void setDomain(String domain) { }
    @Override public void setPassword(String password) { }
    @Override public void setOutboundProxy(SipURI outboundProxy) { }
    @Override public void setSipPort(int sipPort) { }
    @Override public void setMediaMode(MediaMode mediaMode) { }
    @Override public void setMediaDebug(boolean mediaDebug) { }
    @Override public void setRtpPort(int rtpPort) { }
    @Override public void save() { }
    @Override public void setAuthorizationUsername(String authorizationUsername) { }
    @Override public void setSupportedCodecs(List<Codec> codecs) {}
    @Override public void setMediaFileDataFormat(SoundSource.DataFormat var1) {}
}
