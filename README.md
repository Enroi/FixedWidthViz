# Fixed Width files transformer.
## General description
This application transforms fixed width files to html page with one table.  
Format of fixed width file is described in file ``src/main/resources/TransformerConfig.json``  
For convert fixed width file to html format application should be started with such command line parameters  
--sourcefile=< PATH TO SOURCE FILE >  
--destinationfile=< PATH WHERE GENERATED HTML FILE SHOULD BE PLACED >  
## Example
File ``src/main/resources/TransformerConfig.json`` has example configuration for format fised width file [https://www1.ncdc.noaa.gov/pub/data/ghcn/daily/ghcnd-stations.txt](https://www1.ncdc.noaa.gov/pub/data/ghcn/daily/ghcnd-stations.txt) to HTML format. 
