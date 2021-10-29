# Healenium plugin for ReportPortal

Healenium plugin for ReportPortal is an easy way to sort your test results to analyze changes in design locators for a time.

## Build instruction:

1. Clone current repository for plugin
2. Checkout source code
3. Move to root project folder and open console here
4. Perform command ```gradlew clean build```
   
   Command should successfully build the project:
![img.png](build.png)
   
5. As a result, new ```healenium-plugin-reportportal-1.0.jar``` file appeared in ```build/libs/``` directory
![img.png](jarfile.png)
   
6. Upload received file to Report Portal using following instruction: https://reportportal.io/docs/Plugins
   
   Also, you can find helpful instructions for uploading jar files into Report Portal here: https://github.com/reportportal/reportportal/issues/680
   
## Usage

* Make Healenium plugin Enabled on Report Portal

   Than it will be available for your project
  ![img.png](rpplugin.png)
  
* Run your automation tests
* In case when healing has been run and passed test attributes on RP will be like:
![img.png](hlmtrue.png)

* In case when healing has been run and failed test attributes on RP will be like:
![img_1.png](hlmfalse.png)
  
* In case if healing is not necessary there will be no related attributes 