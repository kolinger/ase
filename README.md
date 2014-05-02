ASE platform
============

Agent Simulation Environment platform

Requirements
------------
- java 1.7
- maven
- git
- ZeroMQ 3.2.3
- JZMQ (master)

Download
--------
`git clone https://github.com/kolinger/ase-node.git`

Build
-----
`mvn package`

Configuration
-------------
Copy `src/main/resources/configuration_example.xml` as `configuration.xml` to `/path/to/somewhere` and configure it.

Running
-------
`java -cp "/path/to/configuration:/path/to/ase-node-jar-with-dependencies.jar" cz.uhk.fim.ase.App`

In some cases may need to specify library path for ZeroMQ and JZMQ. For example:

`java -cp "/path/to/somewhere:/path/to/ase-node-jar-with-dependencies.jar" -Djava.library.path=/usr/local/lib cz.uhk.fim.ase.App`
