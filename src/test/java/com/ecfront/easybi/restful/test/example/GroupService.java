package com.ecfront.easybi.restful.test.example;

import com.ecfront.easybi.restful.exchange.annotation.Get;
import com.ecfront.easybi.restful.exchange.annotation.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Uri("group/")
public class GroupService {

    @Get("")
    public void findGroups() {
        logger.debug("findGroups");
    }

    @Get("*/user/")
    public List<Person> findUsersByGroup(int id) {
        logger.debug("findUsersByGroup,id=" + id);
        return null;
    }

    @Get("*/user/*")
    public List<Person> getUserByGroup(String groupId, String userId) {
        logger.debug("getUserByGroup,groupId=" + groupId + ",userId=" + userId);
        return null;
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


}
