# Ticketing
Below are the steps to setup dependent services for ticketing application

1. First of all you need to download and run keycloak server (you can follow this link https://www.tutorialsbuddy.com/keycloak-quickstart) but specify the values as mentioned in the screen-shots provided under **keycloak setup** folder in this repository.
2. To run keycloak server execute the command **./bin/standalone.sh
   ** on the directory where you have downloaded keycloak
3. Modify the credentials in **com.venturedive.app.ticketing.common.constant.GlobalConstant** class and the properties files for both **Keycloak and MySql**
4. You can use provided postman collection as well for accessing different APIs (API are self-explanatory)
5. Make sure that MySql is running on your machine (specify MySql configurations in properties files as you have configured on your machine)
6. Make sure that you MySql and Keycloak services are configured and running
7. Execute the following command to run application as per the given profile **mvn -Dspring.profiles.active=dev  spring-boot:run**