Painter
=======

My plugins: [WordWarning](https://github.com/gdude2002/WordWarning) | **Painter** | [LotteryBox](https://github.com/gdude2002/LotteryBox)

---

Block replacement tools, plus support for LogBlock, Prism, Hawkeye and CoreProtect, as well as permissions via Vault.

See the [BukkitDev page](http://dev.bukkit.org/bukkit-plugins/painter/) for full documentation.

You can find development builds [on Bamboo](http://bamboo.gserv.me/browse/PLUG-PTR).
Remember, they haven't been approved by the BukkitDev staff! Use them at your own risk (but remember, you can check the
source included in the jar).

The latest built jar is always available
[here](http://bamboo.gserv.me/browse/PLUG-PTR/latest/artifact/JOB1/Version-agnostic-jar/Painter.jar).

Maven/Ivy repos
===============

Gradle automatically builds the following repos for your use.

* Ivy: http://cherry.gserv.me/repos/ivy/
* Maven: http://cherry.gserv.me/repos/maven/

This plugin doesn't have an API, but if you need to use it as a dependency, you can use the above repos.

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

Buy me a soda
=============

Sometimes people ask me to accept donations. If you like what I do, you can donate [here](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=85GN242EDQSCJ).

---

My plugins: [WordWarning](https://github.com/gdude2002/WordWarning) | **Painter** | [LotteryBox](https://github.com/gdude2002/LotteryBox)
