package phone.eneity;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by wujl on 2017/11/10.
 */
@Data
public class PhoneEntity {
    @javax.validation.constraints.NotNull(message = "to can't null")
    @Size(min = 1)
    private List<String> to;

    private int type;

    private String word;

    private String caller;

    public String toString() {
        return "[Phone API] To: " + to.toString() + " Type is: " + type;
    }

}
