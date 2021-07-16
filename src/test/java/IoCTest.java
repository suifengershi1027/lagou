
import com.lagou.edu.factory.BeanFactory;
import com.lagou.edu.service.TransferService;
import org.junit.Test;

/**
 * @author 应癫
 */
public class IoCTest {

    /**
     * 事务
     * @throws Exception
     */
    @Test
    public void testIoC() throws Exception {
        TransferService transferService = (TransferService) BeanFactory.getBean("transferService");
        transferService.transfer("1", "2", 2);
        System.out.println(transferService);

    }
}
