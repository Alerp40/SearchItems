Description
-----------------------------
A minecraft mod to search already opened chests similar to terraria and other forge mods, this mod was developed with the intention of client side only so it can be used in ANY server without needing to add the mod to the server, this comes with limitations that will be talked about in the how to use.

-------------------------------
How to use
--------------
- Open a chest let the mod know what items are inside. (cannot check inside chest if it isnt opened because of client side only limitations)
- Click K to open the search menu.
- Search wichever item you want to find, if it doesnt appear you dont have it in the chests you opened.
Good to knows:
The mod checks every half a second if you have a chest screen open for performance reasons, if items arent showing up its most likely because you didnt open it for long enough. The mod stores the chest information on a session to session basis a permanent storage method will be implemented in future versions so chests need to be re openen every time you open and close minecraft.Right now there is not a range check so items appear on the search menu even if chests are thousands of blocks away, a range check will be implemented soon.

------------

Known Bugs
------------
- Items doubling up when double chests are open from both chest blocks.
- Chests items appear without range limit.
- If too many chests are open the gui gets too big and is unscrollable.

---------------------------------

Cloning the repo
----------------
If you do clone the repo to use the code to better or customise the mod the gradle settings and files are set up so everything is installed through maven2.fabricmc.net because of a cloudflare problem with my isp this leads to slower build times, if you do not have this problem i recommend changing them back to default.
