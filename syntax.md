# ezDev Syntax Cheatsheet

## Syntax Phrases
Example:
```
<header> <trigger>:
    <action> <target> <value>
end
----------
repeat 6h:
    tell all "Hi!"
end
```
`repeat` is referred to as the "header". This initiates a new command, listener or runnable.

`6h` is referred to as the "trigger", but in the `repeat` context is also known as the `timeout`. See the Runnable section for formatting information.

`tell` is referred to as the "action". See the Action section below for all available actions.

`all` is the "target" for the `tell` action. The "target" is what the action will effect. All actions need "target"s.

`"Hi!"` is the "value" for the `tell` action. Many actions need values, but not all.

This simple command will send a message to all online players saying "Hi!".

---

## Commands:

- `command <label>:` - Where `<label>` is the command the player types in-game.
Currently arguments are not supported.
- See Actions section below.
- `end` - Tell ezDev your command section is complete.

## Listeners:
- `listener <event>:` - Where event is an event listed in the Events section below.
- See Actions section below.
- `end` - Tell ezDev your listener section is complete.

## Runnables/Repeating Tasks:
- `repeat <number><time>:` - Where `<time>` is one of the following:
    - s - Seconds
    - m - Minutes
    - h - Hours
- See Actions section below.
- `end` - Tell ezDev your runnable section is complete.

---

## Targets:

#### Command Context:
- `sender`: Player who ran the command. Using in a text message will automagically use their username.

#### Listener Context:
- `player`: The player (if applicable) who triggered the event. Using in a text message will automagically use their username.
- `entity`: The entity (if applicable) which triggered or was involved in the event.

#### Runnable/Repeating Task Context:
- null

---

### Actions:
- `permission <permission node>` - Require the sender has `<permission node>` to proceed. The permission node is the `<value>`.
- `tell <recipient> <message>` - Send a chat message to `<player(s)>`. 
    - Applicable `<recipient>` variables: `sender`, `player`, `all`.
- `give <recipient> <quantity> <item>` - Give `<player(s)>` an item.
    - Applicable `<recipient>` variables: `sender`, `player`, `all`.
    - `<quantity>` needs to be a positive integer.
    - `<item>` needs to be a [Minecraft Item Name](https://minecraft-ids.grahamedgecombe.com/), **not** item ID.
- `teleport <recipient> <x> <y> <z> <world> - Teleport `<recipient>` to a location.
    - Applicable `<recipient>` variables: `sender`, `player`, `all`.
- `execute <recipient> <command>` - Forces `<recipient>` to run `<command>` command.
    - Applicable `<recipient>` variables: `sender`, `player`, `all`.
    - If `<recipient>` does not have `<command>` permission, or any other sort of error, they will see the error message as normal. This is not "ghost".    

### Events:
- `join` - When a `player` joins the server.
- `leave` - When a `player` leaves the server.
- `chat` - When a chat message is sent.

### Custom Variables:
- Create in variables.json
- To use a custom variable, wrap it in curly braces. See Examples section if you need help.
- Examples:
```json
{
  "spawn": "-24, 60, 114",
  "sv_games_spawns": [
    "52, 71, 223",
    "60, 71, 500"
  ],
  "server_info": {
    "name": "VaultMC",
    "owner": "Aberdeener",
    "website": "https://vaultmc.net"
  }
}
```

---

## Tips and Tricks

- ezDev does not care about indentation, though I do recommend using it so your code is more readable.
- ezDev does care about line breaks, and your scripts will not work unless each action has its own line. This will hopefully be temporary.
- ezDev will be compatible with VaultAPI variables.

---

## Examples:

Simple Command
```
command hi:
    tell sender "Hello " + sender + "!"
    teleport sender: 73, 14, -143
end
```
Survival Games Command - By using the custom variable 'sv_games_spawns', a random location from the list will be chosen.
```
command sv:
    permission "surivalgames.join"
    teleport sender {sv_games_spawns}
    tell sender "The game starts in 1 minute."
end
```
Join Listener
```
listener join:
    tell all player + " has joined " + {server_info.name} + "!"
    give player 5 diamond_ore
    teleport player: {spawn}
end
```
Website Broadcast Runnable
```
repeat 5m:
    tell all "Check out our website: " + {server_info.website}
end
```

