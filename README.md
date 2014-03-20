## The light weight Restful tools.

### How to use

@Uri : Class annotation,  defined root path in the current class

@Get @Post @Put @Delete : Method annotation, defined request method type.

Method parameter :

    method(<URL wildcard>,Map<String,String[]> parameter,List<FileItem> fileItems,<Object> object)
        URL wildcard : The number of parameters same as number of url wildcards ,
                                Order is the same as the url wildcards ,
                                Type requirements : basic types and packaging 、UUID 、 String 、BigDecimal .
        Map<String,String[]>  : Parameter.
        List<FileItem> : Upload files.
        <Object>  : Model Or VO object.

example :

    @Uri("user/")
    public class PersonService {

    	@Get("")
    	public List<Person> findPersons()  {
    	}

    	@Get("*/")
    	public Person getPerson(String idcard)  {
    	}

    	@Post("")
    	public Person savePerson(Person person)  {
    	}

    	@Put("*/")
    	public Person updatePerson(String idcard, Person person)  {
    	}

    	@Delete("*/")
    	public void deletePerson(String idcard) {
    	}

    }

Configure :

    ez_restful_scan_base_path=com.ecfront
    #Set Security implement when ez_restful_spring_support=false
    ez_restful_security=
    ez_restful_spring_support=false
    #Use jsonp response when request contains this val.
    ez_restful_jsonp_callback=jsonpCallback

Web support:

    Web.xml
    <servlet>
    <servlet-name>servlet</servlet-name>
    <servlet-class>com.ecfront.easybi.restful.exchange.RestfulServlet</servlet-class>
    </servlet>
    <servlet-mapping>
    <servlet-name>servlet</servlet-name>
    <url-pattern>/api/*</url-pattern>
    </servlet-mapping>

Spring support :

    <bean id="springContextHolder" class="com.ecfront.easybi.restful.exchange.spring.SpringContextHolder" lazy-init="false"/>

    <context:component-scan base-package="com.ecfront">
        <context:include-filter type="annotation" expression="com.ecfront.easybi.restful.exchange.annotation.Uri"/>
    </context:component-scan>


### Building from source
The Project uses a [Maven][]-based build system.

### Check out sources
`git clone https://github.com/gudaoxuri/EZ-Restful.git`

### License

Under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

[Maven]:http://maven.apache.org/
