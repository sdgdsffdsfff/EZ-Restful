package com.ecfront.easybi.restful.test;

import com.ecfront.easybi.base.utils.PropertyHelper;
import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.ResponseVO;
import com.ecfront.easybi.restful.exchange.Restful;
import com.ecfront.easybi.restful.inner.UniformCode;
import com.ecfront.easybi.restful.test.example.Person;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

public class RestfulTest {

    @Test
    public void test() throws Exception {
        Restful.getInstance().init(PropertyHelper.get("ez_restful_scan_base_path"));
        ResponseVO vo = Restful.getInstance().excute(HttpMethod.GET, "group/", null);
        Assert.assertEquals(vo.getCode(), UniformCode.SUCCESS.getCode());
        vo = Restful.getInstance().excute(HttpMethod.GET, "group/1/user/", null);
        Assert.assertEquals(vo.getCode(), UniformCode.SUCCESS.getCode());
        vo = Restful.getInstance().excute(HttpMethod.GET, "group/1/user/111/", null);
        Assert.assertEquals(vo.getCode(), UniformCode.SUCCESS.getCode());
        vo = Restful.getInstance().excute(HttpMethod.GET, "user/", null);
        Assert.assertEquals(((List<Person>) vo.getBody()).get(0).getIdcard(), "111");
        vo = Restful.getInstance().excute(HttpMethod.GET, "user/111/", null);
        Assert.assertEquals(((Person) vo.getBody()).getIdcard(), "111");
        vo = Restful.getInstance().excute(HttpMethod.POST, "user/", new Person() {{
            setIdcard("qqq");
            setAge(41);
        }});
        Assert.assertEquals(((Person) vo.getBody()).getIdcard(), "qqq");
        vo = Restful.getInstance().excute(HttpMethod.PUT, "user/111/", new Person() {{
            setIdcard("111");
            setAge(41);
        }});
        Assert.assertEquals(((Person) vo.getBody()).getIdcard(), "111");
        Restful.getInstance().excute(HttpMethod.DELETE, "user/111/", null);
    }
}
