## File structure
The source code for app is located in the directory `io2019/app/src/`.  It contains two directories:
```
- java/io2019/nfsfinder - Kotlin source files (main activity and app file 
                          are stored here)
  - data - backend files
    - database - requests to REST API server handler
    - login - handlers of the process of logging in and caching logged user
    - maps - objects displayed on the map
  - ui - frontend files
    - login - login UI source
    - maps - maps UI source
- res - directory with all XML files needed to build app
  - layout - definitions of layouts of activities used in app
  - values - definitions of resources such as colors or strings
```