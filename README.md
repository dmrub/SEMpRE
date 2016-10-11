# SEMpRE

In very short: Generates an ARVIDA scenegraph from a given `.ldr` distribution of OLE asset.

```
mvn clean generate-resources package tomcat7:run
```

## Usage

```
GET http://sempre-frontend/models?uri=http://example.com/foo.ldr
```

## Acknowledgements
This work has been supported by the [German Ministry for Education and Research (BMBF)](http://www.bmbf.de/en/index.html) (FZK 01IMI3001 J) as part of the [ARVIDA](http://www.arvida.de/) project.
