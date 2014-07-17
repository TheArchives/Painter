Painter
=======

My plugins: [WordWarning](https://github.com/gdude2002/WordWarning) | **Painter**

Block replacement tools, plus support for LogBlock, Prism, Hawkeye and CoreProtect.

See the BukkitDev page (when I make one) for full documentation.

You can find development builds [on Bamboo](http://bamboo.gserv.me/browse/PLUG-PTR).
Remember, they haven't been approved by the BukkitDev staff! Use them at your own risk (but remember, you can check the
source included in the jar).

The latest built jar is always available
[at this directory listing](bamboo.gserv.me/browse/PLUG-PTR/latest/artifact/JOB1/Painter/).

Prism support
=============

If you want to use this with prism, bear in mind that Painter supports Prism custom actions - namely, it adds the `painter-block-paint` action.
You may have to add `Painter` to your allowed plugins for this. To do so, do the following..

* Open your `Prism/config.yml` file.
* Find the section named `allowed-plugins` - it's under `api`, under `tracking`, under `prism`.
* Add `Painter` to the list.
* You're ready to go!

Compiling
=========

Compilation of the plugin is fairly simple.

1. Install [the JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) (version 1.7 or higher)
2. Install [Gradle](http://www.gradle.org/)
3. Ensure that the JDK and Gradle are on your system's PATH
4. Open a terminal, `cd` to the project files and `gradle clean build`
5. You'll find the jar in `build/libs/`

I use Gradle instead of Maven simply because I don't like Maven, and Gradle is much easier to work with.
If you need to do Maven things, you can do `gradle install`, which will generate poms and install the plugin
into your local maven repository. Poms are generated in `build/poms/`.

---

My plugins: [WordWarning](https://github.com/gdude2002/WordWarning) | **Painter**