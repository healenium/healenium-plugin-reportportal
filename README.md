# Healenium plugin for ReportPortal

Healenium plugin for ReportPortal is an easy way to sort your test results to analyze changes in design locators for a time.

## Build instruction:

1. Download jar asset from releases https://github.com/healenium/healenium-plugin-reportportal/releases 

2. Upload received file to Report Portal using following instruction: https://reportportal.io/docs/plugins/ManagePlugins
   
   Also, you can find helpful instructions for uploading jar files into Report Portal here: https://github.com/reportportal/reportportal/issues/680

3. Ensure that Healenium is set up and successfully launched. In this repository, in the Healenium folder, you can also find docker-compose files for launching Healenium.

## Usage

* Make Healenium plugin Enabled on Report Portal

   Than it will be available for your project
  ![img.png](rpplugin.png)
  
## Run your automation tests

* In case when healing has been run and passed test attributes on RP will be like:
![img.png](hlmtrue.png)

* In case when healing has been run and failed test attributes on RP will be like:
![img_1.png](hlmfalse.png)
  
* In case if healing is not necessary there will be no related attributes 

* For successful use of Healenium-Proxy with RP plugin, you need to use any library to broadcast hlm-proxy WARN logs to the console. For java, you can use TailerListenerAdapter. Test example: https://github.com/healenium/healenium-plugin-reportportal/tree/main/src/test/java/proxy