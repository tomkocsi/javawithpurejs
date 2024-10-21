# Getting Started

## Git commands

### most common git commands 
git add .
git commit -m "commit text"

### transfer local repo to remote:
* 1. create a repo on GitHub
* 2. type:
git remote add szakdoga https://github.com/tomkocsi/oenikszakdolg.git  
git push -u szakdoga [branch name]
* GitHub username and password will be asked
***
*** instead of password use this:
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
***

### push commit to remote
git push szakdoga [branch name]
* if fails, pull first:
git pull szakdoga [branch name]


## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.3/maven-plugin/reference/html/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#using-boot-devtools)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OAuth2 Client](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#boot-features-security-oauth2-client)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.3/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

