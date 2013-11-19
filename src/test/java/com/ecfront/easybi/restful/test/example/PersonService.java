package com.ecfront.easybi.restful.test.example;

import com.ecfront.easybi.restful.exchange.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: EasyBI_Framework
 * Person: 震宇
 * Date: 13-9-8
 */
@Uri("user/")
public class PersonService {

    @Get("")
    public List<Person> findPersons() throws ParseException {
        logger.debug("findPersons");
        return new ArrayList<Person>() {{
            add(person1);
            add(person2);
            add(person3);
        }};
    }

    @Get("*/")
    public Person getPerson(String idcard) throws ParseException {
        logger.debug("getPerson,idcard=" + idcard);
        return person1;
    }

    @Post("")
    public Person savePerson(Person person) throws ParseException {
        logger.debug("savePerson,person=" + person.toString());
        return person;
    }

    @Put("*/")
    public Person updatePerson(String idcard, Person person) throws ParseException {
        logger.debug("updatePerson,idcard=" + idcard + ",person=" + person.toString());
        return person;
    }

    @Delete("*/")
    public void deletePerson(String idcard) throws ParseException {
        logger.debug("deletePerson,idcard=" + idcard);
    }


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Person person1;
    private Person person2;
    private Person person3;

    {
        person1 = new Person() {{
            setIdcard("111");
            setName("张三");
            setAge(21);
            try {
                setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1990-4-5"));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            setIncome(new BigDecimal(2332223L));
        }};
        person2 = new Person() {{
            setIdcard("222");
            setName("张四");
            setAge(40);
            try {
                setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1790-4-5"));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            setIncome(new BigDecimal(2332223L));
        }};
        person3 = new Person() {{
            setIdcard("333");
            setName("张五");
            setAge(41);
            try {
                setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1790-4-5"));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            setIncome(new BigDecimal(2332223L));
        }};
        person1.setParents(new ArrayList<Person>() {{
            add(person2);
            add(person3);
        }});
    }

}
