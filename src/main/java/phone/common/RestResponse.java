package phone.common;

import lombok.Data;

/**
 * Created by wujl on 2017/11/10.
 */
@Data
public class RestResponse<T> {
    private int code;
    private String message;
    private T data;
}
