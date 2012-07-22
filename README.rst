====================
 JBoss Configurator
====================

.. contents::

Overview
========

This is a simple project aimed at making the management of multiple JBoss connection profiles a bit easier. This is achieved by providing a set of preferences that can easily be switched between in Eclipse, which will update the specified connection details in the relevant Data Source file.

Usage
=====

This plugin is designed to be used with a minimal amount of fuss. To get up and running is relatively simple. Firstly, you'll need to install the plugin (A built version of which is included in this project under the /build directory).
Simply drop this file into your Eclipse *dropins* directory.

Next - start Eclipse, and choose **Preferences --> JBoss Configurator**.

.. image:: JBoss-Configurator/doc/images/preferences.png

Configure the **JBoss Datasource File** to point at the relevant datasource under your JBoss directory.

.. image:: JBoss-Configurator/doc/images/jboss-directory.png

Next, navigate to the **JBoss Configurator Preferences** sub-page. From here, you can enter any relevant connection details, and maintain a list of connection profiles.

.. image:: JBoss-Configurator/doc/images/create-profile.png

Once a profile has been created, you can select it via the main preferences page. After selecting the profile, simply hit **Change Data Source** and the datasource file should be automatically updated.

.. image:: JBoss-Configurator/doc/images/change-datasource.png
