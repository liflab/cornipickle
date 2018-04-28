Cornipickle: a runtime monitor for layout constraints
=====================================================

[![Travis](https://img.shields.io/travis/liflab/cornipickle.svg?style=flat-square)](https://travis-ci.org/liflab/cornipickle)
[![SonarQube Coverage](https://sonarcloud.io/api/project_badges/measure?project=liflab%3Acornipickle&metric=coverage)](https://sonarcloud.io/dashboard?id=liflab%3Acornipickle)

Cornipickle is an automated testing tool that can verify declarative properties
on-the-fly as a user interacts with an application. Test oracles are written in
a a declarative language that can express desirable properties of a web
application as a set of human-readable assertions on the page's HTML and CSS
data.


Table of Contents                                                    {#toc}
-----------------

- [Compiling and installing Cornipickle](#install)
- [Command-line usage](#cli)
- [Using Cornipickle on your server](#server)
- [Status page](#status)
- [Further documentation](#doc)
- [About the authors](#about)

Compiling and Installing Cornipickle                             {#install}
------------------------------------

## With Docker

First make sure you have a docker installed.

Build:

```
docker build . --tag cornipickle
```

Run

```
docker run -it -p10101:10101 cornipickle
```

## Without Docker

First make sure you have the following installed:

To use Cornipickle, the easiest way is to download a pre-compiled release
from the [Releases](https://github.com/liflab/cornipickle/releases) page.

If you want to build Cornipickle by yourself, first make sure you have the
following installed:

- The Java Development Kit (JDK) to compile. Cornipickle was developed and
  tested on version 6 of the JDK, but it is probably safe to use any later
  version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

Download the sources for Cornipickle from
[GitHub](https://github.com/liflab/cornipickle) or clone the repository using
Git:

    git@github.com:liflab/cornipickle.git

### Installing dependencies

The project requires the following libraries to be present in the system:

- The [json-lif](https://github.com/liflab/json-lif) library for
  fast JSON parsing *(tested with version 1.6.3)*
- The [Azrael](https://github.com/sylvainhale/Azrael) library for
  serialization of Java objects. *(tested with version 0.7.3-alpha)*
- The [Bullwinkle parser](https://github.com/sylvainhalle/Bullwinkle),
  an on-the-fly parser for BNF grammars. *(tested with version 1.4.2)*
- The [Jerrydog server](https://github.com/sylvainhalle/Jerrydog) for basic
  HTTP server capabilities. *(tested with version 0.3.1-alpha)*

Using Ant, you can automatically download any libraries missing from your
system by typing:

    ant download-deps

This will put the missing JAR files in the `dep` folder in the project's
`Server` folder.

### Compiling

Compile the sources by simply typing:

    ant

This will produce a file called `cornipickle.jar` in the folder. This file
is runnable and stand-alone, or can be used as a library, so it can be moved
around to the location of your choice.

In addition, the script generates in the `doc` folder the Javadoc
documentation for using Cornipickle.

### Testing

Cornipickle can test itself by running:

    ant test

Unit tests are run with [jUnit](http://junit.org); a detailed report of
these tests in HTML format is availble in the folder `tests/junit`, which
is automatically created. Code coverage is also computed with
[JaCoCo](http://www.eclemma.org/jacoco/); a detailed report is available
in the folder `tests/coverage`.

Command-line Usage                                                   {#cli}
------------------

Cornipickle works as a server. You start it on the command line with:

    java -jar cornipickle.jar [options] [file1 [file2 ...]]

where `file1`, `file2`, etc. are optional filenames containing Cornipickle
specifications that the server can pre-load. Available options are:

`-h`, `--help`
:  Display command line usage

`-p`,`--port <x>`
:  Listen on port x (default: 10101)

`-s`,`--servername <x>`
:  Set server name or IP address x (default: localhost)

`--serve-as <path>`
:  Serve local folder as remote folder <path>. For example, with the default
   settings, when launching Cornipickle with `--serve-as myfolder/` (note the
   trailing slash), the URL `http://localhost:10101/myfolder/foo.bar` will
   refer to `foo.bar` from the local folder where Cornipickle is launched.
   This also works for nested folders.

Using Cornipickle on your server                                  {#server}
--------------------------------

Cornipickle uses Ajax to relay information between the browser and its
interpreter. If you want to use Cornipickle to test an application hosted on
your server, you need to enable [Cross-origin resource
sharing](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing) (CORS)
on the server that hosts your site.

Suppose that:

- your site is hosted on `myserver.com`, running on a server that
  listens to port `80`;
- you wish to run the Cornipickle server on `myserver.com`, listening to port
  `10101` (it cannot be set to the same port as your web server). 

Normally, as per the Same-Origin Policy (SOP), a page served from
`myserver.com:80` is forbidden to send an Ajax request to
`myserver.com:10101` --meaning that the web site will not be able to relay
data to Cornipickle. To enable CORS on the server, please refer to this
page: https://enable-cors.org/server.html. For example, on Apache, you must add
the following line to the server's `.htaccess` configuration file:

    Header set Access-Control-Allow-Origin "http://myserver.com:10101"

Built-in Examples
-----------------

Cornipickle contains a few examples. Using the default settings, you can
try these examples by starting the server and visiting
[http://localhost:10101/examples/index.html](http://localhost:10101/examples/index.html)
in your browser.

Status page                                                       {#status}
-----------

You can have more detailed status on the specifications that Cornipickle is
watching through a simple web interface. Using the default settings, you can
try these examples by starting the server and visiting
[http://localhost:10101/status](http://localhost:10101/status)
in your browser. Refresh the page to get updated info.

Further documentation                                                {#doc}
---------------------

More online documentation about Cornipickle is under way. In the meantime,
please refer to Cornipickle's page on the LIF's website for references to
presentation slides and research papers detailing how the tool works and how to
use the language.

Please feel free to contact us if you have any questions, or if you want to use
Cornipickle in a specific context.

About the authors                                                  {#about}
-----------------

Cornipickle was first written by [Sylvain Hallé](http://leduotang.ca/sylvain),
associate professor at [Université du Québec à Chicoutimi](https://www.uqac.ca),
Canada, and head of the [Laboratoire d'informatique
formelle](https://liflab.ca). Special thanks to the team of of graduate and
undergraduate students who contributed to the development of Cornipickle:

- Gabrielle Bastien
- Nicolas Bergeron
- Oussama Beroual
- Xavier Chamberland-Thibeault
- Francis Guérin
- Chafik Meniar
- Florence Opalvens
- Jérémy Spieldenner

<!-- :wrap=hard:maxLineLen=80: -->
