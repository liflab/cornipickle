Cornipickle: a runtime monitor for layout constraints
=====================================================

[![Build Status](https://semaphoreapp.com/api/v1/projects/3f990e53-7e43-4198-941f-db77aa637459/344315/badge.png)](https://semaphoreapp.com/sylvainhalle/cornipickle)

Todo: describe.

Table of Contents                                                    {#toc}
-----------------

- [Compiling and installing Cornipickle](#install)
- [Command-line usage](#cli)
- [About the author](#about)

Compiling and Installing Cornipickle                             {#install}
------------------------------------

First make sure you have the following installed:

- The Java Development Kit (JDK) to compile. Cornipickle was developed and
  tested on version 6 of the JDK, but it is probably safe to use any
  later version. Moreover, it most probably compiles on the JDK 5, although
  this was not tested.
- [Ant](http://ant.apache.org) to automate the compilation and build process
- The [Apache Commons CLI](http://commons.apache.org/proper/commons-cli/)
  to handle command-line parameters *(tested with version 1.2)*
- The [json-simple](https://code.google.com/p/json-simple/) library for
  fast JSON parsing *(tested with version 1.1.1)*
- The [Web Resource Optimizer for Java](https://code.google.com/p/wro4j/)
  (wro4j) for minification of JavaScript files. Make sure to retrieve a JAR
  called "with dependencies". *(tested with version 1.6.3)*
- The [Bullwinkle parser](https://github.com/sylvainhalle/Bullwinkle),
  an on-the-fly parser for BNF grammars

The last libraries should be put in Java's extension folder. This location
varies according to the operating system you use:

- Solaris™ Operating System: `/usr/jdk/packages/lib/ext`
- Linux: `/usr/java/packages/lib/ext`
- Microsoft Windows: `%SystemRoot%\Sun\Java\lib\ext`

Do **not** create subfolders there (i.e. put the archive directly in that
folder).

Download the sources for Cornipickle from
[Bitbucket](http://bitbucket.org.com/sylvainhalle/cornipickle) or clone the
repository using Git:

    git@bitbucket.org:sylvainhalle/cornipickle.git

Compile the sources by simply typing:

    ant

This will produce a file called `Cornipickle.jar` in the folder. (Ignore the
slew of Javadoc warnings, which will be fixed some day but have no impact on
the build process.) This file is runnable and stand-alone, or can be used as
a library, so it can be moved around to the location of your choice.

In addition, the script generates in the `doc` folder the Javadoc
documentation for using Cornipickle. This documentation is also embedded in
the JAR file. To show documentation in Eclipse, right-click on the jar,
click "Properties", then fill the Javadoc location (which is the JAR
itself).

Command-line Usage                                                   {#cli}
------------------

Cornipickle works as a server. You start it on the command line with:

    java -jar Cornipickle.jar [options] [file1 [file2 ...]]

where `file1`, `file2`, etc. are optional filenames containing Cornipickle
specifications that the server can pre-load. Available options are:

`-h`, `--help`
:  Display command line usage
`-p`,`--port <x>` 
:  Listen on port x (default: 10101)
`-s`,`--servername <x>`
:  Set server name or IP address x (default: localhost)

### Built-in Examples

Cornipickle contains a few examples. Using the default settings, you can
try these examples by starting the server and visiting
[http://localhost:10101/examples/index.html](http://localhost:10101/examples/index.html)
in your browser.

### Status page

You can have more detailed status on the specifications that Cornipickle is
watching through a simple web interface. Using the default settings, you can
try these examples by starting the server and visiting
[http://localhost:10101/status](http://localhost:10101/status)
in your browser. Refresh the page to get updated info.


About the author                                                   {#about}
----------------

Cornipickle was written by Sylvain Hallé, associate professor at Université
du Québec à Chicoutimi, Canada.
