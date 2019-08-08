package gjw.wx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxApplicationTests {

    @Test
    public void contextLoads() {
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        String name = demo.a;
        String name1 = Demo.a;
        String name12 = demo.b;
        //String name13 = Demo.b;
    }

}
