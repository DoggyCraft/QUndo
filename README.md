# QUndo

This plugin makes it easy to undo and redo changes from a certain point in time.

## Commands
All commands begin with "qundo" as the label, however it can be substituted with "q".

"/qundo" shows the plugins version and commands, as well as their uses.

"/qundo set" sets the time point in which the plugin will start recording blocks for the player.

"/qundo undo" undoes all the changes. This means puttin back blocks that were broken, and removing blocks that were broken. After undoing, the plugin no longer records block changes (Deactivates ChangeManager), however, the changes are still in memory.

"/qundo redo" redoes all the changes. Opposite of undo. One thing to note: This makes the plugin record block changes again (Activates the ChangeManager), so that next time undo is used, ALL the changes, from the beginning of the time point, are reversed to their original state.

"/qundo exit" removes all knowledge of block changes since the time point. This is a good idea to use, as the plugin keeps recording block changes, unless the player undoes the changes.

"/qundo help" help command. Does the same as "/qundo".

## Permission
There exists only one permission in this plugin.

"qundo.use" gives the player/group permission to use all of the commands above.
