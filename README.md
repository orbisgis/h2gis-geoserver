# geoserver-h2gis or H2GIS-GS

Is an extension to use H2GIS with Geoserver 2.6.2.

Note that this version doesn't support H2GIS linked table.
 
### Install

In a terminal, run

```bash
$ mvn clean install
```

* Go to your target folder, select the h2gis-gs-XX.jar and copy-paste it to the WEB-INF/lib directory of your GeoServer installation.
* In your target folder, go to plugin-dep that contains all dependencies. Select all jars and copy-paste it to the WEB-INF/lib directory of your GeoServer installation.
* Restart GeoServer


