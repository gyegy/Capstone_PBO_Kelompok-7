# AEROCARE GUI upgrade

GUI starter screens have been added under `src/main/java/view`. The project entry point now opens the Swing login screen, and login lookup has been corrected so the controller can validate the password after loading an active user by username.

## Apply the full view overlay
The repository branch includes the login entry screen and login query fix. The remaining screen set (role dashboards, module workspaces, shared theme/layout helpers and matching NetBeans `.form` files) is provided in the companion `AEROCARE_GUI_Upgrade.zip` delivered in the ChatGPT conversation. Extract its `src/main/java/view` folder over the repository's matching folder.

## Notes
- Open the Maven project from `pom.xml` in Apache NetBeans.
- The UI shell currently contains sample rows and summary values; connect each module to its DAO/controller before treating those values as live database data.
- The app requires the MySQL database `aerocare` and the existing connection configuration.
